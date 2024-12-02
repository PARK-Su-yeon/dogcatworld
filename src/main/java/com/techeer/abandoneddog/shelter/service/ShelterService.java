package com.techeer.abandoneddog.shelter.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.animal.repository.PetInfoRepository;
import com.techeer.abandoneddog.shelter.Dto.Coordinate;
import com.techeer.abandoneddog.shelter.Dto.PetInfoDto;
import com.techeer.abandoneddog.shelter.Dto.ShelterInfo;
import com.techeer.abandoneddog.shelter.entity.Shelter;
import com.techeer.abandoneddog.shelter.repository.ShelterRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShelterService {
	@Value("${GET_COORDINATE}")
	private String secretKey;
	private final ShelterRepository shelterRepository;
	private final PetInfoRepository petInfoRepository;

	@Autowired
	@Qualifier("stringRedisTemplate")
	public RedisTemplate<String, String> redisTemplate;

	public ShelterService(ShelterRepository shelterRepository, PetInfoRepository petInfoRepository) {
		this.shelterRepository = shelterRepository;
		this.petInfoRepository = petInfoRepository;
	}

	public List<ShelterInfo> getAllShelterInfos() {
		List<Shelter> shelters;
		try {
			shelters = shelterRepository.findAll();
		} catch (Exception e) {
			log.error("에러", e.getMessage());
			throw new RuntimeException("런타임 에러", e);
		}

		List<ShelterInfo> shelterInfos = new ArrayList<>();

		for (Shelter shelter : shelters) {
			try {
				String address = shelter.getCareAddr();
				Coordinate coordinate = getCachedCoordinate(address, "ROAD");

				if (coordinate != null) {
					ShelterInfo shelterInfo = new ShelterInfo(shelter.getCareNm(), coordinate, shelter.getCareNm(),
						shelter.getCareTel(), shelter.getCareAddr());

					shelterInfos.add(shelterInfo);
					log.info(shelterInfo.getName(), shelterInfo.getCoordinate());
				}
			} catch (Exception e) {
				log.error("Error occurred while processing shelter: {}", e.getMessage());
				// 예외가 발생한 경우 해당 shelter를 무시하고 다음 shelter로 넘어감
			}
		}

		return shelterInfos;
	}

	public Coordinate getCachedCoordinate(String address, String searchType) {
		String cacheKey = "shelter:" + address + ":" + searchType;
		String cachedValue = redisTemplate.opsForValue().get(cacheKey);

		if (cachedValue != null) {
			return parseCoordinateFromJson(cachedValue);
		} else {
			Coordinate coordinate = getCoordinates(address, searchType);
			cacheCoordinate(cacheKey, coordinate);
			return coordinate;
		}
	}

	private void cacheCoordinate(String cacheKey, Coordinate coordinate) {
		try {
			JSONObject jsonCoordinate = new JSONObject();
			jsonCoordinate.put("latitude", coordinate.getLatitude());
			jsonCoordinate.put("longitude", coordinate.getLongitude());



			redisTemplate.opsForValue().set(cacheKey, jsonCoordinate.toJSONString());
			redisTemplate.expire(cacheKey, 10, TimeUnit.DAYS); // 캐시 만료 시간 설정 (24시간)
		} catch (Exception e) {
			log.error("Error occurred while caching coordinate: {}", e.getMessage());
			// 예외 처리
		}
	}

	private Coordinate parseCoordinateFromJson(String json) {
		try {
			JSONObject jsonObject = (JSONObject)new JSONParser().parse(json);
			double latitude = Double.parseDouble(jsonObject.get("latitude").toString());
			double longitude = Double.parseDouble(jsonObject.get("longitude").toString());
			return new Coordinate(latitude, longitude);
		} catch (Exception e) {
			log.error("Error occurred while parsing coordinate from JSON: {}", e.getMessage());
			// 예외 처리
			return null;
		}
	}

	public Coordinate getCoordinates(String address, String searchType) {
		String apikey = secretKey;
		String epsg = "epsg:4326";
		try {
			StringBuilder sb = new StringBuilder("https://api.vworld.kr/req/address");
			sb.append("?service=address");
			sb.append("&request=getCoord");
			sb.append("&format=json");
			sb.append("&crs=").append(epsg);
			sb.append("&key=").append(apikey);
			sb.append("&type=").append(searchType);
			sb.append("&address=").append(URLEncoder.encode(address, StandardCharsets.UTF_8));

			URL url = new URL(sb.toString());
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
			JSONParser jspa = new JSONParser();
			JSONObject jsob = (JSONObject)jspa.parse(reader);
			JSONObject jsrs = (JSONObject)jsob.get("response");
			JSONObject jsResult = (JSONObject)jsrs.get("result");

			if (jsResult == null) {
				if ("ROAD".equals(searchType)) {
					return getCoordinates(address, "PARCEL");
				} else if ("PARCEL".equals(searchType)) {
					return null;
				}
			}
			JSONObject jspoint = (JSONObject)jsResult.get("point");
			double x = Double.parseDouble(jspoint.get("x").toString());
			double y = Double.parseDouble(jspoint.get("y").toString());

			return new Coordinate(x, y);
		} catch (IOException | ParseException e) {
			log.error("Error occurred while getting coordinates: {}", e.getMessage());
			throw new RuntimeException("Error occurred while getting coordinates", e);
		}
	}

	public Page<PetInfoDto> getPetInfosByShelterNamePaginated(Long shelter_id, int page, int size) {
		Optional<Shelter> optionalShelter = shelterRepository.findById(shelter_id);
		if (!optionalShelter.isPresent()) {
			throw new EntityNotFoundException("Shelter not found with name: " + shelter_id);
		}

		Shelter shelter = optionalShelter.get();
		Pageable pageable = PageRequest.of(page, size);
		Page<PetInfo> petInfoPage = petInfoRepository.findByShelter(shelter, pageable);

		return petInfoPage.map(petInfo -> PetInfoDto.builder()
			.popfile(petInfo.getPopfile())
			.kindCd(petInfo.getKindCd())
			.age(petInfo.getAge())
			.sexCd(petInfo.getSexCd())
			.processState(petInfo.getProcessState())
			.desertionNo(petInfo.getDesertionNo())

			.build());
	}

}







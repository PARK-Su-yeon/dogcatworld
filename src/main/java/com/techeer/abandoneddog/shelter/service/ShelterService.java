package com.techeer.abandoneddog.shelter.service;

import com.techeer.abandoneddog.shelter.Dto.Coordinate;
import com.techeer.abandoneddog.shelter.Dto.ShelterInfo;
import com.techeer.abandoneddog.shelter.entity.Shelter;
import com.techeer.abandoneddog.shelter.repository.ShelterRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.beans.factory.annotation.Value;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ShelterService {
    @Value("${GET_COORDINATE}")
    private String secretKey;
    private final ShelterRepository shelterRepository;

    public RedisTemplate<String, String> redisTemplate;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }


     @Cacheable(value = "shelterCoordinates", key = "#address + ':' + #searchType")
    public List<ShelterInfo> getAllShelterInfos() {
        List<Shelter> shelters;
        try {
            shelters = shelterRepository.findAll();
        } catch (Exception e) {
            log.error("Error occurred while fetching shelters: {}", e.getMessage());
            throw new RuntimeException("Error occurred while fetching shelters", e);
        }

        List<ShelterInfo> shelterInfos = new ArrayList<>();

        for (Shelter shelter : shelters) {
            try {
                String address = shelter.getCareAddr();
                log.error("11111111111111111111111111 shelter: {}");
                Coordinate coordinate = getCoordinates(address, "ROAD");
                log.error("222222222222222222222222222222222 shelter: {}");
                cacheShelterCoordinates(shelter);
                log.error("333333333333333333333333333 shelter: {}");


                if (coordinate != null) {
                    ShelterInfo shelterInfo = new ShelterInfo(shelter.getCareNm(), coordinate);
                    shelterInfos.add(shelterInfo);
                }
            } catch (Exception e) {
                log.error("Error occurred while processing shelter: {}", e.getMessage());
                // 예외가 발생한 경우 해당 shelter를 무시하고 다음 shelter로 넘어감
            }
        }

        return shelterInfos;
    }

    public Coordinate getCoordinates(String address, String searchType) {
        String apikey = secretKey;
        String epsg = "epsg:4326";
        log.error("44444444444444444444444444444444444444444444 shelter: {}");
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
            JSONObject jsob = (JSONObject) jspa.parse(reader);
            JSONObject jsrs = (JSONObject) jsob.get("response");
            JSONObject jsResult = (JSONObject) jsrs.get("result");
            log.error("55555555555555555555555555555555555555 shelter: {}");
            if (jsResult == null) {
                if ("ROAD".equals(searchType)) {
                    return getCoordinates(address, "PARCEL");
                } else if ("PARCEL".equals(searchType)) {
                    return null;
                }
            }
            log.error("6666666666666666666666666666666666666666666666666 shelter: {}");
            JSONObject jspoint = (JSONObject) jsResult.get("point");
            double x = Double.parseDouble(jspoint.get("x").toString());
            double y = Double.parseDouble(jspoint.get("y").toString());

            return new Coordinate(x, y);
        } catch (IOException | ParseException e) {
            log.error("Error occurred while getting coordinates: {}", e.getMessage());
            throw new RuntimeException("Error occurred while getting coordinates", e);
        }
    }


    public void cacheShelterCoordinates(Shelter shelter) {
        String cacheKey = "shelter:" + shelter.getCareNm();
        String newCacheValue = null;
        log.error("7777777777777777777777777777777777777 shelter: {}");
        try {
            Coordinate coordinate = getCoordinates(shelter.getCareAddr(), "ROAD");
            if (coordinate != null) {
                log.error("8888888888888888888888888888888 shelter: {}");
                newCacheValue = "{\"latitude\": " + coordinate.getLatitude() + ", \"longitude\": " + coordinate.getLongitude() + "}";
            }
        } catch (Exception e) {
            log.error("Error cache while getting coordinates: {}", e.getMessage());
            // 예외 처리
        }

        if (newCacheValue != null) {
            log.error("99999999999999999999999999999999999 shelter: {}");
            redisTemplate.opsForValue().set(cacheKey, newCacheValue);
            log.error("8887878787978798 shelter: {}");
            redisTemplate.expire(cacheKey, 24, TimeUnit.HOURS); // 캐시 만료 시간 설정 (예: 24시간)
            // 로깅 추가
        }
    }
}
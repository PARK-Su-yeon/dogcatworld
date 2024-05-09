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
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class ShelterService {
    @Value("${GET_COORINATE}")
    private String secretKey;
    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

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
                Coordinate coordinate = getCoordinates(address, "ROAD");
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

            if (jsResult == null) {
                if ("ROAD".equals(searchType)) {
                    return getCoordinates(address, "PARCEL");
                } else if ("PARCEL".equals(searchType)) {
                    return null;
                }
            }

            JSONObject jspoint = (JSONObject) jsResult.get("point");
            double x = Double.parseDouble(jspoint.get("x").toString());
            double y = Double.parseDouble(jspoint.get("y").toString());

            return new Coordinate(x, y);
        } catch (IOException | ParseException e) {
            log.error("Error occurred while getting coordinates: {}", e.getMessage());
            throw new RuntimeException("Error occurred while getting coordinates", e);
        }
    }
}

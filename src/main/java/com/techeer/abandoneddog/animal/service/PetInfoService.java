package com.techeer.abandoneddog.pet_info_openapi.service;

import com.techeer.abandoneddog.animal.repository.PetInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.json.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.techeer.abandoneddog.animal.entity.PetInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PetInfoService {

    private final PetInfoRepository petInfoRepository;

    public PetInfoService(PetInfoRepository petInfoRepository) {
        this.petInfoRepository = petInfoRepository;
    }

    public void getAllAndSaveInfo() throws IOException, JSONException {
        int numOfRows = 1000; // 한 번에 가져올 결과 수, 최대 1000
        int pageNo = 1; // 시작 페이지 번호
        int totalCount = 0; // 전체 결과 수

        // 첫 번째 페이지부터 마지막 페이지까지 반복하여 데이터를 가져옴
        while (true) {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=dxWfTIaGba1Ly%2F9PBbG1r5Avm5PoIBe5QhFR2OQyQ2jWKheEoyawSM3ClbhiMZI02HHROKLQsWlR3ALnIICpVg%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(numOfRows), "UTF-8")); /*한 페이지 결과 수(1,000 이하)*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8")); /*페이지 번호*/
            urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=417000"); /*강아지*/

            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml(기본값) 또는 json*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            log.info("Response code: " + conn.getResponseCode());

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            // 전체 결과 수 갱신
            if (totalCount == 0) {
                totalCount = getTotalCount(sb.toString());
            }

            // PetInfo 엔티티에 저장
            savePetInfoFromApiResponse(sb.toString());

            // 다음 페이지로 이동
            pageNo++;

            // 마지막 페이지인지 확인
            if ((pageNo - 1) * numOfRows >= totalCount) {
                break;
            }
        }

        log.info("Total count: " + totalCount);
    }

    private int getTotalCount(String jsonResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject body = jsonObject.getJSONObject("response").getJSONObject("body");
        return body.getInt("totalCount");
    }

    private void savePetInfoFromApiResponse(String apiResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(apiResponse);
        JSONObject body = jsonObject.getJSONObject("response").getJSONObject("body");
        JSONArray itemArray = body.getJSONObject("items").getJSONArray("item");

        List<PetInfo> petInfoList = new ArrayList<>();
        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject item = itemArray.getJSONObject(i);

            PetInfo petInfo = PetInfo.builder()
                    .desertionNo(Long.valueOf(item.getString("desertionNo")))
                .filename(item.getString("filename"))
                .happenDt(item.getString("happenDt"))
                .happenPlace(item.getString("happenPlace"))
                .kindCd(item.getString("kindCd"))
                .colorCd(item.getString("colorCd"))
                .age(item.getString("age"))
                .weight(item.getString("weight"))
                .noticeNo(item.getString("noticeNo"))
                .noticeSdt(item.getString("noticeSdt"))
                .noticeEdt(item.getString("noticeEdt"))
                .popfile(item.getString("popfile"))
                .processState(item.getString("processState"))
                .sexCd(item.getString("sexCd"))
                .neuterYn(item.getString("neuterYn"))
                .specialMark(item.getString("specialMark"))
                .careNm(item.getString("careNm"))
                .careTel(item.getString("careTel"))
                .careAddr(item.getString("careAddr"))
                .orgNm(item.getString("orgNm"))
                .chargeNm(item.getString("chargeNm"))
                .officetel(item.getString("officetel"))
                .noticeComment(item.getString("noticeComment"))
                    .build();

            petInfoList.add(petInfo);
        }

        petInfoRepository.saveAll(petInfoList);
        log.info("Saved " + petInfoList.size() + " pet information to the database.");
    }
    public PetInfo getPetInfo(Long id) {
        return petInfoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found"));
    }

    public void deletePetInfo(Long id) {
        PetInfo entity = petInfoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found"));
        petInfoRepository.delete(entity);
    }

}

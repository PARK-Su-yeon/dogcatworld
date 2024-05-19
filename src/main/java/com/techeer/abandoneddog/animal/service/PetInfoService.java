package com.techeer.abandoneddog.animal.service;

import com.techeer.abandoneddog.animal.repository.PetInfoRepository;
import com.techeer.abandoneddog.shelter.entity.Shelter;
import com.techeer.abandoneddog.shelter.repository.ShelterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import org.json.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final ShelterRepository shelterRepository;

    public PetInfoService(PetInfoRepository petInfoRepository, ShelterRepository shelterRepository) {
        this.petInfoRepository = petInfoRepository;
        this.shelterRepository = shelterRepository;
    }

    @Value("${OPEN_API_SECRETKEY}")
    private String secretKey;

    public void deleteAllPetInfo() {
        petInfoRepository.deleteAll();
    }

    public void getAllAndSaveInfo(String upkind) throws IOException, JSONException {
        int numOfRows = 1000; // 한 번에 가져올 결과 수, 최대 1000
        int pageNo = 1; // 시작 페이지 번호
        int totalCount = 0; // 전체 결과 수

        // 첫 번째 페이지부터 마지막 페이지까지 반복하여 데이터를 가져옴
        while (true) {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") +secretKey ); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(numOfRows), "UTF-8")); /*한 페이지 결과 수(1,000 이하)*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8")); /*페이지 번호*/
            urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + upkind); /*강아지*/
            //urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=422400"); /*강아지*/
            log.info("upking code: " + upkind);

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
    private void savePetInfoFromApiResponse(String apiResponse) {
        try {
            JSONObject jsonObject = new JSONObject(apiResponse);
            JSONObject body = jsonObject.getJSONObject("response").getJSONObject("body");
            JSONArray itemArray = body.getJSONObject("items").getJSONArray("item");

            List<PetInfo> petInfoList = new ArrayList<>();
            List<Shelter> ShelterList = new ArrayList<>();
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject item = itemArray.getJSONObject(i);


                try {
                    String careNm=item.getString("careNm");

                    if (!shelterRepository.existsByCareNm(careNm)) {
                        Shelter shelter = Shelter.builder()
                                .careNm(careNm)
                                .careTel(item.getString("careTel"))
                                .careAddr(item.getString("careAddr"))
                                .build();

                        // Shelter 엔티티를 저장
                        shelterRepository.save(shelter);
                    }

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
//                            .careNm(item.getString("careNm"))
//                            .careTel(item.getString("careTel"))
//                            .careAddr(item.getString("careAddr"))
                            .orgNm(item.getString("orgNm"))
                            .chargeNm(item.getString("chargeNm"))
                            .officetel(item.getString("officetel"))
                            .build();


                    if (!item.isNull("noticeComment")) {
                        petInfo.setNoticeComment(item.getString("noticeComment"));
                        log.info("Saved " + petInfo.getNoticeComment());
                    }
                    else {
                        log.info("Saved null notice comment");
                    }

                    // noticeComment는 null 값이 아닌 것으로 가정


                    petInfoList.add(petInfo);
                    log.info("Saved " + petInfoList.size());

                } catch (JSONException e) {
                    log.error("Error creating PetInfo object: " + e.getMessage());
                }
            }
            petInfoRepository.saveAll(petInfoList);
     ;

            log.info("Saved " + petInfoList.size() + " pet information to the database.");
        } catch (JSONException e) {
            log.error("Error parsing JSON response: " + e.getMessage());
        }
    }
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    //@Scheduled(fixedRate = 180000)
    public void updatePetInfoDaily() {
        try {
            deleteAllPetInfo();
            log.info("delete update schedluder");

            getAllAndSaveInfo("417000");
            getAllAndSaveInfo("422400");
            log.info("Saved update schedluder");
        } catch (Exception e) {
            log.error( e.getMessage());
        }
    }


    public PetInfo getPetInfo(Long id) {
        return petInfoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found"));
    }

    public void deletePetInfo(Long id) {
        PetInfo entity = petInfoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found"));
        petInfoRepository.delete(entity);
    }
    public Page<PetInfo> getAllPetInfos(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return petInfoRepository.findAll(pageRequest);
    }
}

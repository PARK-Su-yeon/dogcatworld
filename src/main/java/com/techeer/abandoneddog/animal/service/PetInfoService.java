package com.techeer.abandoneddog.animal.service;

import com.techeer.abandoneddog.animal.PetInfoDto.PetInfoDto;
import com.techeer.abandoneddog.animal.repository.PetInfoRepository;
import com.techeer.abandoneddog.shelter.entity.Shelter;
import com.techeer.abandoneddog.shelter.repository.ShelterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
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
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@Service
@Slf4j
public class PetInfoService {

    private final PetInfoRepository petInfoRepository;
    private final ShelterRepository shelterRepository;
    private final Set<Long> existingDesertionNos = new HashSet<>();

    public PetInfoService(PetInfoRepository petInfoRepository, ShelterRepository shelterRepository) {
        this.petInfoRepository = petInfoRepository;
        this.shelterRepository = shelterRepository;
    }

    @Value("${OPEN_API_SECRETKEY}")
    private String secretKey;

    public void initializeExistingDesertionNos() {
        List<Long> desertionNos = petInfoRepository.findAllDesertionNos();
        existingDesertionNos.addAll(desertionNos);
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
            savePetInfoFromApiResponse(sb.toString(), upkind);

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

    private void savePetInfoFromApiResponse(String apiResponse, String upkind) {
        try {
            JSONObject jsonObject = new JSONObject(apiResponse);
            JSONObject body = jsonObject.getJSONObject("response").getJSONObject("body");
            JSONArray itemArray = body.getJSONObject("items").getJSONArray("item");

            List<PetInfo> petInfoList = new ArrayList<>();
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject item = itemArray.getJSONObject(i);

                try {
                    Long desertionNo = Long.parseLong(item.getString("desertionNo"));

                    // 이미 존재하는 desertionNo인지 확인
                    if (existingDesertionNos.contains(desertionNo)) {
                        continue;
                    }


                    String careNm = item.getString("careNm");

                    Shelter shelter;
                    Optional<Shelter> optionalShelter = shelterRepository.findByCareNm(careNm);
                    if (!optionalShelter.isPresent()) {
                        shelter = Shelter.builder()
                                .careNm(careNm)
                                .careTel(item.getString("careTel"))
                                .careAddr(item.getString("careAddr"))
                                .build();

                        // Shelter 엔티티를 저장
                        shelterRepository.save(shelter);
                    } else {
                        // 이미 존재하는 Shelter 객체를 불러옴
                        shelter = optionalShelter.get();
                    }

                    PetInfo petInfo = PetInfo.builder()
                            .desertionNo(desertionNo)
                            .filename(item.optString("filename", null))
                            .happenDt(item.optString("happenDt", null))
                            .happenPlace(item.optString("happenPlace", null))
                            .petType(getPetType(upkind)) // 개와 고양이를 구분
                            .kindCd(getKindCd(item.optString("kindCd", null))) // 품종만 추출
//                            .kindCd(item.optString("kindCd", null))
                            .colorCd(item.optString("colorCd", null))
                            .age(item.optString("age", null))
                            .weight(item.optString("weight", null))
                            .noticeNo(item.optString("noticeNo", null))
                            .noticeSdt(item.optString("noticeSdt", null))
                            .noticeEdt(item.optString("noticeEdt", null))
                            .popfile(item.optString("popfile", null))
                            .processState(item.optString("processState", null))
                            .sexCd(item.optString("sexCd", null))
                            .neuterYn(item.optString("neuterYn", null))
                            .specialMark(item.optString("specialMark", null))
                            .orgNm(item.optString("orgNm", null))
                            .chargeNm(item.optString("chargeNm", null)) // optString을 사용하여 필드가 없을 경우 기본값 null로 설정
                            .officetel(item.optString("officetel", null))
                            .noticeComment(item.optString("noticeComment", null))
                            .shelter(shelter)
                            .build();

                    petInfoList.add(petInfo);

                    if (!item.isNull("noticeComment")) {
                        petInfo.setNoticeComment(item.getString("noticeComment"));
                    }

                    petInfoList.add(petInfo);
                    existingDesertionNos.add(desertionNo);

                } catch (JSONException e) {
                    log.error("Error creating PetInfo object: " + e.getMessage());
                }
            }
            petInfoRepository.saveAll(petInfoList);

            log.info("Saved " + petInfoList.size() + " new pet information to the database.");
        } catch (JSONException e) {
            log.error("Error parsing JSON response: " + e.getMessage());
        } catch (Exception e) {
            log.error("General error: " + e.getMessage());
        }
    }

    private String getPetType(String upkind) {
        if ("417000".equals(upkind)) {
            return "개";
        } else if ("422400".equals(upkind)) {
            return "고양이";
        } else {
            return "기타";
        }
    }

    private String getKindCd(String kindCd) {
        if (kindCd.contains("]")) {
            return kindCd.split("]")[1].trim();
        }
        return kindCd;
    }

    // @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void updatePetInfoDaily() {
        try {
            initializeExistingDesertionNos();
            log.info("Initialized existing desertion numbers");

            getAllAndSaveInfo("417000"); //고양이
            getAllAndSaveInfo("422400"); //개
            log.info("Saved update scheduler");
        } catch (Exception e) {
            log.error(e.getMessage());
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

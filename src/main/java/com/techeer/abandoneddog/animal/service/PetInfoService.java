package com.techeer.abandoneddog.animal.service;

import com.techeer.abandoneddog.animal.PetInfoDto.PetInfoDto;
import com.techeer.abandoneddog.animal.PetInfoDto.PetInfoResponseDto;
import com.techeer.abandoneddog.animal.repository.PetInfoRepository;
import com.techeer.abandoneddog.shelter.entity.Shelter;
import com.techeer.abandoneddog.shelter.repository.ShelterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.techeer.abandoneddog.animal.entity.PetInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class PetInfoService {

    private final PetInfoRepository petInfoRepository;
    private final ShelterRepository shelterRepository;
    private final Set<Long> existingDesertionNos = new HashSet<>();
    private boolean isInitialized = false;

    @Value("${OPEN_API_SECRETKEY}")
    private String secretKey;

    public PetInfoService(PetInfoRepository petInfoRepository, ShelterRepository shelterRepository) {
        this.petInfoRepository = petInfoRepository;
        this.shelterRepository = shelterRepository;
    }

    public void initializeExistingDesertionNos() {
        List<Long> desertionNos = petInfoRepository.findAllDesertionNos();
        existingDesertionNos.addAll(desertionNos);
        isInitialized = true;
    }

    public void getAllAndSaveInfo(String upkind, String bgnde, String endde) throws IOException, JSONException {
        int numOfRows = 1000;
        int pageNo = 1;
        int totalCount = 0;

        while (true) {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + secretKey);
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(numOfRows), "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + URLEncoder.encode(upkind, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("bgnde", "UTF-8") + "=" + URLEncoder.encode(bgnde, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("endde", "UTF-8") + "=" + URLEncoder.encode(endde, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

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

            if (totalCount == 0) {
                totalCount = getTotalCount(sb.toString());
            }

            // PetInfo 엔티티에 저장
            savePetInfoFromApiResponse(sb.toString(), upkind);

            pageNo++;
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
                        shelterRepository.save(shelter);
                    } else {
                        shelter = optionalShelter.get();
                    }
                    String year = item.optString("age", null);

                    boolean isYoung = false;

                    int currentYear = Year.now().getValue();

                    if (year.contains("60일")) {
                        isYoung = true;

                    }
                    String ageStr = year.split("\\(")[0].trim();


                    int age = currentYear - Integer.parseInt(ageStr);




                   // 괄호 이전의 숫자만 추출


                    PetInfo petInfo = PetInfo.builder()
                            .desertionNo(desertionNo)
                            .filename(item.optString("filename", null))
                            .happenDt(item.optString("happenDt", null))
                            .happenPlace(item.optString("happenPlace", null))
                            .petType(getPetType(upkind)) // 개와 고양이를 구분
                            .kindCd(getKindCd(item.optString("kindCd", null))) // 품종만 추출
//                            .kindCd(item.optString("kindCd", null))
                            .colorCd(item.optString("colorCd", null))
                            .age(age)
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
                            .chargeNm(item.optString("chargeNm", null))
                            .officetel(item.optString("officetel", null))
                            .noticeComment(item.optString("noticeComment", null))
                            .shelter(shelter)
                            .isPublicApi(true)
                            .isYoung(isYoung)
                            .build();

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

    @Scheduled(cron = "0 0 0 1 1 ?") // 매년 1월 1일 자정에 실행
    public void updateAges() {
        try {
            List<PetInfo> allPets = petInfoRepository.findAll();
            for (PetInfo pet : allPets) {

                pet.setAge(pet.getAge() + 1);
            }
            petInfoRepository.saveAll(allPets);
            log.info("Updated ages for all pets");
        } catch (Exception e) {
            log.error("Error updating ages: " + e.getMessage());
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
            if (!isInitialized) {
                initializeExistingDesertionNos();
                log.info("Initialized existing desertion numbers");
            }

            LocalDate today = LocalDate.now();
            LocalDate tenDaysAgo = today.minusDays(10);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            String bgnde = tenDaysAgo.format(formatter);
            String endde = today.format(formatter);

            getAllAndSaveInfo("417000", bgnde, endde); // 강아지
            getAllAndSaveInfo("422400", bgnde, endde); // 고양이

            log.info("Saved update scheduler");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

//    public PetInfo getPetInfo(Long id) {
//        return petInfoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found"));
//    }

    public PetInfoResponseDto getPetInfoById(Long id) {
        PetInfo petInfo = petInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PetInfo not found with id: " + id));
        return PetInfoResponseDto.fromEntity(petInfo);
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

package com.techeer.abandoneddog.funeral.service;

import java.io.InputStream;
import java.util.List;
import java.io.IOException;
import java.io.FileInputStream;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.core.io.ResourceLoader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.techeer.abandoneddog.funeral.entity.Region;
import com.techeer.abandoneddog.funeral.entity.PetFuneral;
import com.techeer.abandoneddog.funeral.dto.PetFuneralResponseDto;
import com.techeer.abandoneddog.funeral.dto.PetFuneralRequestDto;
import com.techeer.abandoneddog.funeral.repository.PetFuneralRespository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetFuneralService {

    private final PetFuneralRespository petFuneralRespository;
    private final ResourceLoader resourceLoader;

    @Transactional
    public List<PetFuneralResponseDto> getPetFunerals() {

        List<PetFuneral> petFuneralList = petFuneralRespository.findAll();
        return petFuneralList.stream()
                .map(PetFuneralResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void savePetFuneral() throws IOException {
        String filePath = "classpath:funeralData/장묘업목록_20240410 복사본.xls";
        log.info("파일 경로: {}", filePath);

        Resource resource = resourceLoader.getResource(filePath);
        if (!resource.exists()) {
            throw new IllegalArgumentException("파일 경로가 잘못되었습니다.");
        }

        try (InputStream inputStream = resource.getInputStream();
             HSSFWorkbook workbook = new HSSFWorkbook(inputStream)) {

            HSSFSheet worksheet = workbook.getSheetAt(0);

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                PetFuneralRequestDto dto = new PetFuneralRequestDto();

                DataFormatter formatter = new DataFormatter();
                HSSFRow row = worksheet.getRow(i);
                String funeralName = formatter.formatCellValue(row.getCell(2));
                String phoneNum = formatter.formatCellValue(row.getCell(3));
                String address = formatter.formatCellValue(row.getCell(4));
                String homepage = formatter.formatCellValue(row.getCell(5));
                String image = formatter.formatCellValue(row.getCell(6));

                // 지역 필터링 추출
                Region region = extractRegionFromAddress(address);

                dto.setFuneralName(funeralName);
                dto.setPhoneNum(phoneNum);
                dto.setAddress(address);
                dto.setHomepage(homepage);
                dto.setRegion(region);
                dto.setImage(image);

                petFuneralRespository.save(dto.toEntity());
            }
        }
    }

    private Region extractRegionFromAddress(String address) {
        if (address.contains("서울")) {
            return Region.서울;
        } else if (address.contains("경기")) {
            return Region.경기;
        } else if (address.contains("인천")) {
            return Region.인천;
        } else if (address.contains("강원")) {
            return Region.강원;
        } else if (address.contains("세종")) {
            return Region.세종;
        } else if (address.contains("충청") || address.contains("충북") || address.contains("충남")) {
            return Region.충청;
        } else if (address.contains("전라") || address.contains("전남") || address.contains("전북")) {
            return Region.전라;
        } else if (address.contains("부산")) {
            return Region.부산;
        } else if (address.contains("경상") || address.contains("경남") || address.contains("경북")) {
            return Region.경상;
        } else if (address.contains("대구")) {
            return Region.대구;
        } else if (address.contains("울산")) {
            return Region.울산;
        }else if(address.contains("광주")){
            return Region.광주;
        }else {
            return null; // or a default value or throw an exception
        }
    }
}

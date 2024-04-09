package com.techeer.abandoneddog.funeral.service;

import com.techeer.abandoneddog.funeral.dto.PetFuneralRequestDto;
import com.techeer.abandoneddog.funeral.dto.PetFuneralResponseDto;
import com.techeer.abandoneddog.funeral.entity.PetFuneral;
import com.techeer.abandoneddog.funeral.repository.PetFuneralRespository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PetFuneralService {

    private final PetFuneralRespository petFuneralRespository;

    @Transactional
    public void savePetFuneral() throws IOException {
        String filePath = System.getenv("LOCAL_EXEL_PATH");
        FileInputStream fileInputStream = new FileInputStream(filePath);
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        HSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            PetFuneralRequestDto dto = new PetFuneralRequestDto();

            DataFormatter formatter = new DataFormatter();
            HSSFRow row = worksheet.getRow(i);

            String funeralName = formatter.formatCellValue(row.getCell(2));
            String phoneNum = formatter.formatCellValue(row.getCell(3));
            String address = formatter.formatCellValue(row.getCell(4));
            String homepage = formatter.formatCellValue(row.getCell(5));

            dto.setFuneralName(funeralName);
            dto.setPhoneNum(phoneNum);
            dto.setAddress(address);
            dto.setHomepage(homepage);

            petFuneralRespository.save(dto.toEntity());

        }
        // 필요한 작업 수행 후 리소스 정리
        workbook.close();
        fileInputStream.close();
    }


    @Transactional
    public List<PetFuneralResponseDto> getPetFunerals() throws IOException {
        savePetFuneral();
        List<PetFuneral> petFuneralList = petFuneralRespository.findAll();
        return petFuneralList.stream()
                .map(PetFuneralResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}

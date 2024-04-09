package com.techeer.abandoneddog.funeral.controller;

import com.techeer.abandoneddog.funeral.dto.PetFuneralResponseDto;
import com.techeer.abandoneddog.funeral.service.PetFuneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pet-funeral")
public class PetFuneralController {

    private final PetFuneralService petFuneralService;

    @GetMapping()
    public ResponseEntity<?> getPetFunerals() {
        try {
            List<PetFuneralResponseDto> petFuneralList = petFuneralService.getPetFunerals();
            return ResponseEntity.status(HttpStatus.OK).body(petFuneralList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펫 장례식장 조회에 실패하였습니다.");
        }
    }

}

package com.techeer.abandoneddog.pet_info.controller;

import com.techeer.abandoneddog.pet_info.dto.PetInfoRequestDto;
import com.techeer.abandoneddog.pet_info.dto.PetInfoResponseDto;
import com.techeer.abandoneddog.pet_info.entity.PetInfo;
import com.techeer.abandoneddog.pet_info.repository.PetInfoRepository;
import com.techeer.abandoneddog.pet_info.service.PetInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pet-info")
public class PetInfoController {

    private final PetInfoService petInfoService;
    private final PetInfoRepository petInfoRepository;

    @PostMapping
    public ResponseEntity<?> petInfoCreate(@RequestBody PetInfoRequestDto petInfoRequestDto) {
        try {
            Long petInfoId = petInfoService.createPetInfo(petInfoRequestDto);
            return ResponseEntity.ok().body("반려동물 정보 등록을 성공했습니다.");
//            return ResponseEntity.ok().body("반려동물 정보 등록을 성공했습니다. ID: " + petInfoId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("반려동물 정보 등록을 실패했습니다.");
        }
    }
}

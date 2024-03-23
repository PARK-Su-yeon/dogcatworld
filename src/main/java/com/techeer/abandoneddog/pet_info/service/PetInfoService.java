package com.techeer.abandoneddog.pet_info.service;

import com.techeer.abandoneddog.pet_info.dto.PetInfoRequestDto;
import com.techeer.abandoneddog.pet_info.entity.PetInfo;
import com.techeer.abandoneddog.pet_info.repository.PetInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PetInfoService {
    private final PetInfoRepository petInfoRepository;

    @Transactional
    public Long createPetInfo(PetInfoRequestDto petInfoRequestDto) {
        PetInfo petInfo = petInfoRequestDto.toEntity();
        petInfoRepository.save(petInfo);
        return petInfo.getPetInfoId();
    }
}

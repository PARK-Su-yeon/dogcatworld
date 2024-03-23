package com.techeer.abandoneddog.pet_info.dto;

import com.techeer.abandoneddog.pet_info.entity.Adoption;
import com.techeer.abandoneddog.pet_info.entity.Gender;
import com.techeer.abandoneddog.pet_info.entity.PetInfo;
import com.techeer.abandoneddog.pet_info.entity.PetType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetInfoResponseDto {
    private Long petInfoId;
    private Adoption adoption;
    private String petName;
    private PetType petType;
    private int adoptCount;
    private String breed;
    private Gender gender;
    private float weight;

    public static PetInfoResponseDto fromEntity(PetInfo petInfo) {
        return PetInfoResponseDto.builder()
                .petInfoId(petInfo.getPetInfoId())
                .adoption(petInfo.getAdoption())
                .petName(petInfo.getPetName())
                .petType(petInfo.getPetType())
                .adoptCount(petInfo.getAdoptCount())
                .breed(petInfo.getBreed())
                .gender(petInfo.getGender())
                .weight(petInfo.getWeight())
                .build();
    }
}

package com.techeer.abandoneddog.pet_info.dto;

import com.techeer.abandoneddog.pet_info.entity.Adoption;
import com.techeer.abandoneddog.pet_info.entity.Gender;
import com.techeer.abandoneddog.pet_info.entity.PetInfo;
import com.techeer.abandoneddog.pet_info.entity.PetType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetInfoRequestDto {
    private Adoption adoption;
    private String petName;
    private PetType petType;
    private int adoptCount;
    private String breed;
    private Gender gender;
    private float weight;

    public PetInfo toEntity() {
        return PetInfo.builder()
                .adoption(adoption)
                .petName(petName)
                .petType(petType)
                .adoptCount(adoptCount)
                .breed(breed)
                .gender(gender)
                .weight(weight)
                .build();
    }
}

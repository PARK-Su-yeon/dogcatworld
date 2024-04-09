package com.techeer.abandoneddog.funeral.dto;

import com.techeer.abandoneddog.funeral.entity.PetFuneral;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetFuneralResponseDto {
    private Long id;

    private String funeralName;

    private String address;

    private String phoneNum;

    private String homepage;

    public static PetFuneralResponseDto fromEntity(PetFuneral petFuneral) {
        return new PetFuneralResponseDto(
                petFuneral.getId(),
                petFuneral.getFuneralName(),
                petFuneral.getAddress(),
                petFuneral.getPhoneNum(),
                petFuneral.getHomepage()
        );
    }
}

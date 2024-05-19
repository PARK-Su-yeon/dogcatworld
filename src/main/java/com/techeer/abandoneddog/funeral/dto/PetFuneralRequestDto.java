package com.techeer.abandoneddog.funeral.dto;

import com.techeer.abandoneddog.funeral.entity.PetFuneral;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PetFuneralRequestDto {

    private String funeralName;

    private String address;

    private String phoneNum;

    private String homepage;

    public PetFuneral toEntity() {
        return PetFuneral.builder()
                .funeralName(funeralName)
                .phoneNum(phoneNum)
                .homepage(homepage)
                .address(address)
                .build();
    }
}

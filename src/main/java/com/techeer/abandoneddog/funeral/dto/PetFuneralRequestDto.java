package com.techeer.abandoneddog.funeral.dto;

import com.techeer.abandoneddog.funeral.entity.PetFuneral;
import com.techeer.abandoneddog.funeral.entity.Region;
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

    private Region region;

    private String image;

    public PetFuneral toEntity() {
        return PetFuneral.builder()
                .funeralName(funeralName)
                .phoneNum(phoneNum)
                .homepage(homepage)
                .address(address)
                .region(region)
                .image(image)
                .build();
    }
}

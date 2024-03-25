package com.techeer.abandoneddog.animal.Dto;

import com.techeer.abandoneddog.animal.entity.Gender;
import com.techeer.abandoneddog.animal.entity.PetType;
import jakarta.persistence.Column;

import lombok.Getter;

@Getter
public class PetInfoRequestDto {

    private int shelterId;
    private String petName;
    private PetType petType;
    private String breed;
    private Gender gender;
    private Float weight;

}

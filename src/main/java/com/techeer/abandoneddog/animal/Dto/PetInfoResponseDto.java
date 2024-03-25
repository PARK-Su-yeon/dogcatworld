package com.techeer.abandoneddog.animal.Dto;


import com.techeer.abandoneddog.animal.entity.Gender;
import com.techeer.abandoneddog.animal.entity.PetType;
import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Builder
public class PetInfoResponseDto {

    private int shelterId;
    private String petName;
    private PetType petType;
    private String breed;
    private Gender gender;
    private Float weight;





}


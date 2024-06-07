package com.techeer.abandoneddog.animal.PetInfoDto;

import lombok.Getter;

@Getter
public class PetInfoDto {
    private Long id;
    private Long desertionNo;
    private String filename;
    private String popfile;
    private String processState;
    private String age;
    private String weight;
    private String sexCd;
    private String kindCd;
    private String petType;

    public PetInfoDto(Long id, Long desertionNo, String filename, String popfile, String processState, String age, String weight, String sexCd, String kindCd, String petType) {
        this.id = id;
        this.desertionNo = desertionNo;
        this.filename = filename;
        this.popfile = popfile;
        this.processState = processState;
        this.age = age;
        this.weight = weight;
        this.sexCd = sexCd;
        this.kindCd = kindCd;
        this.petType = petType;
    }
}

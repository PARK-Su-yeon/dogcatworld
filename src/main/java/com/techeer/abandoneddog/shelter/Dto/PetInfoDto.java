package com.techeer.abandoneddog.shelter.Dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ContentDisposition;
@Getter
@Builder
public class PetInfoDto {
    private final String kindCd;
    private String filename;

    private String age;
    private String sexCd;
    private String processState;

    private Long desertionNo;
    public PetInfoDto(String filename, String kindCd, String age, String sexCd, String processState, Long desertionNo) {
        this.filename = filename;
        this.kindCd = kindCd;
        this.age = age;
        this.sexCd = sexCd;
        this.processState = processState;
        this.desertionNo = desertionNo;
    }



}

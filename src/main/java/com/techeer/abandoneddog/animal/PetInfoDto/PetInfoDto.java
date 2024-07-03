package com.techeer.abandoneddog.animal.PetInfoDto;

import com.techeer.abandoneddog.image.entity.Image;
import lombok.Getter;

import java.util.List;

@Getter
public class PetInfoDto {
    private Long id;
    private Long desertionNo;
    private String filename;
    private String popfile;
    private String processState;
    private int age;
    private String weight;
    private String sexCd;
    private String kindCd;
    private String petType;
    private boolean isPublicApi;
    private List<String> images;


    public PetInfoDto(Long id, Long desertionNo, String filename, String popfile, String processState, int age, String weight, String sexCd, String kindCd, String petType, boolean isPublicApi, List<String> images) {

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
        this.isPublicApi = isPublicApi;
        this.images=images;
    }
}

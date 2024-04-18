package com.techeer.abandoneddog.animal.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PetInfo {
    //@Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private Long id;
    @Id
    private Long desertionNo;
    private String filename;
    private String happenDt; //발견 날짜
    private String happenPlace; //발견 장소
    private String kindCd; //품종 //kindCd": "[개] 믹스견",
    private String colorCd;
    private String age;
    private String weight;
    private String noticeNo;
    private String noticeSdt;
    private String noticeEdt;
    private String popfile;
    private String processState;
    private String sexCd;
    private String neuterYn;
    private String specialMark;
    private String careNm;
    private String careTel;
    private String careAddr;
    private String orgNm;
    private String chargeNm;
    private String officetel;
    private String noticeComment;


}



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
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
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

    public void updateFrom(PetInfo other) {
        if (other.filename != null) this.filename = other.filename;
        if (other.happenDt != null) this.happenDt = other.happenDt;
        if (other.happenPlace != null) this.happenPlace = other.happenPlace;
        if (other.kindCd != null) this.kindCd = other.kindCd;
        if (other.colorCd != null) this.colorCd = other.colorCd;
        if (other.age != null) this.age = other.age;
        if (other.weight != null) this.weight = other.weight;
        if (other.noticeNo != null) this.noticeNo = other.noticeNo;
        if (other.noticeSdt != null) this.noticeSdt = other.noticeSdt;
        if (other.noticeEdt != null) this.noticeEdt = other.noticeEdt;
        if (other.popfile != null) this.popfile = other.popfile;
        if (other.processState != null) this.processState = other.processState;
        if (other.sexCd != null) this.sexCd = other.sexCd;
        if (other.neuterYn != null) this.neuterYn = other.neuterYn;
        if (other.specialMark != null) this.specialMark = other.specialMark;
        if (other.careNm != null) this.careNm = other.careNm;
        if (other.careTel != null) this.careTel = other.careTel;
        if (other.careAddr != null) this.careAddr = other.careAddr;
        if (other.orgNm != null) this.orgNm = other.orgNm;
        if (other.chargeNm != null) this.chargeNm = other.chargeNm;
        if (other.officetel != null) this.officetel = other.officetel;
        if (other.noticeComment != null) this.noticeComment = other.noticeComment;
    }

}


//    public void update(PetInfoRequestDto dto) {
//
//         shelterId=dto.getShelterId();
//         petName=dto.getPetName();
//         petType=dto.getPetType();
//         breed=dto.getBreed();
//        gender=dto.getGender();
//        weight=dto.getWeight();
//
//    }


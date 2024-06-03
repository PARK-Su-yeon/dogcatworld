package com.techeer.abandoneddog.animal.entity;

import com.techeer.abandoneddog.global.entity.BaseEntity;
import com.techeer.abandoneddog.pet_board.dto.PetBoardRequestDto;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.shelter.entity.Shelter;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class PetInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nullable
    private Long desertionNo;
    @Nullable
    private String filename;
    @Nullable
    private String happenDt; // 발견 날짜
    @Nullable
    private String happenPlace; // 발견 장소
    @Nullable
    private String kindCd; // 품종
    @Nullable
    private String colorCd;
    @Nullable
    private String age;
    @Nullable
    private String weight;
    @Nullable
    private String noticeNo;
    @Nullable
    private String noticeSdt;
    @Nullable
    private String noticeEdt;
    @Nullable
    private String popfile;
    @Nullable
    private String processState;
    @Nullable
    private String sexCd;
    @Nullable
    private String neuterYn;
    @Nullable
    private String specialMark;

    private boolean isPublicApi= true;

    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = true)
    private Shelter shelter;

    @OneToOne(mappedBy = "petInfo", cascade = CascadeType.REMOVE)
    private PetBoard petBoard;

    @Nullable
    private String orgNm;
    @Nullable
    private String chargeNm;
    @Nullable
    private String officetel;
    @Nullable
    private String noticeComment;




    public void update(PetInfo petInfo) {
        this.desertionNo = petInfo.getDesertionNo();
        this.isPublicApi=false;
        this.filename = petInfo.getFilename();
        this.happenDt = petInfo.getHappenDt();
        this.happenPlace = petInfo.getHappenPlace();
        this.kindCd = petInfo.getKindCd();
        this.colorCd = petInfo.getColorCd();
        this.age = petInfo.getAge();
        this.weight = petInfo.getWeight();
        this.noticeNo = petInfo.getNoticeNo();
        this.noticeSdt = petInfo.getNoticeSdt();
        this.noticeEdt = petInfo.getNoticeEdt();
        this.popfile = petInfo.getPopfile();
        this.processState = petInfo.getProcessState();
        this.sexCd = petInfo.getSexCd();
        this.neuterYn = petInfo.getNeuterYn();
        this.specialMark = petInfo.getSpecialMark();
        this.shelter = petInfo.getShelter();
        this.orgNm = petInfo.getOrgNm();
        this.chargeNm = petInfo.getChargeNm();
        this.officetel = petInfo.getOfficetel();
        this.noticeComment = petInfo.getNoticeComment();
    }
}



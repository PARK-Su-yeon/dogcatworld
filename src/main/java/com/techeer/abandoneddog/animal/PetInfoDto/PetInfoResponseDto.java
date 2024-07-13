package com.techeer.abandoneddog.animal.PetInfoDto;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.image.entity.Image;
import com.techeer.abandoneddog.shelter.Dto.ShelterInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PetInfoResponseDto {
    private Long id;
    private Long desertionNo;
    private String filename;
    private String happenDt;
    private String happenPlace;
    private String petType;
    private String kindCd;
    private String colorCd;
    private int age;
    private String weight;
    private String noticeNo;
    private String noticeSdt;
    private String noticeEdt;
    private String popfile;
    private String processState;
    private String sexCd;
    private String neuterYn;
    private String specialMark;
    private boolean isPublicApi;
    private ShelterInfo shelter;
    private boolean isYoung;
    private List<String> images;

    public static PetInfoResponseDto fromEntity(PetInfo petInfo) {
        ShelterInfo shelterInfo = new ShelterInfo(
                petInfo.getShelter().getCareNm(),
                null,
                petInfo.getShelter().getCareNm(),
                petInfo.getShelter().getCareTel(),
                petInfo.getShelter().getCareAddr()
        );




        return PetInfoResponseDto.builder()
                .id(petInfo.getId())
                .desertionNo(petInfo.getDesertionNo())
                .filename(petInfo.getFilename())
                .happenDt(petInfo.getHappenDt())
                .happenPlace(petInfo.getHappenPlace())
                .petType(petInfo.getPetType())
                .kindCd(petInfo.getKindCd())
                .colorCd(petInfo.getColorCd())
                .age(petInfo.getAge())
                .weight(petInfo.getWeight())
                .noticeNo(petInfo.getNoticeNo())
                .noticeSdt(petInfo.getNoticeSdt())
                .noticeEdt(petInfo.getNoticeEdt())
                .popfile(petInfo.getPopfile())
                .processState(petInfo.getProcessState())
                .sexCd(petInfo.getSexCd())
                .neuterYn(petInfo.getNeuterYn())
                .specialMark(petInfo.getSpecialMark())
                .isPublicApi(petInfo.isPublicApi())
                .shelter(shelterInfo)
                .isYoung(petInfo.isYoung())
                .images(petInfo.getImages().stream()
                        .map(Image::getUrl)
                        .collect(Collectors.toList()))
                .build();
    }
}

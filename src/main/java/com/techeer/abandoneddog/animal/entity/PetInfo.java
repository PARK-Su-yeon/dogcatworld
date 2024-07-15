package com.techeer.abandoneddog.animal.entity;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.techeer.abandoneddog.global.entity.BaseEntity;
import com.techeer.abandoneddog.image.entity.Image;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.shelter.entity.Shelter;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@SQLDelete(sql = "UPDATE pet_info SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Table(name = "pet_info")
public class PetInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
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
	private String petType; // 개 or 고양이인지

	@Nullable
	private String kindCd; // 품종

	@Nullable
	private String colorCd;

	@Nullable
	private int age;

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

	private boolean isPublicApi = false;

	@ManyToOne
	@JoinColumn(name = "shelter_id", nullable = true)
	private Shelter shelter;

	@OneToOne(mappedBy = "petInfo")
	private PetBoard petBoard;

	@Nullable
	private String orgNm;

	@Nullable
	private String chargeNm;

	@Nullable
	private String officetel;

	@Nullable
	private String noticeComment;

	private boolean isYoung = false;

	@Column(name = "pet_board_stored")
	private boolean petBoardStored = false;

	@OneToMany(mappedBy = "petInfo", cascade = CascadeType.ALL)
	private List<Image> images;

	public void update(PetInfo petInfo) {
		this.desertionNo = petInfo.getDesertionNo();
		this.isPublicApi = false;
		this.filename = petInfo.getFilename();
		this.happenDt = petInfo.getHappenDt();
		this.happenPlace = petInfo.getHappenPlace();
		this.petType = petInfo.getPetType();
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
		this.petBoardStored = true;

	}

	public void updateImages(List<Image> newImageUrls) {
		this.images = newImageUrls;
	}

	public void updatePopfile(String popfile) {
		this.popfile = popfile;
	}

}



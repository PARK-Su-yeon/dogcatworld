package com.techeer.abandoneddog.shelter.entity;

import java.util.List;

import com.techeer.abandoneddog.animal.entity.PetInfo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Shelter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 다른 속성들 생략

	@OneToMany(mappedBy = "shelter")
	private List<PetInfo> petInfoList; // PetInfo 엔티티와의 관계

	private String careNm;
	private String careTel;
	private String careAddr;
}
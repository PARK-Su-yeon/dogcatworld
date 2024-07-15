package com.techeer.abandoneddog.shelter.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetInfoDto {
	private final String kindCd;
	private String popfile;

	private int age;
	private String sexCd;
	private String processState;

	private Long desertionNo;

	public PetInfoDto(String popfile, String kindCd, int age, String sexCd, String processState, Long desertionNo) {
		this.popfile = popfile;
		this.kindCd = kindCd;
		this.age = age;
		this.sexCd = sexCd;
		this.processState = processState;
		this.desertionNo = desertionNo;
	}

}

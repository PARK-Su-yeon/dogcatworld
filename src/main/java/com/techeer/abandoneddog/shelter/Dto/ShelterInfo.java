package com.techeer.abandoneddog.shelter.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShelterInfo {
	private String name;
	private Coordinate coordinate;
	private String careNm;
	private String careTel;
	private String careAddr;

	//    public ShelterInfo(String name, Coordinate coordinate) {
	//        this.name = name;
	//        this.coordinate = coordinate;
	//    }
	public ShelterInfo(String name, Coordinate coordinate, String careNm, String careTel, String careAddr) {
		this.name = name;
		this.coordinate = coordinate;
		this.careNm = careNm;
		this.careTel = careTel;
		this.careAddr = careAddr;
	}

}

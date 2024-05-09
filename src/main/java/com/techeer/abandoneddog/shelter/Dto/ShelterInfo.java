package com.techeer.abandoneddog.shelter.Dto;

import lombok.Getter;

@Getter
public class ShelterInfo {
    private String name;
    private Coordinate coordinate;
    public ShelterInfo(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

}

package com.techeer.abandoneddog.shelter.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShelterInfo {
    private String name;
    private Coordinate coordinate;
    public ShelterInfo(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

}

package com.techeer.abandoneddog.shelter.Dto;

import lombok.Getter;

@Getter
public class Coordinate {
    private double latitude; // 위도
    private double longitude; // 경도

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
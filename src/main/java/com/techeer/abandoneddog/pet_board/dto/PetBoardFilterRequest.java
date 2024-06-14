package com.techeer.abandoneddog.pet_board.dto;

import com.techeer.abandoneddog.pet_board.entity.Status;

import java.util.List;

public class PetBoardFilterRequest {
    private int page = 0;
    private int size = 12;
    private String direction = "asc";
    private String kind;
    private Status adoptionStatus;  // Enum for adoption status
    private Integer minAge;
    private Integer maxAge;
    private String title;

    // Getters and Setters
}
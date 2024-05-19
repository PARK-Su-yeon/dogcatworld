package com.techeer.abandoneddog.shelter.Controller;

import com.techeer.abandoneddog.shelter.Dto.ShelterInfo;
import com.techeer.abandoneddog.shelter.service.ShelterService;
import com.techeer.abandoneddog.users.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/")
@RestController
@Slf4j
public class ShelterController {

    private final ShelterService shelterService;


    public ShelterController( ShelterService shelterService ) {
        this.shelterService = shelterService;
    }


    @GetMapping("/shelter_address")
     public List<ShelterInfo> getShelterAddress() {
        try {
            List<ShelterInfo> shelters = shelterService.getAllShelterInfos();
            return shelters;


        } catch (Exception e) {
           return null;
        }
    }


}

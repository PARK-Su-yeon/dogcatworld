//package com.techeer.abandoneddog.pet_info_openapi.controller;
//
//import com.techeer.abandoneddog.pet_info_openapi.service.PetInfoService;
//import com.techeer.abandoneddog.users.dto.ResultDto;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//
//@RestController
//@RequestMapping("/api/pet-info")
//@Slf4j
//public class OpenApiController
//{
//
//    private final PetInfoService petInfoService;
//
//
//    public OpenApiController(PetInfoService petInfoService) {
//        this.petInfoService = petInfoService;
//    }
//
//    @GetMapping("/update")
//    public ResponseEntity<String> updatePetInfo() {
//        try {
//            petInfoService.getAllAndSaveInfo();
//            return ResponseEntity.ok("Pet information updated successfully.");
//        } catch (Exception e) {
//            log.error("Error updating pet information: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update pet information.");
//        }
//    }
//}
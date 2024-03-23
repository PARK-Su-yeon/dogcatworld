package com.techeer.abandoneddog.pet_board.controller;

import com.techeer.abandoneddog.pet_board.dto.PetBoardRequestDto;
import com.techeer.abandoneddog.pet_board.service.PetBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pet-board")
public class PetBoardController {
    private final PetBoardService petBoardService;

    @PostMapping
    public ResponseEntity<?> createPetBoard(@RequestBody PetBoardRequestDto petBoardRequestDto) {
        try {
            Long petBoardId = petBoardService.createPetBoard(petBoardRequestDto);
            return ResponseEntity.ok().body("게시물 작성에 성공하였습니다.");
//            return ResponseEntity.ok().body("게시물 작성에 성공. ID: " + petBoardId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시물 작성에 실패하였습니다.");
        }
    }
}

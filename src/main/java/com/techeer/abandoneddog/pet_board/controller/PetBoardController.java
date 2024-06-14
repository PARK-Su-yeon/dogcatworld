package com.techeer.abandoneddog.pet_board.controller;

import com.techeer.abandoneddog.pet_board.dto.PetBoardRequestDto;
import com.techeer.abandoneddog.pet_board.dto.PetBoardResponseDto;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.pet_board.entity.Status;
import com.techeer.abandoneddog.pet_board.service.PetBoardService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/pet_board")
public class PetBoardController {
    private final PetBoardService petBoardService;

    @PostMapping
    public ResponseEntity<?> createPetBoard(@RequestBody PetBoardRequestDto petBoardRequestDto) {
        try {
            Long petBoardId = petBoardService.createPetBoard(petBoardRequestDto);

            return ResponseEntity.ok().body("게시물 작성에 성공하였습니다.");
//            return ResponseEntity.ok().body("게시물 작성에 성공. ID: " + petBoardId);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시물 작성에 실패하였습니다.");
        }
    }

    @PutMapping("/{petBoardId}")
    public ResponseEntity<?> updatePetBoard(@PathVariable("petBoardId") Long petBoardId, @RequestBody PetBoardRequestDto requestDto) {
        try {
            PetBoardResponseDto responseDto = petBoardService.updatePetBoard(petBoardId, requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("반려동물 게시판 글 업데이트에 실패했습니다.");
        }
    }

    @GetMapping("/{petBoardId}")
    public ResponseEntity<?> getPetBoard(@PathVariable("petBoardId") Long petBoardId) {
        try {
            PetBoardResponseDto responseDto = petBoardService.getPetBoard(petBoardId);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시글 조회에 실패하였습니다.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getPetBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by("petBoardId").descending() : Sort.by("petBoardId").ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<PetBoardResponseDto> petBoardPage = petBoardService.getPetBoards(pageable);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "입양/분양 공고 리스트 조회 성공");
            response.put("result", petBoardPage.getContent());
            response.put("totalPages", petBoardPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving pet boards", e);
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("message", "입양/분양 공고 리스트 조회에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/list/type/{petType}")
    public ResponseEntity<?> getPetBoardsByPetType(
            @PathVariable String petType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by("petBoardId").descending() : Sort.by("petBoardId").ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<PetBoardResponseDto> petBoardPage = petBoardService.getPetBoardsByPetType(petType, pageable);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "입양/분양 공고 리스트 조회 성공");
            response.put("result", petBoardPage.getContent());
            response.put("totalPages", petBoardPage.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving pet boards by pet type", e);
            Map<String, String> errorResponse = new LinkedHashMap<>();
            errorResponse.put("message", "입양/분양 공고 리스트 조회에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PetBoardResponseDto>> searchPetBoards(
            @RequestParam(value = "categories", required = false) String categories,
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "minAge", required = false) int minAge,
            @RequestParam(value = "maxAge", required = false) int maxAge,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<PetBoardResponseDto> result = petBoardService.searchPetBoards(categories, status, minAge, maxAge, title, page, size);
        return ResponseEntity.ok(result);
    }

    // 연도 추출 메서드


    @DeleteMapping("/{petBoardId}")
    public ResponseEntity<?> deletePetBoard(@PathVariable("petBoardId") Long petBoardId) {
        try {
            petBoardService.deletePetBoard(petBoardId);
            return ResponseEntity.ok().body("게시물 삭제에 성공하였습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 삭제에 실패하였습니다.");
        }
    }
}

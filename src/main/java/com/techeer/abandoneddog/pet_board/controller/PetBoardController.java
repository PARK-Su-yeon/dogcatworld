package com.techeer.abandoneddog.pet_board.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techeer.abandoneddog.image.dto.ImageResizingDto;
import com.techeer.abandoneddog.pet_board.dto.PetBoardDetailResponseDto;
import com.techeer.abandoneddog.pet_board.dto.PetBoardRequestDto;
import com.techeer.abandoneddog.pet_board.dto.PetBoardResponseDto;
import com.techeer.abandoneddog.pet_board.entity.Status;
import com.techeer.abandoneddog.pet_board.service.PetBoardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/pet_board")
public class PetBoardController {
	private final PetBoardService petBoardService;

	@Autowired
	public PetBoardController(PetBoardService petBoardService) {
		this.petBoardService = petBoardService;
	}

	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "게시물 생성", description = "게시물을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "게시물 작성 성공"),
		@ApiResponse(responseCode = "400", description = "게시물 작성 실패")
	})
	public ResponseEntity<String> createPetBoard(
		@RequestPart(name = "petBoardRequestDto") PetBoardRequestDto petBoardRequestDto,
		@RequestPart(name = "mainImage") MultipartFile mainImage,
		@RequestPart(name = "images") List<MultipartFile> images) {
		try {
			log.info("Received DTO: {}", petBoardRequestDto);
			log.info("Received main image: {}", mainImage.getOriginalFilename());
			log.info("Received additional images: {}",
				images.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.joining(", ")));

			// 요청 파라미터 검증
			if (petBoardRequestDto == null) {
				throw new IllegalArgumentException("게시물 정보가 누락되었습니다.");
			}
			if (mainImage == null || mainImage.isEmpty()) {
				throw new IllegalArgumentException("메인 이미지가 누락되었습니다.");
			}
			if (images == null || images.isEmpty()) {
				throw new IllegalArgumentException("이미지 리스트가 누락되었습니다.");
			}

			long petBoardId = petBoardService.createPetBoard(petBoardRequestDto, mainImage, images);
			return ResponseEntity.ok().body("게시물 작성에 성공하였습니다.");
		} catch (IllegalArgumentException e) {
			log.error("입력 값 검증 실패: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입력 값이 잘못되었습니다: " + e.getMessage());
		} catch (Exception e) {
			log.error("게시물 작성 중 예외 발생: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시물 작성에 실패하였습니다.");
		}
	}

	@PutMapping(value = "/{petBoardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "게시물 수정", description = "게시물을 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "게시물 수정 성공"),
		@ApiResponse(responseCode = "400", description = "게시물 수정 실패")
	})
	public ResponseEntity<?> updatePetBoard(@PathVariable("petBoardId") Long petBoardId,
		@RequestPart(name = "petBoardRequestDto") @Parameter(description = "게시물 정보", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) PetBoardRequestDto petBoardRequestDto,
		@RequestPart(name = "mainImage", required = true) @Parameter(description = "메인 이미지", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) MultipartFile mainImage,
		@RequestPart(name = "images", required = true) @Parameter(description = "추가 이미지 목록", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) List<MultipartFile> images) {
		try {
			PetBoardResponseDto responseDto = petBoardService.updatePetBoard(petBoardId, petBoardRequestDto, mainImage,
				images);
			return ResponseEntity.ok().body(responseDto);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("반려동물 게시판 글 업데이트에 실패했습니다.");
		}
	}

	@GetMapping("/{petBoardId}")
	public ResponseEntity<?> getPetBoard(@PathVariable("petBoardId") Long petBoardId,
		@RequestParam(name = "userId", defaultValue = "-1") Long userId) {

		try {
			PetBoardDetailResponseDto responseDto = petBoardService.getPetBoard(petBoardId, userId);
			return ResponseEntity.ok(responseDto);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시글 조회에 실패하였습니다.");
		}
	}

	@GetMapping("/list")
	public ResponseEntity<?> getPetBoards(
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "12") int size,
		@RequestParam(value = "direction", defaultValue = "desc") String direction) {

		try {
			//            Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by("petBoardId").descending() : Sort.by("petBoardId").ascending();
			Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by("createdAt").descending() :
				Sort.by("createdAt").ascending();
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
		@PathVariable("petType") String petType,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "12") int size,
		@RequestParam(value = "direction", defaultValue = "desc") String direction) {

		try {
			Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by("createdAt").descending() :
				Sort.by("createdAt").ascending();
			//            Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by("createdAt").descending() : Sort.by("createdAt").ascending();
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
		@RequestParam(value = "minAge", required = false) Integer minAge,
		@RequestParam(value = "maxAge", required = false) Integer maxAge,
		@RequestParam(value = "title", required = false) String title,
		@RequestParam(value = "isYoung", required = false) boolean isYoung,
		@RequestParam(value = "page", defaultValue = "0") Integer page,
		@RequestParam(value = "size", defaultValue = "10") Integer size) {

		Page<PetBoardResponseDto> result = petBoardService.searchPetBoards(categories, status, minAge, maxAge, title,
			isYoung, page, size);
		return ResponseEntity.ok(result);
	}

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

	@GetMapping("/myPetBoard/{userId}")
	public ResponseEntity<?> getMyPetBoard(@PathVariable("userId") Long userId) {
		try {
			Pageable pageable = PageRequest.of(0, 12, Sort.by("petBoardId").descending());
			Page<PetBoardResponseDto> petBoardPage = petBoardService.getMyPetBoard(userId, pageable);
			return ResponseEntity.ok().body(petBoardPage.getContent());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시글 조회에 실패하였습니다.");
		}
	}

	@PostMapping("/update-image")
	public ResponseEntity<?> updateResizingImg(@RequestBody ImageResizingDto dto) {
		try {
			petBoardService.updateResizingImg(dto);
			return ResponseEntity.ok().body("이미지 리사이징 저장 성공");
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미지 리사이징 저장에 실패하였습니다.");
		}
	}
}

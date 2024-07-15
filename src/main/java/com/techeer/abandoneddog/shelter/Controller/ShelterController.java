package com.techeer.abandoneddog.shelter.Controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techeer.abandoneddog.shelter.Dto.PetInfoDto;
import com.techeer.abandoneddog.shelter.Dto.ShelterInfo;
import com.techeer.abandoneddog.shelter.service.ShelterService;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/v1/")
@RestController
@Slf4j
public class ShelterController {

	private final ShelterService shelterService;

	public ShelterController(ShelterService shelterService) {
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

	@GetMapping("/shelter/{shelter_id}")
	public ResponseEntity<?> getPetInfosByShelterNamePaginated(
		@PathVariable("shelter_id") Long shelter_id,
		@RequestParam(name = "page", required = false, defaultValue = "0") int page,
		@RequestParam(name = "size", required = false, defaultValue = "12") int size) {
		try {
			Page<PetInfoDto> petInfosPage = shelterService.getPetInfosByShelterNamePaginated(shelter_id, page, size);
			return ResponseEntity.ok().body(petInfosPage);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("펫 정보를 가져오는 데 실패하였습니다.");
		}
	}

}

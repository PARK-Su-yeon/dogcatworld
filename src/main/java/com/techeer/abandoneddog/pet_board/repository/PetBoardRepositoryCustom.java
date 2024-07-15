package com.techeer.abandoneddog.pet_board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.pet_board.entity.Status;

public interface PetBoardRepositoryCustom {

	Page<PetBoard> searchPetBoards(String categories, Status status, Integer minAge, Integer maxAge, String title,
		Boolean isYoung, Pageable pageable);
}
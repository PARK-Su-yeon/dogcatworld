package com.techeer.abandoneddog.pet_board.repository;

import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetBoardRepository extends JpaRepository<PetBoard, Long> {
}

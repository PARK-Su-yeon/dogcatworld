package com.techeer.abandoneddog.pet_board.repository;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.pet_board.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PetBoardRepository extends JpaRepository<PetBoard, Long> {
    Page<PetBoard> findByPetInfoPetTypeAndStatus(String petType, Status status, Pageable pageable);

    // List<PetInfo> findByCreatedAtBefore(LocalDateTime tenDaysAgo);

 //   void deleteAll(List<PetInfo> oldPetInfos);
}

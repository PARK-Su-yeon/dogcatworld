package com.techeer.abandoneddog.pet_board.repository;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.pet_board.dto.PetBoardFilterRequest;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.pet_board.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface PetBoardRepository extends JpaRepository<PetBoard, Long> {
    Page<PetBoard> findByPetInfoPetTypeAndStatus(String petType, Status status, Pageable pageable);


    @Query("SELECT pb FROM PetBoard pb JOIN pb.petInfo pi WHERE " +
            "(:#{#dto.categories} IS NULL OR pi.kindCd IN :#{#dto.categories}) AND " +
            "(:#{#dto.isYoung} IS NULL OR pi.isYoung = :#{#dto.isYoung}) AND " +
            "(:#{#dto.status} IS NULL OR pb.status = :#{#dto.status}) AND " +
            "(:#{#dto.minYear} IS NULL OR pi.age >= :#{#dto.minYear}) AND " +
            "(:#{#dto.maxYear} IS NULL OR pi.age <= :#{#dto.maxYear}) AND " +
            "(:#{#dto.title} IS NULL OR pb.title LIKE %:#{#dto.title}%)")
    Page<PetBoard> searchPetBoards(@Param("dto") PetBoardFilterRequest dto, Pageable pageable);
    }

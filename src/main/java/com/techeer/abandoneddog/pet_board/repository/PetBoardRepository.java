package com.techeer.abandoneddog.pet_board.repository;

import com.techeer.abandoneddog.animal.entity.PetInfo;
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
                "(:categories IS NULL OR pi.kindCd IN :categories) AND " +
                "(:status IS NULL OR pb.status = :status) AND " +
                "(:minYear IS NULL OR  pi.age >= :minYear) AND " +
                "(:maxYear IS NULL OR  pi.age <= :maxYear) AND " +
                "(:title IS NULL OR pb.title LIKE %:title%)")
        Page<PetBoard> searchPetBoards(@Param("categories") String categories,
                                       @Param("status") Status status,
                                       @Param("minYear") int minYear,
                                       @Param("maxYear") int maxYear,
                                       @Param("title") String title,
                                       Pageable pageable);
    }

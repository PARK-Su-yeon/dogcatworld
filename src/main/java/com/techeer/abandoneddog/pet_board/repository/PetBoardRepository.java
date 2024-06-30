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

    Page<PetBoard> findPetBoardByUsersId(Long userId, Pageable pageable);

    // List<PetInfo> findByCreatedAtBefore(LocalDateTime tenDaysAgo);



    @Query("SELECT pb FROM PetBoard pb JOIN pb.petInfo pi WHERE " +
            "(:categories IS NULL OR pi.kindCd IN :categories) AND " +
            "(:status IS NULL OR pb.status = :status) AND " +
            "(:minAge IS NULL OR  pi.age >= :minAge) AND " +
            "(:maxAge IS NULL OR  pi.age <= :maxAge) AND " +
            "(:isYoung IS NULL OR  pi.isYoung =:isYoung) AND " +
            "(:title IS NULL OR pb.title LIKE %:title%)")
    Page<PetBoard> searchPetBoards(@Param("categories") String categories,
                                   @Param("status") Status status,
                                   @Param("minAge") Integer minAge,
                                   @Param("maxAge") Integer maxAge,
                                   @Param("title") String title,
                                   @Param("isYoung") boolean isYoung,

                                   Pageable pageable);
    }

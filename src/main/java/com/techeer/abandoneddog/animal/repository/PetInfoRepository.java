package com.techeer.abandoneddog.animal.repository;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PetInfoRepository extends JpaRepository<PetInfo,Long> {
    PetInfo findByDesertionNo(Long desertionNo);
    Page<PetInfo> findAll(Pageable pageable);

}
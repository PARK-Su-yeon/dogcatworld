package com.techeer.abandoneddog.animal.repository;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface PetInfoRepository extends JpaRepository<PetInfo, Long> {
  //  PetInfo findByDesertionNo(Long desertionNo);
    Page<PetInfo> findAll(Pageable pageable);
  @Query("SELECT b.desertionNo FROM PetInfo b")
  List<Long> findAllDesertionNos();


  //   void deleteByCreatedDateBefore(LocalDateTime oneWeekAgo);
}


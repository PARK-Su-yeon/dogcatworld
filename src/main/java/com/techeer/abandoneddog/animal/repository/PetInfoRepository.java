package com.techeer.abandoneddog.animal.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.shelter.entity.Shelter;

@Repository
public interface PetInfoRepository extends JpaRepository<PetInfo, Long> {
	//  PetInfo findByDesertionNo(Long desertionNo);
	Page<PetInfo> findAll(Pageable pageable);

	@Query("SELECT b.desertionNo FROM PetInfo b")
	List<Long> findAllDesertionNos();

	List<PetInfo> findByPetBoardStoredFalse();

	Page<PetInfo> findByShelter(Shelter shelter, Pageable pageable);

	Optional<PetInfo> findPetInfoByPopfile(String popfile);
	//   void deleteByCreatedDateBefore(LocalDateTime oneWeekAgo);
}


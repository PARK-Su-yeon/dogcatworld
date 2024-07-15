package com.techeer.abandoneddog.shelter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techeer.abandoneddog.shelter.entity.Shelter;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {

	boolean existsByCareNm(String careNm);

	Optional<Shelter> findByCareNm(String careNm);

	Optional<Shelter> findById(Long shelter_id);

}
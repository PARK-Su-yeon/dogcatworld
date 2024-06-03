package com.techeer.abandoneddog.shelter.repository;

import com.techeer.abandoneddog.shelter.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ShelterRepository extends JpaRepository<Shelter,Long> {
    boolean existsByCareNm(String careNm);

    Optional<Shelter> findByCareNm(String careNm);

}
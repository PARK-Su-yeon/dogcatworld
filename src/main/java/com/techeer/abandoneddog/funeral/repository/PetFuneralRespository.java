package com.techeer.abandoneddog.funeral.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techeer.abandoneddog.funeral.entity.PetFuneral;

public interface PetFuneralRespository extends JpaRepository<PetFuneral, Long> {
}

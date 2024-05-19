package com.techeer.abandoneddog.funeral.repository;

import com.techeer.abandoneddog.funeral.entity.PetFuneral;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PetFuneralRespository extends JpaRepository<PetFuneral, Long> {
}

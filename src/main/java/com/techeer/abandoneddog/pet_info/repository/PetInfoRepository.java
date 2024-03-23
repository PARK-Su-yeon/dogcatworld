package com.techeer.abandoneddog.pet_info.repository;

import com.techeer.abandoneddog.pet_info.entity.PetInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetInfoRepository extends JpaRepository<PetInfo, Long> {
}

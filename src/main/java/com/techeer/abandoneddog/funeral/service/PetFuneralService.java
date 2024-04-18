package com.techeer.abandoneddog.funeral.service;

import com.techeer.abandoneddog.funeral.dto.PetFuneralResponseDto;
import com.techeer.abandoneddog.funeral.entity.PetFuneral;
import com.techeer.abandoneddog.funeral.repository.PetFuneralRespository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PetFuneralService {

    private final PetFuneralRespository petFuneralRespository;

    @Transactional
    public List<PetFuneralResponseDto> getPetFunerals() {

        List<PetFuneral> petFuneralList = petFuneralRespository.findAll();
        return petFuneralList.stream()
                .map(PetFuneralResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}

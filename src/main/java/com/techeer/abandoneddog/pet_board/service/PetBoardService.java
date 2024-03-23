package com.techeer.abandoneddog.pet_board.service;

import com.techeer.abandoneddog.pet_board.dto.PetBoardRequestDto;
import com.techeer.abandoneddog.pet_board.dto.PetBoardResponseDto;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.pet_board.repository.PetBoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PetBoardService {
    private final PetBoardRepository petBoardRepository;

    @Transactional
    public Long createPetBoard(PetBoardRequestDto petBoardRequestDto) {
        return petBoardRepository.save(petBoardRequestDto.toEntity()).getPetBoardId();
    }

    @Transactional
    public PetBoardResponseDto updatePetBoard(Long petBoardId, PetBoardRequestDto requestDto) {
        PetBoard petBoard = petBoardRepository.findById(petBoardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + petBoardId));

        petBoard.update(requestDto);
        return PetBoardResponseDto.fromEntity(petBoard);
    }

}

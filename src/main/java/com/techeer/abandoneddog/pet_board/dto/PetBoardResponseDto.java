package com.techeer.abandoneddog.pet_board.dto;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor

@NoArgsConstructor
public class PetBoardResponseDto {
    private Long petBoardId;
//    private Long petId;
//    private Long memberId;
    private String title;
    private String description;
    @Nullable
    private PetInfo petInfo;
    @Nullable
    private String status;

    public static PetBoardResponseDto fromEntity(PetBoard petBoard) {
        return new PetBoardResponseDto(
                petBoard.getPetBoardId(),
                petBoard.getTitle(),
                petBoard.getDescription(),
                petBoard.getPetInfo(),
                petBoard.getStatus().toString()
        );
    }
}

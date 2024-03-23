package com.techeer.abandoneddog.pet_board.dto;

import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetBoardResponseDto {
    private Long petBoardId;
//    private Long petId;
//    private Long memberId;
    private String title;
    private String description;
    private String location;
    private String status;

    public static PetBoardResponseDto fromEntity(PetBoard petBoard) {
        return new PetBoardResponseDto(
                petBoard.getPetBoardId(),
                petBoard.getTitle(),
                petBoard.getDescription(),
                petBoard.getLocation(),
                petBoard.getStatus().toString()
        );
    }
}

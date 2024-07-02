package com.techeer.abandoneddog.pet_board.dto;

import com.techeer.abandoneddog.animal.PetInfoDto.PetInfoResponseDto;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetBoardDetailResponseDto {
    private Long petBoardId;
    private String title;
    private String description;
    @Nullable
    private PetInfoResponseDto petInfo;
    @Nullable
    private String status;
    private boolean isLiked;
    private Integer likeCount;

    public static PetBoardDetailResponseDto fromEntity(PetBoard petBoard, boolean isLiked) {
        PetInfoResponseDto petInfoResponseDto = null;
        if (petBoard.getPetInfo() != null) {
            petInfoResponseDto = PetInfoResponseDto.fromEntity(petBoard.getPetInfo());
        }

        return new PetBoardDetailResponseDto(
                petBoard.getPetBoardId(),
                petBoard.getTitle(),
                petBoard.getDescription(),
                petInfoResponseDto,
                petBoard.getStatus() != null ? petBoard.getStatus().toString() : "N/A",
                isLiked,
                petBoard.getLikeCount()
        );
    }
}
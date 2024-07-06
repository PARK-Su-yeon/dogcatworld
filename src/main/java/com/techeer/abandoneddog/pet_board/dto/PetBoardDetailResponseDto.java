package com.techeer.abandoneddog.pet_board.dto;

import com.techeer.abandoneddog.animal.PetInfoDto.PetInfoResponseDto;
import com.techeer.abandoneddog.image.entity.Image;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<String> imageUrls;

    public static PetBoardDetailResponseDto fromEntity(PetBoard petBoard, boolean isLiked) {
        PetInfoResponseDto petInfoResponseDto = null;
        if (petBoard.getPetInfo() != null) {
            petInfoResponseDto = PetInfoResponseDto.fromEntity(petBoard.getPetInfo());
        }


        List<String> imageUrls = petBoard.getPetInfo() != null && petBoard.getPetInfo().getImages() != null
                ? petBoard.getPetInfo().getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList())
                : List.of();

        return new PetBoardDetailResponseDto(
                petBoard.getPetBoardId(),
                petBoard.getTitle(),
                petBoard.getDescription(),
                petInfoResponseDto,
                petBoard.getStatus() != null ? petBoard.getStatus().toString() : "N/A",
                isLiked,
                petBoard.getLikeCount(),
                imageUrls
        );
    }
}
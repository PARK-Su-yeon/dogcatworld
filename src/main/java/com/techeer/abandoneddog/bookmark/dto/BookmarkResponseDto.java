package com.techeer.abandoneddog.bookmark.dto;

import com.techeer.abandoneddog.bookmark.entity.Bookmark;
import com.techeer.abandoneddog.pet_board.dto.PetBoardDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkResponseDto {
    private Long id;
    private PetBoardDto petBoard;

    public static BookmarkResponseDto fromEntity(Bookmark bookmark) {
        return new BookmarkResponseDto(
                bookmark.getId(),
                PetBoardDto.fromEntity(bookmark.getPetBoard())
        );
    }
}

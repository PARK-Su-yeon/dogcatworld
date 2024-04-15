package com.techeer.abandoneddog.bookmark.dto;

import com.techeer.abandoneddog.bookmark.entity.Bookmark;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkResponseDto {
    private Long id;
    private PetBoard petBoard;

    public static BookmarkResponseDto fromEntity(Bookmark bookmark) {
        return new BookmarkResponseDto(
                bookmark.getId(),
                bookmark.getPetBoard()
        );
    }
}

package com.techeer.abandoneddog.bookmark.dto;

import com.techeer.abandoneddog.bookmark.entity.Bookmark;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.users.entity.Users;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkRequestDto {
    private Users user;
    private PetBoard petBoard;

    public Bookmark toEntity() {
        return Bookmark.builder()
                .user(user)
                .petBoard(petBoard)
                .build();
    }
}

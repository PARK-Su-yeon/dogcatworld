package com.techeer.abandoneddog.bookmark.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkRequestDto {
    private Long userId;
    private Long petBoardId;
}

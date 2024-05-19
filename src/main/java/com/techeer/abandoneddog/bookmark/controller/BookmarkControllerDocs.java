package com.techeer.abandoneddog.bookmark.controller;

import com.techeer.abandoneddog.bookmark.dto.BookmarkRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Bookmark Api", description = "북마크 저장/조회/취소")
public interface BookmarkControllerDocs {

    @Parameters(value = {
            @Parameter(name = "userId", description = "유저 고유 식별아이디"),
            @Parameter(name = "petBoardId", description = "게시글 고유 식별아이디")
    })
    @Operation(summary = "북마크 저장", description = "유저 고유 아이디, 게시글 고유 아이디를 입력받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "북마크 저장 성공")
    })
    public ResponseEntity<?> addBookmark(@RequestBody BookmarkRequestDto bookmarkRequestDto);

    @Parameters(value = {
            @Parameter(name = "userId", description = "유저 고유 식별아이디")
    })
    @Operation(summary = "북마크 리스트 조회", description = "유저 고유 아이디, 게시글 고유 아이디를 입력받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "북마크 리스트 조회 성공")
    })
    public ResponseEntity<?> getUserBookmark(@PathVariable Long userId, Pageable pageable);

    @Parameters(value = {
            @Parameter(name = "userId", description = "유저 고유 식별아이디"),
            @Parameter(name = "petBoardId", description = "게시글 고유 식별아이디")
    })
    @Operation(summary = "북마크 취소", description = "유저 고유 아이디, 게시글 고유 아이디를 입력받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "북마크 취소 성공")
    })
    public ResponseEntity<?> cancelBookmark(@PathVariable Long bookmarkId, @RequestBody BookmarkRequestDto bookmarkRequestDto);

}


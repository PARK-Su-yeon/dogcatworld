package com.techeer.abandoneddog.bookmark.controller;

import com.techeer.abandoneddog.bookmark.dto.BookmarkRequestDto;
import com.techeer.abandoneddog.bookmark.dto.BookmarkResponseDto;
import com.techeer.abandoneddog.bookmark.service.BookmarkService;
import com.techeer.abandoneddog.users.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark")
@Slf4j
public class BookmarkController implements BookmarkControllerDocs {

    @Autowired
    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<?> addBookmark(@RequestBody BookmarkRequestDto bookmarkRequestDto) {
        try {
            bookmarkService.addBookmark(bookmarkRequestDto);
            return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "북마크 저장 성공"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("북마크 저장에 실패했습니다.");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserBookmark(@PathVariable Long userId, Pageable pageable) {
        try {
            Page<BookmarkResponseDto> bookmarkPage = bookmarkService.getUserBookmarks(pageable, userId);
            return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "유저 북마크 리스트 조회 성공", bookmarkPage.getContent()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저 북마크 리스트 조회에 실패했습니다.");
        }
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<?> cancelBookmark(@PathVariable Long bookmarkId, @RequestBody BookmarkRequestDto bookmarkRequestDto) {
        try {
            bookmarkService.cancelBookmark(bookmarkRequestDto);
            return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "북마크 취소 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("북마크 취소에 실패했습니다.");
        }
    }
}

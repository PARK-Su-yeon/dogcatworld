package com.techeer.abandoneddog.bookmark.controller;

import com.techeer.abandoneddog.bookmark.dto.BookmarkRequestDto;
import com.techeer.abandoneddog.bookmark.dto.BookmarkResponseDto;
import com.techeer.abandoneddog.bookmark.service.BookmarkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark")
@Tag(name = "Bookmark", description = "북마크")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<?> createBookmark(@RequestParam(defaultValue = "1") Long userId, @RequestParam(defaultValue = "1") Long petBoardId) {
        try {
            bookmarkService.createBookmark(userId, petBoardId);
            return ResponseEntity.ok().body("북마크 생성 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("북마크 생성에 실패했습니다.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBookmark(@PathVariable Long id, @RequestBody BookmarkRequestDto bookmarkRequestDto) {
        try {
            bookmarkService.updateBookmark(id, bookmarkRequestDto);
            return ResponseEntity.ok().body("북마크 수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("북마크 수정에 실패했습니다.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookmark(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(bookmarkService.getBookmark(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("북마크 조회에 실패했습니다.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getBookmarks(Pageable pageable) {
        try {
            Page<BookmarkResponseDto> bookmarkPage = bookmarkService.getBookmarks(pageable);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "북마크 리스트 조회 성공");
            response.put("result", bookmarkPage.getContent());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("북마크 리스트 조회에 실패했습니다.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookmark(@PathVariable Long id) {
        try {
            bookmarkService.deleteBookmark(id);
            return ResponseEntity.status(HttpStatus.OK).body("북마크 삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("북마크 삭제에 실패하였습니다.");
        }
    }
}

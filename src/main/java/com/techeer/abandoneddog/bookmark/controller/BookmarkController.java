package com.techeer.abandoneddog.bookmark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techeer.abandoneddog.bookmark.dto.BookmarkRequestDto;
import com.techeer.abandoneddog.bookmark.dto.BookmarkResponseDto;
import com.techeer.abandoneddog.bookmark.service.BookmarkService;
import com.techeer.abandoneddog.users.dto.ResultDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	public ResponseEntity<?> getUserBookmark(@PathVariable("userId") Long userId, Pageable pageable) {
		try {
			Page<BookmarkResponseDto> bookmarkPage = bookmarkService.getUserBookmarks(pageable, userId);
			//            List<BookmarkResponseDto> bookmarkPage = bookmarkService.getUserBookmarks(userId);
			return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "유저 북마크 리스트 조회 성공", bookmarkPage.getContent()));
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저 북마크 리스트 조회에 실패했습니다.");
		}
	}

	@DeleteMapping
	public ResponseEntity<?> cancelBookmark(@RequestBody BookmarkRequestDto bookmarkRequestDto) {
		try {
			bookmarkService.cancelBookmark(bookmarkRequestDto);
			return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "북마크 취소 성공"));
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("북마크 취소에 실패했습니다.");
		}
	}
}
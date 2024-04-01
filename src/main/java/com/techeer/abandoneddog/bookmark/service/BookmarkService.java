package com.techeer.abandoneddog.bookmark.service;

import com.techeer.abandoneddog.bookmark.dto.BookmarkRequestDto;
import com.techeer.abandoneddog.bookmark.dto.BookmarkResponseDto;
import com.techeer.abandoneddog.bookmark.entity.Bookmark;
import com.techeer.abandoneddog.bookmark.repository.BookmarkRepository;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.pet_board.repository.PetBoardRepository;
import com.techeer.abandoneddog.users.entity.Users;
import com.techeer.abandoneddog.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    @Autowired
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PetBoardRepository petBoardRepository;

    @Transactional
    public void createBookmark(Long userId, Long petBoardId) {
        Users user = userRepository.findUserById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        PetBoard petBoard = petBoardRepository.findById(petBoardId).orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
        BookmarkRequestDto bookmark = new BookmarkRequestDto();
        bookmark.setUser(user);
        bookmark.setPetBoard(petBoard);
        bookmarkRepository.save(bookmark.toEntity());
    }

    @Transactional
    public BookmarkResponseDto updateBookmark(Long id, BookmarkRequestDto bookmarkRequestDto) {
        Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 북마크를 찾을 수 없습니다."));
        bookmark.update(bookmarkRequestDto);
        return BookmarkResponseDto.fromEntity(bookmark);
    }

    @Transactional
    public BookmarkResponseDto getBookmark(Long id) {
        Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 북마크를 찾을 수 없습니다."));
        return BookmarkResponseDto.fromEntity(bookmark);
    }

    @Transactional
    public Page<BookmarkResponseDto> getBookmarks(Pageable pageable) {
        Page<Bookmark> bookmarkPage = bookmarkRepository.findAll(pageable);
        return bookmarkPage.map(BookmarkResponseDto::fromEntity);
    }

    @Transactional
    public void deleteBookmark(Long id) {
        Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 북마크를 찾을 수 없습니다."));
        bookmarkRepository.delete(bookmark);

    }

}

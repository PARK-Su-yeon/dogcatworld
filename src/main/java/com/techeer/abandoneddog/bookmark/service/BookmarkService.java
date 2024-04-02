package com.techeer.abandoneddog.bookmark.service;

import com.techeer.abandoneddog.bookmark.dto.BookmarkDto;
import com.techeer.abandoneddog.bookmark.dto.BookmarkRequestDto;
import com.techeer.abandoneddog.bookmark.dto.BookmarkResponseDto;
import com.techeer.abandoneddog.bookmark.entity.Bookmark;
import com.techeer.abandoneddog.bookmark.repository.BookmarkRepository;
import com.techeer.abandoneddog.global.exception.petboard.InvalidPetBoardRequestException;
import com.techeer.abandoneddog.global.exception.petboard.PetBoardNotFoundException;
import com.techeer.abandoneddog.global.exception.user.UserNotFoundException;
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
        BookmarkDto bookmark = new BookmarkDto();
        bookmark.setUser(user);
        bookmark.setPetBoard(petBoard);
        bookmarkRepository.save(bookmark.toEntity());
    }

    @Transactional
    public BookmarkResponseDto updateBookmark(Long id, BookmarkDto bookmarkDto) {
        Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 북마크를 찾을 수 없습니다."));
        bookmark.update(bookmarkDto);
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

    @Transactional
    public void BookmarkBoard(Long petBoardId, Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        PetBoard petBoard = petBoardRepository.findById(petBoardId).orElseThrow();

        BookmarkDto bookmarkDto = new BookmarkDto();
        bookmarkDto.setUser(user);
        bookmarkDto.setPetBoard(petBoard);
        bookmarkRepository.save(bookmarkDto.toEntity());
    }

    @Transactional
    public void addBookmark(BookmarkRequestDto requestDto) {
        Users user = userRepository.findById(requestDto.getUserId()).orElseThrow(UserNotFoundException::new);
        PetBoard petBoard = petBoardRepository.findById(requestDto.getPetBoardId()).orElseThrow(PetBoardNotFoundException::new);

        if (!bookmarkRepository.existsByPetBoardAndUser(petBoard, user)) {
            BookmarkDto bookmarkDto = new BookmarkDto();
            bookmarkDto.setUser(user);
            bookmarkDto.setPetBoard(petBoard);
            bookmarkRepository.save(bookmarkDto.toEntity());
        } else {
            System.out.println("error");
            throw new InvalidPetBoardRequestException();
        }
    }

    @Transactional
    public Page<BookmarkResponseDto> getUserBookmarks(Pageable pageable, Long userId) {
        Page<Bookmark> bookmarkPage = bookmarkRepository.findBookmarksByUserId(pageable, userId);
        return bookmarkPage.map(BookmarkResponseDto::fromEntity);
    }
}

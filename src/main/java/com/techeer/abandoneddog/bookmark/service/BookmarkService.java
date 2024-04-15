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
    public void addBookmark(BookmarkRequestDto requestDto) {
        Users user = userRepository.findById(requestDto.getUserId()).orElseThrow(UserNotFoundException::new);
        PetBoard petBoard = petBoardRepository.findById(requestDto.getPetBoardId()).orElseThrow(PetBoardNotFoundException::new);

        if (!bookmarkRepository.existsByPetBoardAndUser(petBoard, user)) {
            BookmarkDto bookmarkDto = new BookmarkDto();
            bookmarkDto.setUser(user);
            bookmarkDto.setPetBoard(petBoard);
            bookmarkRepository.save(bookmarkDto.toEntity());
        } else {
            Bookmark bookmark = bookmarkRepository.findByPetBoardAndUser(petBoard, user);
            bookmarkRepository.resave(bookmark.getId());
//            throw new InvalidPetBoardRequestException();
        }
    }

    @Transactional
    public Page<BookmarkResponseDto> getUserBookmarks(Pageable pageable, Long userId) {
        Page<Bookmark> bookmarkPage = bookmarkRepository.findBookmarksByUserIdAndIsDeletedFalse(pageable, userId);
        return bookmarkPage.map(BookmarkResponseDto::fromEntity);
    }

    @Transactional
    public void cancelBookmark(BookmarkRequestDto requestDto) {
        Users user = userRepository.findById(requestDto.getUserId()).orElseThrow(UserNotFoundException::new);
        PetBoard petBoard = petBoardRepository.findById(requestDto.getPetBoardId()).orElseThrow(PetBoardNotFoundException::new);

        if (!bookmarkRepository.existsByPetBoardAndUser(petBoard, user)) {
            throw new InvalidPetBoardRequestException();
        }

        Bookmark bookmark = bookmarkRepository.findByPetBoardAndUser(petBoard, user);
        bookmarkRepository.delete(bookmark);
    }
}

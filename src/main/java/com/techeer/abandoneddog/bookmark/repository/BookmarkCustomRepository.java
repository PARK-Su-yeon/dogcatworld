package com.techeer.abandoneddog.bookmark.repository;

import com.techeer.abandoneddog.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookmarkCustomRepository {
    Page<Bookmark> findBookmarksByUserIdAndIsDeletedFalse(Pageable pageable, Long userId);
    List<Bookmark> findBookmarkListByUserIdAndIsDeletedFalse(Long userId);
}

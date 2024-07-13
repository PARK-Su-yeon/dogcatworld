package com.techeer.abandoneddog.bookmark.repository;

import com.techeer.abandoneddog.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkCustomRepository {
    Page<Bookmark> findBookmarksByUserIdAndIsDeletedFalse(Pageable pageable, Long userId);
}

package com.techeer.abandoneddog.bookmark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.techeer.abandoneddog.bookmark.entity.Bookmark;

public interface BookmarkCustomRepository {
	Page<Bookmark> findBookmarksByUserIdAndIsDeletedFalse(Pageable pageable, Long userId);
}

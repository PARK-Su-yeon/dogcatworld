package com.techeer.abandoneddog.bookmark.repository;

import com.techeer.abandoneddog.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

}

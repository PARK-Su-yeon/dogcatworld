package com.techeer.abandoneddog.bookmark.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.techeer.abandoneddog.bookmark.entity.Bookmark;
import com.techeer.abandoneddog.bookmark.entity.QBookmark;
import com.techeer.abandoneddog.pet_board.entity.QPetBoard;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class BookmarkCustomRepositoryImpl implements BookmarkCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public BookmarkCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Bookmark> findBookmarksByUserIdAndIsDeletedFalse(Pageable pageable, Long userId) {
        QBookmark bookmark = QBookmark.bookmark;
        QPetBoard petBoard = QPetBoard.petBoard;

        List<Bookmark> bookmarks = queryFactory
                .selectFrom(bookmark)
                .leftJoin(bookmark.petBoard, petBoard).fetchJoin()
                .where(bookmark.user.id.eq(userId)
                        .and(bookmark.isDeleted.eq(false)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(bookmark)
                .where(bookmark.user.id.eq(userId)
                        .and(bookmark.isDeleted.eq(false)))
                .fetchCount();

        return new PageImpl<>(bookmarks, pageable, total);
    }

    @Override
    public List<Bookmark> findBookmarkListByUserIdAndIsDeletedFalse(Long userId) {
        QBookmark bookmark = QBookmark.bookmark;
        QPetBoard petBoard = QPetBoard.petBoard;

        return queryFactory
                .selectFrom(bookmark)
                .leftJoin(bookmark.petBoard, petBoard).fetchJoin()
                .where(bookmark.user.id.eq(userId)
                        .and(bookmark.isDeleted.eq(false)))
                .fetch();
    }
}
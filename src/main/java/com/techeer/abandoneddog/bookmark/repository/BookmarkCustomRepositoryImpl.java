package com.techeer.abandoneddog.bookmark.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.techeer.abandoneddog.animal.entity.QPetInfo;
import com.techeer.abandoneddog.bookmark.entity.Bookmark;
import com.techeer.abandoneddog.bookmark.entity.QBookmark;
import com.techeer.abandoneddog.pet_board.entity.QPetBoard;
import com.techeer.abandoneddog.shelter.entity.QShelter;
import com.techeer.abandoneddog.users.entity.QUsers;

import jakarta.transaction.Transactional;

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
		QPetInfo petInfo = QPetInfo.petInfo;
		QUsers user = QUsers.users;
		QShelter shelter = QShelter.shelter;

		List<Bookmark> bookmarks = queryFactory.selectFrom(bookmark)
			.leftJoin(bookmark.petBoard, petBoard).fetchJoin()
			.leftJoin(petBoard.petInfo, petInfo).fetchJoin()
			.leftJoin(petBoard.users, user).fetchJoin()
			.leftJoin(petInfo.shelter, shelter).fetchJoin()
			.where(bookmark.user.id.eq(userId)
				.and(bookmark.isDeleted.eq(false))
				.and(petBoard.isDeleted.eq(false))
				.and(petInfo.isDeleted.eq(false)))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory.selectFrom(bookmark)
			.leftJoin(bookmark.petBoard, petBoard)
			.leftJoin(petBoard.petInfo, petInfo)
			.leftJoin(petBoard.users, user)
			.leftJoin(petInfo.shelter, shelter)
			.where(bookmark.user.id.eq(userId)
				.and(bookmark.isDeleted.eq(false))
				.and(petBoard.isDeleted.eq(false))
				.and(petInfo.isDeleted.eq(false)))
			.fetchCount();

		return new PageImpl<>(bookmarks, pageable, total);
	}
}
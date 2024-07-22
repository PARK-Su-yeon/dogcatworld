package com.techeer.abandoneddog.pet_board.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.techeer.abandoneddog.animal.entity.QPetInfo;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.pet_board.entity.QPetBoard;
import com.techeer.abandoneddog.pet_board.entity.Status;

import jakarta.persistence.EntityManager;

@Repository
public class PetBoardRepositoryImpl implements PetBoardRepositoryCustom {

	@Autowired
	private EntityManager entityManager;

	@Override
	public Page<PetBoard> searchPetBoards(String categories, Status status, Integer minAge, Integer maxAge,
		String title, Boolean isYoung, Pageable pageable) {
		QPetBoard petBoard = QPetBoard.petBoard;
		QPetInfo petInfo = QPetInfo.petInfo;

		JPAQuery<PetBoard> query = new JPAQuery<>(entityManager);
		BooleanBuilder builder = new BooleanBuilder();

		query.from(petBoard)
			.join(petBoard.petInfo, petInfo);

		if (categories != null) {
			builder.and(petInfo.kindCd.eq(categories));
		}
		if (status != null) {
			builder.and(petBoard.status.eq(status));
		}
		if (minAge != null) {
			builder.and(petInfo.age.goe(minAge));
		}
		if (maxAge != null) {
			builder.and(petInfo.age.loe(maxAge));
		}
		if (isYoung != null) {
			builder.and(petInfo.isYoung.eq(isYoung));
		}
		if (title != null) {
			builder.and(petBoard.title.containsIgnoreCase(title));
		}

		query.where(builder);

		long total = query.fetchCount();
		List<PetBoard> results = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(results, pageable, total);
	}

}
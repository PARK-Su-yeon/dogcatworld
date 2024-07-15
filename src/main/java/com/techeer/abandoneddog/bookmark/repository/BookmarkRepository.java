package com.techeer.abandoneddog.bookmark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techeer.abandoneddog.bookmark.entity.Bookmark;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.users.entity.Users;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	@Query("SELECT b FROM Bookmark b JOIN FETCH b.petBoard WHERE b.user.id = :userId AND b.isDeleted = false")
	Page<Bookmark> findBookmarksByUserIdAndIsDeletedFalse(Pageable pageable, @Param("userId") Long userId);

	boolean existsByPetBoardAndUser(PetBoard petBoard, Users user);

	boolean existsByPetBoardAndUserIdAndIsDeletedFalse(PetBoard petBoard, Long userId);

	Bookmark findByPetBoardAndUser(PetBoard petBoard, Users users);

	@Modifying
	@Query("UPDATE Bookmark b SET b.isDeleted=false WHERE b.id= :bookmarkId")
	void resave(Long bookmarkId);
}

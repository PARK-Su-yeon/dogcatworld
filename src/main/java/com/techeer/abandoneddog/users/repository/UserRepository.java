package com.techeer.abandoneddog.users.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.techeer.abandoneddog.users.entity.Users;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

	Optional<Users> findUserById(Long userId);

	Optional<Users> findByEmail(String email);

	Optional<Users> findByIdAndIsDeletedFalse(Long id);

	@Query("SELECT u FROM Users u WHERE u.isDeleted = false")
	Page<Users> findAllActiveUsers(Pageable pageable);

	@Modifying
	@Transactional
	@Query("UPDATE Users u SET u.isDeleted = false WHERE u.id = :userId")
	void reactivateUser(Long userId);
}

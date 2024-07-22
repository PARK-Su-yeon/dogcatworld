package com.techeer.abandoneddog.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techeer.abandoneddog.users.entity.RefreshEntity;

import jakarta.transaction.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

	Boolean existsByRefresh(String refresh);

	@Transactional
	void deleteByRefresh(String refresh);
}

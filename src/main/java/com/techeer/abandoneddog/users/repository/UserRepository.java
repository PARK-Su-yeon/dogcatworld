package com.techeer.abandoneddog.users.repository;

import com.techeer.abandoneddog.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findUserById(Long userId);

    Users findByEmail(String email);

    Users findByUsername(String username);

    Optional<Users> findUserByEmail(String email);

    boolean existsByEmail(String email);

}

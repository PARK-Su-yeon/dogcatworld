package com.techeer.abandoneddog.users.repository;

import com.techeer.abandoneddog.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);
    Optional<Users> findUserByEmail(String email);

    boolean existsByEmail(String email);

}

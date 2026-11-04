package com.application.sharkpark.repository;

import com.application.sharkpark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByFirstnameAndLastname(String firstname, String lastname);

    boolean existsByEmail(String email);

    Optional<User> findByUserId(Integer id);
}
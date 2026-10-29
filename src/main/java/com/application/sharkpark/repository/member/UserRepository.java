package com.application.sharkpark.repository.member;

import com.application.sharkpark.domain.member.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    private

    Optional<User> findByEmail(String email);

    Optional<User> findByFirstnameAndLastname(String firstname, String lastname);
}
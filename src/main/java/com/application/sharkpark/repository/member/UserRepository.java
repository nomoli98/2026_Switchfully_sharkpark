package com.application.sharkpark.repository.member;

import com.application.sharkpark.domain.member.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User saveUser(User newUser);

    List<User> findAllUsers();

    Optional<User> findByUserId(Integer userId);

    Optional<User> findByFirstnameAndLastname(String firstname, String lastname);

    boolean existsByUserId(Integer userId);
}

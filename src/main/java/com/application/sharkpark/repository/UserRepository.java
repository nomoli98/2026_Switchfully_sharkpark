package com.application.sharkpark.repository;

import com.application.sharkpark.domain.member.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User saveUser(User newUser);

    List<User> findAllUsers();

    User findByUserId(int userId);

    User findByFirstnameAndLastname(String firstname, String lastname);

}

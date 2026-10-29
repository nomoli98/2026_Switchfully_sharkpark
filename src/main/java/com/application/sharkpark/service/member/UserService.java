package com.application.sharkpark.service.member;

import com.application.sharkpark.domain.member.User;
import com.application.sharkpark.repository.member.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {this.userRepository = userRepository;}

    public User save(User user) {
        return userRepository.saveUser(user);
    }

    public List<User> findAll() {
        return userRepository.findAllUsers();
    }

    public User findById(Integer userId) {
        return userRepository.findByUserId(userId)
                             .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findByFullName(String firstname, String lastname) {
        return userRepository.findByFirstnameAndLastname(firstname, lastname)
                             .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean existsById(Integer userId){
        return userRepository.existsByUserId(userId);
    }
}

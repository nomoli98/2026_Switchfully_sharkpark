package com.application.sharkpark.service.member;

import com.application.sharkpark.domain.member.City;
import com.application.sharkpark.domain.member.User;
import com.application.sharkpark.dto.request.member.CreateUserRequest;
import com.application.sharkpark.mapper.UserMapper;
import com.application.sharkpark.repository.member.UserRepository;
import com.application.sharkpark.validator.UserRequestValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CityService cityService;

    public UserService(UserRepository userRepository, CityService cityService) {
        this.userRepository = userRepository;
        this.cityService = cityService;
    }

    public User save(CreateUserRequest request) {
        UserRequestValidator.validateNewUser(request);
        City city = cityService.findByPostalCode(request.postalCode());
        User user = UserMapper.toEntity(request, city);
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

    public boolean existsById(Integer userId) {
        return userRepository.existsByUserId(userId);
    }
}

package com.application.sharkpark.service;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateUserRequest;
import com.application.sharkpark.dto.request.RegisterMemberRequest;
import com.application.sharkpark.dto.response.UserResponse;
import com.application.sharkpark.exceptions.NotFoundException;
import com.application.sharkpark.exceptions.RequestException;
import com.application.sharkpark.mapper.UserMapper;
import com.application.sharkpark.repository.UserRepository;
import com.application.sharkpark.validator.UserRequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final CityService cityService;

    public UserService(UserRepository userRepository, CityService cityService) {
        this.userRepository = userRepository;
        this.cityService = cityService;
    }

    public UserResponse save(CreateUserRequest request) {
        UserRequestValidator.validateNewUser(request);
        if (existsByEmail(request.email())) throw RequestException.existingEmail();
        logger.info("Request validated.");
        City city = cityService.findByPostalCode(request.postalCode());
        User user = UserMapper.toEntity(request, city);
        logger.info("New user created.");
        userRepository.save(user);
        return UserMapper.toUserResponse(user);
    }

    public UserResponse register(RegisterMemberRequest request) {
        logger.info("New user registered.");
        return this.save(UserMapper.toCreateUserRequest(request));
    }

    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        logger.info("Fetching all users.");
        return UserMapper.toListOfUserResponses(users);
    }

    public UserResponse findById(Integer userId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new NotFoundException("User not found"));
        logger.info("Fetching user by id: {}", userId);
        return UserMapper.toUserResponse(user);
    }

    public UserResponse findByFullName(String firstname, String lastname) {
        User user = userRepository.findByFirstnameAndLastname(firstname, lastname)
                                  .orElseThrow(() -> new NotFoundException("User not found"));
        logger.info("Fetching user by full name: {} {}", firstname, lastname);

        return UserMapper.toUserResponse(user);
    }

    public boolean existsByEmail(String email) {return userRepository.existsByEmail(email);}
}
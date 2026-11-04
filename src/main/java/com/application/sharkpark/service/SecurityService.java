package com.application.sharkpark.service;

import com.application.sharkpark.domain.User;
import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.exceptions.ForbiddenException;
import com.application.sharkpark.exceptions.UnauthorizedException;
import com.application.sharkpark.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SecurityService {
    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateAuthorization(String email, Feature feature) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(UnauthorizedException::new);

        if (!user.canHaveAccessTo(feature)) {
            throw new ForbiddenException();
        }
    }
}

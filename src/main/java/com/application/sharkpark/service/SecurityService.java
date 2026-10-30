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

    public void validateAuthorization(String authorization, Feature feature) {
        if (authorization == null || !authorization.contains("Basic ")) {
            throw new UnauthorizedException();
        }

        String encodedCredentials = authorization.substring(6);
        String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials), StandardCharsets.UTF_8);

        if (!decodedCredentials.contains(":")) {
            throw new UnauthorizedException();
        }

        String[] credentials = decodedCredentials.split(":", 2);
        String emailAddress = credentials[0];
        String password = credentials[1];

        User user = userRepository.findByEmail(emailAddress)
                .orElseThrow(UnauthorizedException::new);

        if (!user.doesPasswordMatch(password)) {
            throw new UnauthorizedException();
        }

        if (!user.canHaveAccessTo(feature)) {
            throw new ForbiddenException();
        }
    }
}

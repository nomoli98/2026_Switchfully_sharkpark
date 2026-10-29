package com.application.sharkpark.exceptions;

import com.application.sharkpark.domain.member.User;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer userId) {
        super("User with id " + userId + " doesn't exists");
    }
}

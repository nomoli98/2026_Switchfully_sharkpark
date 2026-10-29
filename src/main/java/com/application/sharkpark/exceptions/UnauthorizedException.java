package com.application.sharkpark.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Invalid credentials");
    }
}

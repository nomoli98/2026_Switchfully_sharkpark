package com.application.sharkpark.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("Access to the resource not allowed");
    }
}

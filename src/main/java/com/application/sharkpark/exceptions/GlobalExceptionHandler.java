package com.application.sharkpark.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<Map<String, String>> handleRequestException(RequestException exception) {
        logBadRequest(exception.getMessage());
        return response(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({UnauthorizedException.class, InvalidParkingAllocationOwnerException.class})
    public ResponseEntity<Map<String, String>> handleUnauthorized(RuntimeException exception) {
        logger.warn("Unauthorized access: {}", exception.getMessage());
        return response(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(ForbiddenException exception) {
        logger.warn("Forbidden access: {}", exception.getMessage());
        return response(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler({NotFoundException.class, DivisionNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException exception) {
        logger.warn("Resource not found: {}", exception.getMessage());
        return response(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler({ParkingLotFullException.class, ActiveParkingAllocationExistException.class,
            ParkingAllocationAlreadyStoppedException.class})
    public ResponseEntity<Map<String, String>> handleForbidden(RuntimeException exception) {
        logger.warn("Conflict: {}", exception.getMessage());
        return response(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
        List<String> messages = exception.getBindingResult().getAllErrors()
                        .stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .toList();

        logBadRequest(messages.toString());
        return response(HttpStatus.BAD_REQUEST, messages.toString());
    }

    private ResponseEntity<Map<String, String>> response(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("message", message));
    }

    private void logBadRequest(String message) {
        logger.warn("Bad request: {}", message);
    }
}

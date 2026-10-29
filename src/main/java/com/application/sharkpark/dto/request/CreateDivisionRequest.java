package com.application.sharkpark.dto.request;

public record CreateDivisionRequest(
        String name,
        String originalName,
        Integer directorId
) {
}

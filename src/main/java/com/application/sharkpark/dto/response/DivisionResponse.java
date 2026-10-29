package com.application.sharkpark.dto.response;

public record DivisionResponse(
        Integer divisionId,
        String name,
        String originalName,
        String directorName
) {
}

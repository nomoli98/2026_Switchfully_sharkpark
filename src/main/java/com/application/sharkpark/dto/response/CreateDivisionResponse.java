package com.application.sharkpark.dto.response;

public record CreateDivisionResponse(
        Integer divisionId,
        String name,
        String originalName,
        Integer director_id
) {
}

package com.application.sharkpark.dto.response;

public record SubDivisionResponse(
        Integer subdivisionId,
        String name,
        String originalName,
        String directorName
) {
}

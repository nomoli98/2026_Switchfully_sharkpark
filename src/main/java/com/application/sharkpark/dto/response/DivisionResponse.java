package com.application.sharkpark.dto.response;

import java.util.List;

public record DivisionResponse(
        Integer divisionId,
        String name,
        String originalName,
        String directorName,
        List<SubDivisionResponse> subdivisions
) {
}

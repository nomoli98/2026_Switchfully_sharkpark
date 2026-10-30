package com.application.sharkpark.mapper;

import com.application.sharkpark.domain.Division;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateDivisionRequest;
import com.application.sharkpark.dto.response.DivisionResponse;

import java.util.List;

public final class DivisionMapper {
    private DivisionMapper() {
    }

    public static Division toEntity(CreateDivisionRequest request, User director) {
        return new Division(
                request.name(),
                request.originalName(),
                director
        );
    }

    public static DivisionResponse toResponse(Division division) {
        return new DivisionResponse(
                division.getDivisionId(),
                division.getName(),
                division.getOriginalName(),
                String.join(" ", division.getDirector().getFirstname(), division.getDirector().getLastname())
        );
    }

    public static List<DivisionResponse> toResponse(List<Division> divisions) {
        return divisions.stream()
                .map((DivisionMapper::toResponse))
                .toList();
    }
}

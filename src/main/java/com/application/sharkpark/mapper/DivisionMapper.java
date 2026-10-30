package com.application.sharkpark.mapper;

import com.application.sharkpark.domain.Division;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateDivisionRequest;
import com.application.sharkpark.dto.response.DivisionResponse;
import com.application.sharkpark.dto.response.SubDivisionResponse;

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
                formatDirectorName(division.getDirector()),
                toSubdivisionResponse(division.getSubdivisions())
        );
    }

    public static List<DivisionResponse> toResponse(List<Division> divisions) {
        return divisions.stream()
                .map((DivisionMapper::toResponse))
                .toList();
    }

    private static SubDivisionResponse toSubdivisionResponse(Division division) {
        return new SubDivisionResponse(
                division.getDivisionId(),
                division.getName(),
                division.getOriginalName(),
                formatDirectorName(division.getDirector()));
    }

    private static List<SubDivisionResponse> toSubdivisionResponse(List<Division> divisions) {
        return divisions.stream()
                .map(DivisionMapper::toSubdivisionResponse)
                .toList();
    }

    private static String formatDirectorName(User director) {
        return String.join(" ", director.getFirstname(), director.getLastname());
    }
}

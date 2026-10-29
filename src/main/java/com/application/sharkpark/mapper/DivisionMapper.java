package com.application.sharkpark.mapper;

import com.application.sharkpark.domain.Division;
import com.application.sharkpark.domain.member.User;
import com.application.sharkpark.dto.request.CreateDivisionRequest;
import com.application.sharkpark.dto.request.member.CreateUserRequest;
import com.application.sharkpark.dto.response.DivisionResponse;

public class DivisionMapper {
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
}

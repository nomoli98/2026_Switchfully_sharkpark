package com.application.sharkpark.mapper;

import com.application.sharkpark.domain.Division;
import com.application.sharkpark.domain.member.User;
import com.application.sharkpark.dto.response.DivisionResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DivisionMapperTest {
    @Test
    void shouldMapDivisionToResponse() {
        // Arrange
        User director = new User();
        director.setUserId(1);
        director.setFirstname("Yv");
        director.setLastname("Es");

        Division division = new Division(
                "Switch",
                "Fully",
                director
        );

        // Act
        DivisionResponse response = DivisionMapper.toResponse(division);

        // Assert
        assertNotNull(response);
        assertEquals("Switch", response.name());
        assertEquals("Fully", response.originalName());
        assertEquals("Yv Es", response.directorName());
    }
}
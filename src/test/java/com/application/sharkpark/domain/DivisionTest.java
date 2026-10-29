package com.application.sharkpark.domain;

import com.application.sharkpark.domain.member.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

    class DivisionTest {

        @Test
        void shouldCreateDivisionWithCorrectValues() {
            // Arrange
            User director = new User();
            director.setFirstname("Yv");
            director.setLastname("Es");

            // Act
            Division division = new Division(
                    "ParkShark Belgium",
                    "Switch Fully",
                    director
            );

            // Assert
            assertNotNull(division);
            assertEquals("ParkShark Belgium", division.getName());
            assertEquals("Switch Fully", division.getOriginalName());
            assertEquals(director, division.getDirector());
            assertEquals("Yv", division.getDirector().getFirstname());
            assertEquals("Es", division.getDirector().getLastname());
        }
    }


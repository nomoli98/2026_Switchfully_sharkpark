package com.application.sharkpark.service;

import com.application.sharkpark.domain.User;
import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.exceptions.ForbiddenException;
import com.application.sharkpark.exceptions.UnauthorizedException;
import com.application.sharkpark.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {
    private static final Feature CREATE_MEMBER_FEATURE = Feature.CREATE_MEMBER;
    private static final String EMAIL = "test@email.com";

    @Mock
    private UserRepository userRepository;

    @Mock
    private User userMock;

    @InjectMocks
    private SecurityService securityService;

    @Test
    void unauthorized_givenANonExistingUser_thenThrowUnauthorizedException() {
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                                   securityService.validateAuthorization(EMAIL, CREATE_MEMBER_FEATURE))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void forbidden_givenAnExistingUserAndWrongAccess_thenThrowForbiddenException() {
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(userMock));
        when(userMock.canHaveAccessTo(CREATE_MEMBER_FEATURE))
                .thenReturn(false);

        assertThatThrownBy(() ->
                                   securityService.validateAuthorization(EMAIL, CREATE_MEMBER_FEATURE))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void success_givenAnExistingUserAndRightAccess_thenNoExceptionThrown() {
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(userMock));
        when(userMock.canHaveAccessTo(CREATE_MEMBER_FEATURE))
                .thenReturn(true);

        assertThatNoException()
                .isThrownBy(() ->
                                    securityService.validateAuthorization(EMAIL, CREATE_MEMBER_FEATURE));
    }
}
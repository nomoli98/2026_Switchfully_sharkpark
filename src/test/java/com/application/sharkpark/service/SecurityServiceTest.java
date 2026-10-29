package com.application.sharkpark.service;

import com.application.sharkpark.domain.member.User;
import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.exceptions.ForbiddenException;
import com.application.sharkpark.exceptions.UnauthorizedException;
import com.application.sharkpark.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {
    private static final Feature CREATE_MEMBER_FEATURE = Feature.CREATE_MEMBER;
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "password";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityService securityService;

    private User userMock;
    private String validCredentials;

    @BeforeEach
    void mockUser() {
        userMock = mock(User.class);
        validCredentials = "Basic " + Base64.getEncoder()
                .encodeToString((EMAIL +":" + PASSWORD).getBytes(StandardCharsets.UTF_8));
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    @ValueSource(strings = {"Invalid", "BasicTest", "Basic"})
    void unauthorized_givenInvalidAuthorizationFormat_thenThrowUnauthorizedException(String authorization) {
        assertThatThrownBy(() ->
                securityService.validateAuthorization(authorization, CREATE_MEMBER_FEATURE))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void unauthorized_givenInvalidCredentialsFormat_thenThrowUnauthorizedException() {
        String invalidCredentialsFormat = Base64.getEncoder()
                .encodeToString(EMAIL.getBytes(StandardCharsets.UTF_8));
        String authorization = "Basic " + invalidCredentialsFormat;

        assertThatThrownBy(() ->
                securityService.validateAuthorization(authorization, CREATE_MEMBER_FEATURE))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void unauthorized_givenANonExistingUser_thenThrowUnauthorizedException() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                securityService.validateAuthorization(validCredentials, CREATE_MEMBER_FEATURE))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void unauthorized_givenAnExistingUserAndInvalidPassword_thenThrowUnauthorizedException() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(userMock));
        when(userMock.doesPasswordMatch(any()))
                .thenReturn(false);

        assertThatThrownBy(() ->
                securityService.validateAuthorization(validCredentials, CREATE_MEMBER_FEATURE))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void forbidden_givenAValidUserAndWrongAccess_thenThrowForbiddenException() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(userMock));
        when(userMock.doesPasswordMatch(any()))
                .thenReturn(true);
        when(userMock.canHaveAccessTo(any()))
                .thenReturn(false);

        assertThatThrownBy(() -> securityService.validateAuthorization(validCredentials, CREATE_MEMBER_FEATURE))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void success_givenValidUserAndRightAccess_thenNoExceptionThrown() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(userMock));
        when(userMock.doesPasswordMatch(any()))
                .thenReturn(true);
        when(userMock.canHaveAccessTo(any()))
                .thenReturn(true);

        assertThatNoException()
                .isThrownBy(() ->
                        securityService.validateAuthorization(validCredentials, CREATE_MEMBER_FEATURE));
    }
}
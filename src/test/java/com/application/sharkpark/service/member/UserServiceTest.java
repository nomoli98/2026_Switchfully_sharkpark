package com.application.sharkpark.service.member;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateUserRequest;
import com.application.sharkpark.dto.request.RegisterMemberRequest;
import com.application.sharkpark.dto.response.UserResponse;
import com.application.sharkpark.exceptions.NotFoundException;
import com.application.sharkpark.exceptions.RequestException;
import com.application.sharkpark.mapper.UserMapper;
import com.application.sharkpark.repository.UserRepository;
import com.application.sharkpark.service.CityService;
import com.application.sharkpark.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.application.sharkpark.controller.member.UserTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CityService cityService;

    @InjectMocks
    private UserService userService;

    private City brussels;

    @BeforeEach
    void setUp() {
        brussels = brussels();
        brussels.setId(1);
    }

    /** User.getUserId() unboxes an Integer, so a saved user must have an id like JPA would give it. */
    private void stubSaveAssigningId(int id) {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setUserId(id);
            return user;
        });
    }

    // ---------- save ----------

    @Test
    @DisplayName("save with a valid request -> returns the response of the new user")
    void save_withValidRequest_returnsUserResponse() {
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(cityService.findByPostalCode(BRUSSELS_POSTAL_CODE)).thenReturn(brussels);
        stubSaveAssigningId(42);

        UserResponse response = userService.save(request().build());

        assertEquals(42, response.userId());
        assertEquals("Jane", response.firstname());
        assertEquals("Doe", response.lastname());
        assertEquals(VALID_EMAIL, response.email());
        assertEquals(VALID_PLATE, response.licensePlate());
        assertEquals("+32470123456", response.mobileNumber());
        assertEquals("021234567", response.phoneNumber());
        assertEquals(LocalDate.now(), response.registrationDate());
    }

    @Test
    @DisplayName("save -> persists a user linked to the city of the postal code")
    void save_persistsUserWithCity() {
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(cityService.findByPostalCode(BRUSSELS_POSTAL_CODE)).thenReturn(brussels);
        stubSaveAssigningId(1);

        userService.save(request().build());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("Jane", saved.getFirstname());
        assertEquals(VALID_EMAIL, saved.getEmail());
        assertEquals(VALID_PLATE, saved.getLicensePlate());
        assertSame(brussels, saved.getCity());
        assertEquals(LocalDate.now(), saved.getRegistrationDate());
    }

    @Test
    @DisplayName("save -> validates, then checks the email, then looks up the city, then saves")
    void save_executesStepsInTheExpectedOrder() {
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(cityService.findByPostalCode(BRUSSELS_POSTAL_CODE)).thenReturn(brussels);
        stubSaveAssigningId(1);

        userService.save(request().build());

        InOrder inOrder = inOrder(userRepository, cityService);
        inOrder.verify(userRepository).existsByEmail(VALID_EMAIL);
        inOrder.verify(cityService).findByPostalCode(BRUSSELS_POSTAL_CODE);
        inOrder.verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("save with an existing email -> RequestException, nothing saved")
    void save_withExistingEmail_throwsAndDoesNotSave() {
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(true);

        assertThrows(RequestException.class, () -> userService.save(request().build()));

        verify(userRepository, never()).save(any());
        verifyNoInteractions(cityService);
    }

    static Stream<Arguments> invalidRequests() {
        return Stream.of(
                arguments("firstname null", request().firstname(null)),
                arguments("firstname blank", request().firstname("  ")),
                arguments("lastname null", request().lastname(null)),
                arguments("lastname blank", request().lastname("")),
                arguments("password null", request().password(null)),
                arguments("password blank", request().password(" ")),
                arguments("email null", request().email(null)),
                arguments("email blank", request().email("")),
                arguments("email invalid", request().email("not-an-email")),
                arguments("mobile invalid", request().mobileNumber("abc")),
                arguments("phone invalid", request().phoneNumber("12")),
                arguments("no phone numbers", request().mobileNumber(null).phoneNumber(null)),
                arguments("address number negative", request().addressNumber(-5)),
                arguments("postal code negative", request().postalCode(-1)),
                arguments("license plate null", request().licensePlate(null)),
                arguments("license plate blank", request().licensePlate("  ")));
    }

    @ParameterizedTest(name = "{0} -> RequestException")
    @MethodSource("invalidRequests")
    @DisplayName("save with an invalid request -> RequestException and no repository access")
    void save_withInvalidRequest_throwsBeforeTouchingAnyDependency(String description,
                                                                   RequestBuilder builder) {
        CreateUserRequest invalid = builder.build();

        assertThrows(RequestException.class, () -> userService.save(invalid));

        verifyNoInteractions(userRepository, cityService);
    }

    @Test
    @DisplayName("save with an unknown postal code -> exception from the city service, nothing saved")
    void save_withUnknownPostalCode_propagatesAndDoesNotSave() {
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(cityService.findByPostalCode(BRUSSELS_POSTAL_CODE)).thenThrow(new RuntimeException("City not found"));

        assertThrows(RuntimeException.class, () -> userService.save(request().build()));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("save when the repository fails -> exception propagates")
    void save_whenRepositoryFails_propagatesTheException() {
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(cityService.findByPostalCode(BRUSSELS_POSTAL_CODE)).thenReturn(brussels);
        when(userRepository.save(any(User.class))).thenThrow(new IllegalStateException("db down"));

        assertThrows(IllegalStateException.class, () -> userService.save(request().build()));
    }

    @Test
    @DisplayName("save with only a mobile number -> accepted")
    void save_withOnlyMobileNumber_isAccepted() {
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(cityService.findByPostalCode(BRUSSELS_POSTAL_CODE)).thenReturn(brussels);
        stubSaveAssigningId(1);

        UserResponse response = userService.save(request().phoneNumber(null).build());

        assertNull(response.phoneNumber());
        assertEquals("+32470123456", response.mobileNumber());
    }

    @Test
    @DisplayName("save with only a phone number -> accepted")
    void save_withOnlyPhoneNumber_isAccepted() {
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(cityService.findByPostalCode(BRUSSELS_POSTAL_CODE)).thenReturn(brussels);
        stubSaveAssigningId(1);

        UserResponse response = userService.save(request().mobileNumber(null).build());

        assertNull(response.mobileNumber());
        assertEquals("021234567", response.phoneNumber());
    }

    // ---------- register ----------

    @Test
    @DisplayName("register -> converts the request and saves the user")
    void register_convertsTheRequestAndSaves() {
        RegisterMemberRequest registerRequest = mock(RegisterMemberRequest.class);
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(cityService.findByPostalCode(BRUSSELS_POSTAL_CODE)).thenReturn(brussels);
        stubSaveAssigningId(7);

        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class, CALLS_REAL_METHODS)) {
            mapper.when(() -> UserMapper.toCreateUserRequest(registerRequest))
                  .thenReturn(request().build());

            UserResponse response = userService.register(registerRequest);

            assertEquals(7, response.userId());
            assertEquals(VALID_EMAIL, response.email());
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    @DisplayName("register with an existing email -> RequestException, nothing saved")
    void register_withExistingEmail_throws() {
        RegisterMemberRequest registerRequest = mock(RegisterMemberRequest.class);
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(true);

        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class, CALLS_REAL_METHODS)) {
            mapper.when(() -> UserMapper.toCreateUserRequest(registerRequest))
                  .thenReturn(request().build());

            assertThrows(RequestException.class, () -> userService.register(registerRequest));

            verify(userRepository, never()).save(any());
        }
    }

    // ---------- findAll ----------

    @Test
    @DisplayName("findAll with no users -> empty list")
    void findAll_withNoUsers_returnsEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        assertTrue(userService.findAll().isEmpty());
    }

    @Test
    @DisplayName("findAll -> every user mapped to a response, in order")
    void findAll_returnsAllUsersAsResponses() {
        User jane = user(1, "Jane", "Doe", VALID_EMAIL, VALID_PLATE, brussels);
        User john = user(2, "John", "Smith", "john@example.com", "1-XYZ-999", brussels);
        when(userRepository.findAll()).thenReturn(List.of(jane, john));

        List<UserResponse> responses = userService.findAll();

        assertEquals(2, responses.size());
        assertEquals(1, responses.get(0).userId());
        assertEquals("Jane", responses.get(0).firstname());
        assertEquals(2, responses.get(1).userId());
        assertEquals("John", responses.get(1).firstname());
    }

    @Test
    @DisplayName("findAll -> responses never contain a password component")
    void findAll_responsesHaveNoPassword() {
        assertTrue(java.util.Arrays.stream(UserResponse.class.getRecordComponents())
                                   .noneMatch(c -> c.getName().toLowerCase().contains("password")));
    }

    // ---------- findById ----------

    @Test
    @DisplayName("findById with existing id -> response")
    void findById_withExistingId_returnsResponse() {
        when(userRepository.findById(5)).thenReturn(Optional.of(user(5, "Jane", "Doe", VALID_EMAIL, VALID_PLATE, brussels)));

        UserResponse response = userService.findById(5);

        assertEquals(5, response.userId());
        assertEquals(VALID_EMAIL, response.email());
    }

    @Test
    @DisplayName("findById with unknown id -> NotFoundException")
    void findById_withUnknownId_throwsNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findById(99));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("findById with null id -> exception from the repository propagates")
    void findById_withNullId_propagatesRepositoryException() {
        when(userRepository.findById(null)).thenThrow(new IllegalArgumentException("id must not be null"));

        assertThrows(IllegalArgumentException.class, () -> userService.findById(null));
    }

    // ---------- findByFullName ----------

    @Test
    @DisplayName("findByFullName with existing name -> response")
    void findByFullName_withExistingName_returnsResponse() {
        when(userRepository.findByFirstnameAndLastname("Jane", "Doe"))
                .thenReturn(Optional.of(user(3, "Jane", "Doe", VALID_EMAIL, VALID_PLATE, brussels)));

        UserResponse response = userService.findByFullName("Jane", "Doe");

        assertEquals(3, response.userId());
        assertEquals("Jane", response.firstname());
        assertEquals("Doe", response.lastname());
    }

    @Test
    @DisplayName("findByFullName with unknown name -> NotFoundException")
    void findByFullName_withUnknownName_throwsNotFound() {
        when(userRepository.findByFirstnameAndLastname("No", "Body")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                                                   () -> userService.findByFullName("No", "Body"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("findByFullName passes the names to the repository unchanged and in the right order")
    void findByFullName_passesArgumentsInTheRightOrder() {
        when(userRepository.findByFirstnameAndLastname("Doe", "Jane")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findByFullName("Doe", "Jane"));

        verify(userRepository).findByFirstnameAndLastname("Doe", "Jane");
        verify(userRepository, never()).findByFirstnameAndLastname("Jane", "Doe");
    }

    // ---------- existsByEmail ----------

    @Test
    @DisplayName("existsByEmail -> true when the repository says so")
    void existsByEmail_whenEmailIsKnown_returnsTrue() {
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(true);

        assertTrue(userService.existsByEmail(VALID_EMAIL));
    }

    @Test
    @DisplayName("existsByEmail -> false when the repository says so")
    void existsByEmail_whenEmailIsUnknown_returnsFalse() {
        when(userRepository.existsByEmail("other@example.com")).thenReturn(false);

        assertFalse(userService.existsByEmail("other@example.com"));
    }
}
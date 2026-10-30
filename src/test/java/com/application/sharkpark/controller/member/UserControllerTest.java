package com.application.sharkpark.controller.member;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.enums.MembershipLevel;
import com.application.sharkpark.enums.Role;
import com.application.sharkpark.repository.CityRepository;
import com.application.sharkpark.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

import static com.application.sharkpark.controller.member.UserTestData.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    private static final String USERS = "/members";
    private static final String REGISTER = "/members";
    private static final String SEARCH = "/members/by-name";

    // The GET endpoints require Basic auth with a role that has VIEW_ALL_MEMBERS.
    private static final String ADMIN_EMAIL = "admin@sharkpark.test";
    private static final String ADMIN_PASSWORD = "admin-password";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.config = RestAssuredConfig.config()
                                              .jsonConfig(JsonConfig.jsonConfig()
                                                                    .numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE));
        RestAssured.port = port;

        userRepository.deleteAll();
        cityRepository.deleteAll();
        City city = cityRepository.save(brussels());
        createAdmin(city);
    }

    private void createAdmin(City city) {
        User admin = new User();
        admin.setFirstname("Admin");
        admin.setLastname("Sharkpark");
        admin.setPassword(ADMIN_PASSWORD);
        admin.setMobileNumber("+32470000000");
        admin.setEmail(ADMIN_EMAIL);
        admin.setCity(city);
        admin.setLicensePlate("ADMIN-001");
        admin.setRegistrationDate(LocalDate.now());
        admin.setRole(Role.ADMIN);
        admin.setMembershipLevel(MembershipLevel.BRONZE);
        userRepository.save(admin);
    }

    @AfterEach
    void tearDown() {
        RestAssured.reset();
        userRepository.deleteAll();
        cityRepository.deleteAll();
    }

    // ---------- Helpers ----------

    private RequestSpecification asAdmin() {
        return given().auth().preemptive().basic(ADMIN_EMAIL, ADMIN_PASSWORD);
    }

    private int postUser(Map<String, Object> body) {
        return given().contentType(ContentType.JSON)
                      .body(body)
                      .when()
                      .post(USERS)
                      .then()
                      .statusCode(201)
                      .extract()
                      .path("userId");
    }

    private void postUserExpecting(Map<String, Object> body, int status) {
        given().contentType(ContentType.JSON)
               .body(body)
               .when()
               .post(USERS)
               .then()
               .statusCode(status);
    }

    // ---------- GET all ----------

    @Test
    @DisplayName("GetAll with no registered users -> only the admin")
    void getAllUsers_whenNoUsersRegistered_returnsOnlyTheAdmin() {
        asAdmin().when()
                 .get(USERS)
                 .then()
                 .statusCode(200)
                 .contentType(ContentType.JSON)
                 .body("", hasSize(1))
                 .body("[0].email", equalTo(ADMIN_EMAIL));
    }

    @Test
    @DisplayName("GetAll without credentials -> 401")
    void getAllUsers_withoutCredentials_returns401() {
        given().when().get(USERS).then().statusCode(401);
    }

    @Test
    @DisplayName("GetAll with wrong password -> 401")
    void getAllUsers_withWrongPassword_returns401() {
        given().auth().preemptive().basic(ADMIN_EMAIL, "wrong")
               .when().get(USERS).then().statusCode(401);
    }

    @Test
    @DisplayName("CreateUsers -> GetAll -> List of users")
    void createdUsers_appearInTheUsersList() {
        int first = postUser(validBody());

        Map<String, Object> other = validBody();
        other.put("email", "john.smith@example.com");
        other.put("licensePlate", "1-XYZ-999");

        int second = postUser(other);

        asAdmin().when()
                 .get(USERS)
                 .then()
                 .statusCode(200)
                 .body("", hasSize(3)) // admin + the two new users
                 .body("userId", hasItems(first, second));
    }

    @Test
    @DisplayName("GetAll never exposes passwords")
    void getAllUsers_doesNotExposePasswords() {
        postUser(validBody());

        asAdmin().when().get(USERS).then().statusCode(200)
                 .body("password", everyItem(nullValue()));
    }

    // ---------- POST create ----------

    @Test
    @DisplayName("CreateUser -> 201 and User")
    void createUser_withValidRequest_returns201AndTheCreatedUser() {
        given().contentType(ContentType.JSON)
               .body(validBody())
               .when()
               .post(USERS)
               .then()
               .statusCode(201)
               .body("userId", notNullValue())
               .body("firstname", equalTo("Jane"))
               .body("lastname", equalTo("Doe"))
               .body("mobileNumber", equalTo("+32470123456"))
               .body("phoneNumber", equalTo("021234567"))
               .body("email", equalTo(VALID_EMAIL))
               .body("licensePlate", equalTo(VALID_PLATE))
               .body("registrationDate", equalTo(LocalDate.now().toString()))
               .body("$", not(hasKey("password")));
    }

    @Test
    @DisplayName("CreateUser -> user is persisted with its city")
    void createUser_persistsTheUserAndLinksTheCity() {
        postUser(validBody());

        User saved = userRepository.findByEmail(VALID_EMAIL).orElseThrow();

        assertEquals("Jane", saved.getFirstname());
        assertEquals(VALID_PLATE, saved.getLicensePlate());
        assertEquals(BRUSSELS_POSTAL_CODE, saved.getCity().getPostalCode());
        assertEquals(LocalDate.now(), saved.getRegistrationDate());
    }

    @Test
    @DisplayName("CreateUser -> GetById returns the same user")
    void createdUser_canBeFetchedById() {
        int id = postUser(validBody());

        asAdmin().when()
                 .get(USERS + "/" + id)
                 .then()
                 .statusCode(200)
                 .body("userId", equalTo(id))
                 .body("email", equalTo(VALID_EMAIL));
    }

    @Test
    @DisplayName("CreateUser with only a mobile number -> 201")
    void createUser_withOnlyMobileNumber_returns201() {
        Map<String, Object> body = validBody();
        body.remove("phoneNumber");
        postUserExpecting(body, 201);
    }

    @Test
    @DisplayName("CreateUser with only a phone number -> 201")
    void createUser_withOnlyPhoneNumber_returns201() {
        Map<String, Object> body = validBody();
        body.remove("mobileNumber");
        postUserExpecting(body, 201);
    }

    @Test
    @DisplayName("CreateUser with blank mobile number but valid phone number -> 201")
    void createUser_withBlankMobileAndValidPhone_returns201() {
        postUserExpecting(bodyWith("mobileNumber", ""), 201);
    }

    @Test
    @DisplayName("CreateUser with neither mobile nor phone number -> 400")
    void createUser_withoutAnyPhoneNumber_returns400() {
        Map<String, Object> body = validBody();
        body.remove("mobileNumber");
        body.remove("phoneNumber");

        postUserExpecting(body, 400);
    }

    @Test
    @DisplayName("CreateUser with blank mobile and blank phone number -> 400")
    void createUser_withBlankMobileAndBlankPhone_returns400() {
        Map<String, Object> body = validBody();
        body.put("mobileNumber", "  ");
        body.put("phoneNumber", "");

        postUserExpecting(body, 400);
    }

    @ParameterizedTest(name = "phone number \"{0}\" -> 201")
    @MethodSource("acceptedPhoneNumbers")
    @DisplayName("Valid phone number formats -> 201")
    void createUser_withAcceptedPhoneFormat_returns201(String phone) {
        postUserExpecting(bodyWith("mobileNumber", phone), 201);
    }

    static Stream<String> acceptedPhoneNumbers() {
        return Stream.of("0470123456",
                         "+32470123456",
                         "0470 12 34 56",
                         "0470.12.34.56",
                         "0470-12-34-56");
    }

    static Stream<Arguments> invalidBodies() {
        return Stream.of(arguments("firstname missing", bodyWithout("firstname")),
                         arguments("firstname null", bodyWith("firstname", null)),
                         arguments("firstname empty", bodyWith("firstname", "")),
                         arguments("firstname blank", bodyWith("firstname", "   ")),
                         arguments("lastname missing", bodyWithout("lastname")),
                         arguments("lastname blank", bodyWith("lastname", "   ")),
                         arguments("password missing", bodyWithout("password")),
                         arguments("password blank", bodyWith("password", " ")),
                         arguments("email missing", bodyWithout("email")),
                         arguments("email blank", bodyWith("email", "  ")),
                         arguments("license plate missing", bodyWithout("licensePlate")),
                         arguments("license plate blank", bodyWith("licensePlate", " ")),
                         arguments("email without @", bodyWith("email", "jane.example.com")),
                         arguments("email without local part", bodyWith("email", "@example.com")),
                         arguments("email without domain", bodyWith("email", "jane@")),
                         arguments("email without top level domain",
                                   bodyWith("email", "jane@example")),
                         arguments("email with one letter TLD",
                                   bodyWith("email", "jane@example.c")),
                         arguments("email with double dots",
                                   bodyWith("email", "ja..ne@example.com")),
                         arguments("email with spaces", bodyWith("email", "jane doe@example.com")),
                         arguments("email with local part over 64 chars",
                                   bodyWith("email", "a".repeat(65) + "@example.com")),
                         arguments("mobile with letters", bodyWith("mobileNumber", "04abc12345")),
                         arguments("mobile too short", bodyWith("mobileNumber", "1234567")),
                         arguments("mobile too long", bodyWith("mobileNumber", "1234567890123456")),
                         arguments("mobile with slash", bodyWith("mobileNumber", "0470/123456")),
                         arguments("phone with letters", bodyWith("phoneNumber", "02abc4567")),
                         arguments("phone too short", bodyWith("phoneNumber", "123")),
                         arguments("address number negative", bodyWith("addressNumber", -1)),
                         arguments("postal code negative", bodyWith("postalCode", -1000)),
                         arguments("empty body", Map.<String, Object>of()));
    }

    @ParameterizedTest(name = "{0} -> 400")
    @MethodSource("invalidBodies")
    @DisplayName("Invalid Request fields -> 400 and nothing is saved")
    void createUser_withInvalidRequest_returns400(String description, Map<String, Object> body) {
        long before = userRepository.count();
        postUserExpecting(body, 400);
        assertEquals(before, userRepository.count());
    }

    @Test
    @DisplayName("CreateUser with an existing email -> 400, only one user stored")
    void createUser_withExistingEmail_returns400() {
        postUser(validBody());
        long before = userRepository.count();

        Map<String, Object> duplicate = bodyWith("licensePlate", "1-OTH-456");

        postUserExpecting(duplicate, 400);
        assertEquals(before, userRepository.count());
    }

    @Test
    @DisplayName("CreateUser with unknown postal code -> 404")
    void createUser_withUnknownPostalCode_returns404() {
        long before = userRepository.count();
        postUserExpecting(bodyWith("postalCode", 9999), 404);
        assertEquals(before, userRepository.count());
    }

    @Test
    @DisplayName("CreateUser with text instead of number in postal code -> 400")
    void createUser_withNonNumericPostalCode_returns400() {
        postUserExpecting(bodyWith("postalCode", "abc"), 400);
    }

    @Test
    @DisplayName("CreateUser with unsupported content type -> 415")
    void createUser_withPlainTextContentType_returns415() {
        given().contentType(ContentType.TEXT)
               .body("hello")
               .when()
               .post(USERS)
               .then()
               .statusCode(415);
    }

    @Test
    @DisplayName("CreateUser without body -> 400")
    void createUser_withoutBody_returns400() {
        given().contentType(ContentType.JSON).when().post(USERS).then().statusCode(400);
    }

    // ---------- POST register ----------

    @Test
    @DisplayName("Register -> 201 and user is stored")
    void register_withValidRequest_returns201() {
        given().contentType(ContentType.JSON)
               .body(validBody())
               .when()
               .post(REGISTER)
               .then()
               .statusCode(201)
               .body("email", equalTo(VALID_EMAIL));

        assertTrue(userRepository.findByEmail(VALID_EMAIL).isPresent());
    }

    @Test
    @DisplayName("Register with an existing email -> 400")
    void register_withExistingEmail_returns400() {
        postUser(validBody());

        given().contentType(ContentType.JSON)
               .body(bodyWith("licensePlate", "1-OTH-456"))
               .when()
               .post(REGISTER)
               .then()
               .statusCode(400);
    }

    @Test
    @DisplayName("Register with invalid email -> 400")
    void register_withInvalidEmail_returns400() {
        given().contentType(ContentType.JSON)
               .body(bodyWith("email", "nope"))
               .when()
               .post(REGISTER)
               .then()
               .statusCode(400);
    }

    // ---------- GET by id ----------

    @Test
    @DisplayName("GetById with unknown id -> 404")
    void getUserById_withUnknownId_returns404() {
        asAdmin().when().get(USERS + "/99999").then().statusCode(404);
    }

    @Test
    @DisplayName("GetById with a non numeric id -> 400")
    void getUserById_withNonNumericId_returns400() {
        asAdmin().when().get(USERS + "/abc").then().statusCode(400);
    }

    @Test
    @DisplayName("GetById never exposes the password")
    void getUserById_doesNotExposePassword() {
        int id = postUser(validBody());

        asAdmin().when()
                 .get(USERS + "/" + id)
                 .then()
                 .statusCode(200)
                 .body("$", not(hasKey("password")));
    }

    // ---------- GET by full name ----------

    @Test
    @DisplayName("GetByFullName -> user")
    void getUserByFullName_returnsTheMatchingUser() {
        int id = postUser(validBody());

        asAdmin().when().get(SEARCH + "/Jane/Doe").then().statusCode(200).body("userId", equalTo(id));
    }

    @Test
    @DisplayName("GetByFullName with wrong lastname -> 404")
    void getUserByFullName_withWrongLastname_returns404() {
        postUser(validBody());

        asAdmin().when().get(SEARCH + "/Jane/Smith").then().statusCode(404);
    }

    @Test
    @DisplayName("GetByFullName with unknown name -> 404")
    void getUserByFullName_withUnknownName_returns404() {
        asAdmin().when().get(SEARCH + "/Nobody/Here").then().statusCode(404);
    }

    @Test
    @DisplayName("GetByFullName without parameters -> 404")
    void getUserByFullName_withoutParams_returns404() {
        asAdmin().when().get(SEARCH).then().statusCode(404);
    }

    @Test
    @DisplayName("GetByFullName without lastname -> 404")
    void getUserByFullName_withoutLastname_returns404() {
        asAdmin().when().get(SEARCH + "/Jane").then().statusCode(404);
    }
}
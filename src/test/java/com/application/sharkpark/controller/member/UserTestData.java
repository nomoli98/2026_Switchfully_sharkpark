package com.application.sharkpark.controller.member;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateUserRequest;
import com.application.sharkpark.enums.MembershipLevel;
import com.application.sharkpark.enums.Role;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public final class UserTestData {

    public static final int BRUSSELS_POSTAL_CODE = 1000;
    public static final String VALID_EMAIL = "jane.doe@example.com";
    public static final String VALID_PLATE = "1-ABC-123";

    private UserTestData() {
    }

    // ---------- entities ----------

    public static City brussels() {
        City city = new City();
        city.setName("Brussels");
        city.setPostalCode(BRUSSELS_POSTAL_CODE);
        return city;
    }

    public static User user(int id, String firstname, String lastname, String email, String plate, City city) {
        User user = new User();
        user.setUserId(id);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPassword("secret123");
        user.setMobileNumber("+32470123456");
        user.setPhoneNumber("021234567");
        user.setEmail(email);
        user.setAddressStreet("Main Street");
        user.setAddressNumber(12);
        user.setCity(city);
        user.setLicensePlate(plate);
        user.setRegistrationDate(LocalDate.now());
        user.setRole(Role.MEMBER);
        user.setMembershipLevel(MembershipLevel.BRONZE);
        return user;
    }

    /**
     * A user ready to be persisted (no id) with the given role. Every NOT NULL column is filled in,
     * including membership, so integration tests can save admins and managers directly.
     */
    public static User persistableUser(Role role, String email, String password, String plate, City city) {
        User user = new User();
        user.setFirstname(role.name().charAt(0) + role.name().substring(1).toLowerCase());
        user.setLastname("Sharkpark");
        user.setPassword(password);
        user.setMobileNumber("+32470000000");
        user.setEmail(email);
        user.setCity(city);
        user.setLicensePlate(plate);
        user.setRegistrationDate(LocalDate.now());
        user.setRole(role);
        user.setMembershipLevel(MembershipLevel.BRONZE);
        return user;
    }

    // ---------- JSON bodies (used by the integration tests) ----------

    public static Map<String, Object> validBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("firstname", "Jane");
        body.put("lastname", "Doe");
        body.put("password", "secret123");
        body.put("mobileNumber", "+32470123456");
        body.put("phoneNumber", "021234567");
        body.put("email", VALID_EMAIL);
        body.put("addressStreet", "Main Street");
        body.put("addressNumber", 12);
        body.put("postalCode", BRUSSELS_POSTAL_CODE);
        body.put("licensePlate", VALID_PLATE);
        return body;
    }

    public static Map<String, Object> bodyWith(String key, Object value) {
        Map<String, Object> body = validBody();
        body.put(key, value);
        return body;
    }

    public static Map<String, Object> bodyWithout(String key) {
        Map<String, Object> body = validBody();
        body.remove(key);
        return body;
    }

    // ---------- CreateUserRequest builder (used by the unit tests) ----------

    public static RequestBuilder request() {
        return new RequestBuilder();
    }

    public static final class RequestBuilder {
        private String firstname = "Jane";
        private String lastname = "Doe";
        private String password = "secret123";
        private String mobileNumber = "+32470123456";
        private String phoneNumber = "021234567";
        private String email = VALID_EMAIL;
        private String addressStreet = "Main Street";
        private Integer addressNumber = 12;
        private Integer postalCode = BRUSSELS_POSTAL_CODE;
        private String licensePlate = VALID_PLATE;
        private LocalDate registrationDate = null;
        private Role role = Role.MEMBER;
        private MembershipLevel membershipLevel = MembershipLevel.BRONZE;

        public RequestBuilder firstname(String v) { this.firstname = v; return this; }
        public RequestBuilder lastname(String v) { this.lastname = v; return this; }
        public RequestBuilder password(String v) { this.password = v; return this; }
        public RequestBuilder mobileNumber(String v) { this.mobileNumber = v; return this; }
        public RequestBuilder phoneNumber(String v) { this.phoneNumber = v; return this; }
        public RequestBuilder email(String v) { this.email = v; return this; }
        public RequestBuilder addressStreet(String v) { this.addressStreet = v; return this; }
        public RequestBuilder addressNumber(Integer v) { this.addressNumber = v; return this; }
        public RequestBuilder postalCode(Integer v) { this.postalCode = v; return this; }
        public RequestBuilder licensePlate(String v) { this.licensePlate = v; return this; }
        public RequestBuilder registrationDate(LocalDate v) { this.registrationDate = v; return this; }
        public RequestBuilder role(Role v) { this.role = v; return this; }
        public RequestBuilder membershipLevel(MembershipLevel v) { this.membershipLevel = v; return this; }

        public CreateUserRequest build() {
            return new CreateUserRequest(firstname, lastname, password, mobileNumber, phoneNumber,
                                         email, addressStreet, addressNumber, postalCode, licensePlate,
                                         registrationDate, role, membershipLevel);
        }
    }
}
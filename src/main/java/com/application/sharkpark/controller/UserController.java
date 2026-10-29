package com.application.sharkpark.controller;

import com.application.sharkpark.dto.request.CreateUserRequest;
import com.application.sharkpark.dto.response.UserResponse;
import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.service.SecurityService;
import com.application.sharkpark.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("members")
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "Missing or invalid credentials",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(type = "object",
                                         example = "{\"message\": \"Unauthorized\"}")
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "User does not have access to this feature",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(type = "object", example = "{\"message\": \"Forbidden\"}")
                )
        )
})
public class UserController {
    private final UserService userService;
    private final SecurityService securityService;

    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @Operation(summary = "Get all users")
    @ApiResponse(
            responseCode = "200",
            description = "List of users",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
            )
    )
    @GetMapping
    public List<UserResponse> getAll(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            @Parameter(hidden = true) String authorization) {
        securityService.validateAuthorization(authorization, Feature.VIEW_ALL_MEMBERS);
        return userService.findAll();
    }

    @Operation(summary = "Get user by id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "object",
                                             example = "{\"message\": \"User not found\"}")
                    )
            )
    })
    @GetMapping("{id}")
    public UserResponse getById(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                @Parameter(hidden = true) String authorization,
                                @PathVariable("id") Integer memberId) {
        securityService.validateAuthorization(authorization, Feature.VIEW_ALL_MEMBERS);
        return userService.findById(memberId);
    }

    @Operation(summary = "Get user by name")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "object",
                                             example = "{\"message\": \"User not found\"}")
                    )
            )
    })
    @GetMapping("by-name/{firstname}/{lastname}")
    public UserResponse getByFullName(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            @Parameter(hidden = true) String authorization,
            @PathVariable("firstname") String firstname,
            @PathVariable("lastname") String lastname) {
        securityService.validateAuthorization(authorization, Feature.VIEW_ALL_MEMBERS);
        return userService.findByFullName(firstname, lastname);
    }

    // "/members/by-name" without both names would otherwise hit "{id}" and fail as a bad id (400).
    // Literal paths win over "{id}", so this returns a clean 404 instead.
    @GetMapping({"by-name", "by-name/{firstname}"})
    public UserResponse getByFullNameIncomplete() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "object", example = "{\"message\": \"Invalid request\"}")
                    )
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody CreateUserRequest request) {
        return userService.save(request);
    }
}
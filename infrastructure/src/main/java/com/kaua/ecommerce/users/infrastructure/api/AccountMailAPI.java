package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.infrastructure.accounts.models.RequestResetPasswordApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.ResetPasswordApiInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account Mail")
@RequestMapping(value = "accounts")
public interface AccountMailAPI {

    @PatchMapping(
            value = "/confirm/{token}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Confirm an account by it's token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account confirmed successfully"),
            @ApiResponse(responseCode = "404", description = "Token was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> confirmAccount(@PathVariable String token);

    @PostMapping(
            value = "/confirm/{accountId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new confirmation token for an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "404", description = "Account was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> createAccountConfirmation(@PathVariable String accountId);

    @PostMapping(
            value = "/request-reset-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new reset password token for an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "404", description = "Account was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> requestResetPassword(@RequestBody RequestResetPasswordApiInput input);

    @PatchMapping(
            value = "/reset-password/{token}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Reset an account password by it's token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Password reset successfully"),
            @ApiResponse(responseCode = "404", description = "Token was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> resetPassword(@RequestBody ResetPasswordApiInput input, @PathVariable String token);
}

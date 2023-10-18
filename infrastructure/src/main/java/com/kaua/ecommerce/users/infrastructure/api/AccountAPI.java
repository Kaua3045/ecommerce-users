package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.GetAccountPresenter;
import com.kaua.ecommerce.users.infrastructure.accounts.models.ListAccountsPresenter;
import com.kaua.ecommerce.users.infrastructure.accounts.models.UpdateAccountRoleApiInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Account")
@RequestMapping(value = "accounts")
public interface AccountAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> createAccount(@RequestBody CreateAccountApiInput input);

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a account by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    GetAccountPresenter getAccount(@PathVariable String id);

    @GetMapping
    @Operation(summary = "List all accounts paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    Pagination<ListAccountsPresenter> listAccounts(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "firstName") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @PatchMapping(
            value = "{id}/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a avatar by account it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Account was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updateAccountAvatar(
            @PathVariable String id,
            @RequestParam("avatar") MultipartFile avatarFile
    );

    @PatchMapping(
            value = "{id}/role",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a account role by account it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Account or Role was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updateAccountRole(
            @PathVariable String id,
            @RequestBody UpdateAccountRoleApiInput input
    );

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a account by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> deleteAccount(@PathVariable String id);
}

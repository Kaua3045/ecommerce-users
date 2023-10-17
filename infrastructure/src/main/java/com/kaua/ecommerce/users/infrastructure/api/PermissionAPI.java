package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.infrastructure.permissions.models.CreatePermissionApiInput;
import com.kaua.ecommerce.users.infrastructure.permissions.models.GetPermissionResponse;
import com.kaua.ecommerce.users.infrastructure.permissions.models.ListPermissionResponse;
import com.kaua.ecommerce.users.infrastructure.permissions.models.UpdatePermissionApiInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Permission")
@RequestMapping(value = "permissions")
public interface PermissionAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> createPermission(@RequestBody CreatePermissionApiInput input);

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a permissions by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permission retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Permission was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    GetPermissionResponse getPermission(@PathVariable String id);

    @GetMapping
    @Operation(summary = "List all permissions paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    Pagination<ListPermissionResponse> listPermissions(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @PatchMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a permission by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Permission was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updatePermission(
            @PathVariable String id,
            @RequestBody UpdatePermissionApiInput input
    );

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a permission by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permission deleted successfully"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> deletePermission(@PathVariable String id);
}

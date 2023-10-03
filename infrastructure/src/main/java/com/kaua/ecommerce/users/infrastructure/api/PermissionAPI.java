package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.infrastructure.permissions.models.CreatePermissionApiInput;
import com.kaua.ecommerce.users.infrastructure.permissions.models.GetPermissionResponse;
import com.kaua.ecommerce.users.infrastructure.permissions.models.ListPermissionResponse;
import com.kaua.ecommerce.users.infrastructure.permissions.models.UpdatePermissionApiInput;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "permissions")
public interface PermissionAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> createPermission(@RequestBody CreatePermissionApiInput input);

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    GetPermissionResponse getPermission(@PathVariable String id);

    @GetMapping
    Pagination<ListPermissionResponse> listPermissions(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @PatchMapping("{id}")
    ResponseEntity<?> updatePermission(
            @PathVariable String id,
            @RequestBody UpdatePermissionApiInput input
    );

    @DeleteMapping("{id}")
    ResponseEntity<?> deletePermission(@PathVariable String id);
}

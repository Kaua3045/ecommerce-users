package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.infrastructure.permissions.models.CreatePermissionApiInput;
import com.kaua.ecommerce.users.infrastructure.permissions.models.GetPermissionResponse;
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

    @DeleteMapping("{id}")
    ResponseEntity<?> deletePermission(@PathVariable String id);
}

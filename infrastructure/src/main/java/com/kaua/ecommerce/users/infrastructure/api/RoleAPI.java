package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.infrastructure.roles.models.CreateRoleApiInput;
import com.kaua.ecommerce.users.infrastructure.roles.models.GetRoleOutput;
import com.kaua.ecommerce.users.infrastructure.roles.models.UpdateRoleApiInput;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "roles")
public interface RoleAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> createRole(@RequestBody CreateRoleApiInput input);

    @PatchMapping("{id}")
    ResponseEntity<?> updateRole(
            @PathVariable String id,
            @RequestBody UpdateRoleApiInput input
    );

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    GetRoleOutput getRole(@PathVariable String id);

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteRole(@PathVariable String id);
}

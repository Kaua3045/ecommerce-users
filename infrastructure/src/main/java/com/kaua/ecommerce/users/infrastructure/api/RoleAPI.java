package com.kaua.ecommerce.users.infrastructure.api;

import com.kaua.ecommerce.users.infrastructure.roles.models.CreateRoleApiInput;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "roles")
public interface RoleAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> createRole(@RequestBody CreateRoleApiInput input);
}

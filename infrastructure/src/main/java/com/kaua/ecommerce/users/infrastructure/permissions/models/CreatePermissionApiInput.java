package com.kaua.ecommerce.users.infrastructure.permissions.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreatePermissionApiInput(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description
) {
}

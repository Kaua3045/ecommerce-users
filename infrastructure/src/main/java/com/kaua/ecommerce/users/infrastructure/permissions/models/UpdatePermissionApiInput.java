package com.kaua.ecommerce.users.infrastructure.permissions.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdatePermissionApiInput(
        @JsonProperty("description") String description
) {
}

package com.kaua.ecommerce.users.infrastructure.roles.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateRoleApiInput(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("role_type") String roleType
) {
}
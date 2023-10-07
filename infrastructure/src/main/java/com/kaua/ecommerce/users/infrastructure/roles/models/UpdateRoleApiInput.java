package com.kaua.ecommerce.users.infrastructure.roles.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record UpdateRoleApiInput(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("role_type") String roleType,
        @JsonProperty("is_default") boolean isDefault,
        @JsonProperty("permissions") Set<String> permissions
) {
}

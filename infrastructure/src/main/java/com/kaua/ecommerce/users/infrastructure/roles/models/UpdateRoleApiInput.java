package com.kaua.ecommerce.users.infrastructure.roles.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UpdateRoleApiInput(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("role_type") String roleType,
        @JsonProperty("is_default") boolean isDefault,
        @JsonProperty("permissions") List<String> permissions
) {
}

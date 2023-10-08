package com.kaua.ecommerce.users.infrastructure.roles.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record GetRoleResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("role_type") String roleType,
        @JsonProperty("is_default") boolean isDefault,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("permissions") Set<String> permissions
) {
}

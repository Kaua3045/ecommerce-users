package com.kaua.ecommerce.users.infrastructure.roles.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ListRoleResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("role_type") String roleType,
        @JsonProperty("created_at") String createdAt
) {
}

package com.kaua.ecommerce.users.infrastructure.roles.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetRoleOutput(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("role_type") String roleType,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt
) {
}

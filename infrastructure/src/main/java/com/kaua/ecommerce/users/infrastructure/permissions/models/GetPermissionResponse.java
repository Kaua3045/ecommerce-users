package com.kaua.ecommerce.users.infrastructure.permissions.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetPermissionResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt
) {
}

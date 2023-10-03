package com.kaua.ecommerce.users.infrastructure.permissions.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ListPermissionResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("created_at") String createdAt
) {
}

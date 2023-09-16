package com.kaua.ecommerce.users.infrastructure.accounts.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetAccountPresenter(
        @JsonProperty("id") String id,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("email") String email,
        @JsonProperty("avatar_url") String avatarUrl,
        @JsonProperty("mail_status") String mailStatus,
        @JsonProperty("role_id") String roleId,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt
) {
}

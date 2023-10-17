package com.kaua.ecommerce.users.infrastructure.accounts.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ListAccountsPresenter(
        @JsonProperty("id") String id,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("email") String email,
        @JsonProperty("created_at") String createdAt
) {
}

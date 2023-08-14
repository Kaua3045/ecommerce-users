package com.kaua.ecommerce.users.infrastructure.accounts.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAccountApiInput(
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name")String lastName,
        String email,
        String password
) {
}

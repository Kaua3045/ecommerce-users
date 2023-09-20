package com.kaua.ecommerce.users.infrastructure.accounts.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateAccountRoleApiInput(
        @JsonProperty("role_id") String roleId
) {
}

package com.kaua.ecommerce.users.infrastructure.roles.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RemoveRolePermissionApiInput(
        @JsonProperty("permission_name") String permissionName
) {
}

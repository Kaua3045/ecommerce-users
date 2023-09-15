package com.kaua.ecommerce.users.application.role.retrieve.list;

import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleID;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;

import java.time.Instant;

public record ListRolesOutput(
        RoleID id,
        String name,
        String description,
        RoleTypes type,
        Instant createdAt
) {

    public static ListRolesOutput from(final Role aRole) {
        return new ListRolesOutput(
                aRole.getId(),
                aRole.getName(),
                aRole.getDescription(),
                aRole.getRoleType(),
                aRole.getCreatedAt());
    }
}

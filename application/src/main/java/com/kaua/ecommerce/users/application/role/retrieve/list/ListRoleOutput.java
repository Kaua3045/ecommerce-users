package com.kaua.ecommerce.users.application.role.retrieve.list;

import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleID;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;

import java.time.Instant;

public record ListRoleOutput(
        RoleID id,
        String name,
        String description,
        RoleTypes type,
        Instant createdAt
) {

    public static ListRoleOutput from(final Role aRole) {
        return new ListRoleOutput(
                aRole.getId(),
                aRole.getName(),
                aRole.getDescription(),
                aRole.getRoleType(),
                aRole.getCreatedAt());
    }
}

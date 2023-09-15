package com.kaua.ecommerce.users.application.role.retrieve.list;

import com.kaua.ecommerce.users.domain.roles.RoleID;

import java.time.Instant;

public record ListRoleOutput(
        RoleID id,
        String name,
        String description,
        String type,
        Instant createdAt,
        Instant updatedAt
) {
}

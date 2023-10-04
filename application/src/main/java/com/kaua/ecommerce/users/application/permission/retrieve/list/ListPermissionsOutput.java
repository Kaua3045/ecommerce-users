package com.kaua.ecommerce.users.application.permission.retrieve.list;

import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.permissions.PermissionID;

import java.time.Instant;

public record ListPermissionsOutput(
        PermissionID id,
        String name,
        String description,
        Instant createdAt
) {

    public static ListPermissionsOutput from(final Permission aPermission) {
        return new ListPermissionsOutput(
                aPermission.getId(),
                aPermission.getName(),
                aPermission.getDescription(),
                aPermission.getCreatedAt());
    }
}

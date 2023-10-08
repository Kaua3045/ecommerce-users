package com.kaua.ecommerce.users.application.role.retrieve.get;

import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RolePermission;

import java.util.Set;
import java.util.stream.Collectors;

public record GetRoleByIdOutput(
        String id,
        String name,
        String description,
        String roleType,
        boolean isDefault,
        String createdAt,
        String updatedAt,
        Set<String> permissions
) {

    public static GetRoleByIdOutput from(final Role aRole) {
        return new GetRoleByIdOutput(
                aRole.getId().getValue(),
                aRole.getName(),
                aRole.getDescription(),
                aRole.getRoleType().name(),
                aRole.isDefault(),
                aRole.getCreatedAt().toString(),
                aRole.getUpdatedAt().toString(),
                aRole.getPermissions()
                        .stream()
                        .map(RolePermission::getPermissionName)
                        .collect(Collectors.toSet())
        );
    }
}

package com.kaua.ecommerce.users.application.role.retrieve.get;

import com.kaua.ecommerce.users.domain.roles.Role;

public record GetRoleByIdOutput(
        String id,
        String name,
        String description,
        String roleType,
        boolean isDefault,
        String createdAt,
        String updatedAt
) {

    public static GetRoleByIdOutput from(final Role aRole) {
        return new GetRoleByIdOutput(
                aRole.getId().getValue(),
                aRole.getName(),
                aRole.getDescription(),
                aRole.getRoleType().name(),
                aRole.isDefault(),
                aRole.getCreatedAt().toString(),
                aRole.getUpdatedAt().toString()
        );
    }
}

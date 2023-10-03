package com.kaua.ecommerce.users.application.permission.retrieve.get;

import com.kaua.ecommerce.users.domain.permissions.Permission;

public record GetPermissionByIdOutput(
        String id,
        String name,
        String description,
        String createdAt,
        String updatedAt
) {

    public static GetPermissionByIdOutput from(final Permission aPermission) {
        return new GetPermissionByIdOutput(
                aPermission.getId().getValue(),
                aPermission.getName(),
                aPermission.getDescription(),
                aPermission.getCreatedAt().toString(),
                aPermission.getUpdatedAt().toString()
        );
    }
}

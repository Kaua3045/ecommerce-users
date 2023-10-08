package com.kaua.ecommerce.users.application.usecases.permission.create;

import com.kaua.ecommerce.users.domain.permissions.Permission;

public record CreatePermissionOutput(String id) {

    public static CreatePermissionOutput from(final String id) {
        return new CreatePermissionOutput(id);
    }

    public static CreatePermissionOutput from(final Permission aPermission) {
        return new CreatePermissionOutput(aPermission.getId().getValue());
    }
}

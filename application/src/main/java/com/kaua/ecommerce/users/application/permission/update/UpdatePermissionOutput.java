package com.kaua.ecommerce.users.application.permission.update;

import com.kaua.ecommerce.users.domain.permissions.Permission;

public record UpdatePermissionOutput(String id) {

    public static UpdatePermissionOutput from(final Permission aPermission) {
        return new UpdatePermissionOutput(aPermission.getId().getValue());
    }
}

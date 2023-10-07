package com.kaua.ecommerce.users.domain.roles;

import com.kaua.ecommerce.users.domain.ValueObject;
import com.kaua.ecommerce.users.domain.permissions.PermissionID;

public class RolePermission extends ValueObject {

    private PermissionID permissionID;
    private String permissionName;

    private RolePermission(final PermissionID aPermissionID, final String aPermissionName) {
        this.permissionID = aPermissionID;
        this.permissionName = aPermissionName;
    }

    public static RolePermission newRolePermission(
            final PermissionID aPermissionID,
            final String aPermissionName
    ) {
        return new RolePermission(aPermissionID, aPermissionName);
    }

    public PermissionID getPermissionID() {
        return permissionID;
    }

    public String getPermissionName() {
        return permissionName;
    }
}

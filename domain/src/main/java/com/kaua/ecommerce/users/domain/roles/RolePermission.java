package com.kaua.ecommerce.users.domain.roles;

import com.kaua.ecommerce.users.domain.ValueObject;
import com.kaua.ecommerce.users.domain.permissions.PermissionID;
import com.kaua.ecommerce.users.domain.utils.Generated;

import java.util.Objects;

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

    @Generated
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RolePermission that = (RolePermission) o;
        return Objects.equals(getPermissionID(), that.getPermissionID()) && Objects.equals(getPermissionName(), that.getPermissionName());
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(getPermissionID(), getPermissionName());
    }
}

package com.kaua.ecommerce.users.infrastructure.roles.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RolePermissionID implements Serializable {

    @Column(name = "role_id", nullable = false)
    private String roleId;

    @Column(name = "permission_id", nullable = false)
    private String permissionId;

    public RolePermissionID() {}

    private RolePermissionID(final String roleId, final String permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    public static RolePermissionID from(final String roleId, final String permissionId) {
        return new RolePermissionID(roleId, permissionId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RolePermissionID that = (RolePermissionID) o;
        return getRoleId().equals(that.getRoleId()) && getPermissionId().equals(that.getPermissionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getPermissionId());
    }

    public String getRoleId() {
        return roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }
}

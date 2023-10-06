package com.kaua.ecommerce.users.infrastructure.roles.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

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

    public String getRoleId() {
        return roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }
}

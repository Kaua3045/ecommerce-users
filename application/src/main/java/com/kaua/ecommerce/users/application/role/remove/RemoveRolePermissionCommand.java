package com.kaua.ecommerce.users.application.role.remove;

public record RemoveRolePermissionCommand(String id, String permissionId) {

    public static RemoveRolePermissionCommand with(final String aId, final String aPermissionId) {
        return new RemoveRolePermissionCommand(aId, aPermissionId);
    }
}

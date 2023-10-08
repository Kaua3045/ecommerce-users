package com.kaua.ecommerce.users.application.role.remove;

public record RemoveRolePermissionCommand(String id, String permissionName) {

    public static RemoveRolePermissionCommand with(final String aId, final String aPermissionName) {
        return new RemoveRolePermissionCommand(aId, aPermissionName);
    }
}

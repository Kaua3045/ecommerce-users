package com.kaua.ecommerce.users.application.permission.update;

public record UpdatePermissionCommand(
        String id,
        String name,
        String description
) {

    public static UpdatePermissionCommand with(String id, String name, String description) {
        return new UpdatePermissionCommand(id, name, description);
    }
}

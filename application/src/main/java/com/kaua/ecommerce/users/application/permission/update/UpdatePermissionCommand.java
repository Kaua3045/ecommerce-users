package com.kaua.ecommerce.users.application.permission.update;

public record UpdatePermissionCommand(
        String id,
        String description
) {

    public static UpdatePermissionCommand with(String id, String description) {
        return new UpdatePermissionCommand(id, description);
    }
}

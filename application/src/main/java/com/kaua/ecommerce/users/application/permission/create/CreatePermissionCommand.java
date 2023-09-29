package com.kaua.ecommerce.users.application.permission.create;

public record CreatePermissionCommand(String name, String description) {

    public static CreatePermissionCommand with(String name, String description) {
        return new CreatePermissionCommand(name, description);
    }
}

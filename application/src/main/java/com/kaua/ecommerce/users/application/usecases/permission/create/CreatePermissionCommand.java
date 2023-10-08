package com.kaua.ecommerce.users.application.usecases.permission.create;

public record CreatePermissionCommand(String name, String description) {

    public static CreatePermissionCommand with(final String name, final String description) {
        return new CreatePermissionCommand(name, description);
    }
}

package com.kaua.ecommerce.users.application.usecases.permission.delete;

public record DeletePermissionCommand(String id) {

    public static DeletePermissionCommand with(final String aId) {
        return new DeletePermissionCommand(aId);
    }
}

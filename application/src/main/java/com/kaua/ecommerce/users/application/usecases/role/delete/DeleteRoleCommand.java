package com.kaua.ecommerce.users.application.usecases.role.delete;

public record DeleteRoleCommand(String id) {

    public static DeleteRoleCommand with(final String aId) {
        return new DeleteRoleCommand(aId);
    }
}

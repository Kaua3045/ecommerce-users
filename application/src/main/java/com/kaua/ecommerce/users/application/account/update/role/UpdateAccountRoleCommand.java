package com.kaua.ecommerce.users.application.account.update.role;

public record UpdateAccountRoleCommand(
        String id,
        String roleId
) {

    public static UpdateAccountRoleCommand with(final String id, final String roleId) {
        return new UpdateAccountRoleCommand(id, roleId);
    }
}

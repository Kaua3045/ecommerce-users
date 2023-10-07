package com.kaua.ecommerce.users.application.role.create;

import java.util.List;

public record CreateRoleCommand(
        String name,
        String description,
        String roleType,
        boolean isDefault,
        List<String> permissions
) {

    public static CreateRoleCommand with(
            final String aName,
            final String aDescription,
            final String aRoleType,
            final boolean isDefault,
            final List<String> aPermissions
    ) {
        return new CreateRoleCommand(aName, aDescription, aRoleType, isDefault, aPermissions);
    }
}

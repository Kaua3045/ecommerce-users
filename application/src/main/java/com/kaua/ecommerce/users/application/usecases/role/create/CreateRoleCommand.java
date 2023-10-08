package com.kaua.ecommerce.users.application.usecases.role.create;

import java.util.Set;

public record CreateRoleCommand(
        String name,
        String description,
        String roleType,
        boolean isDefault,
        Set<String> permissions
) {

    public static CreateRoleCommand with(
            final String aName,
            final String aDescription,
            final String aRoleType,
            final boolean isDefault,
            final Set<String> aPermissions
    ) {
        return new CreateRoleCommand(aName, aDescription, aRoleType, isDefault, aPermissions);
    }
}

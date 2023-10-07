package com.kaua.ecommerce.users.application.role.update;

import java.util.Set;

public record UpdateRoleCommand(
        String id,
        String name,
        String description,
        String roleType,
        boolean isDefault,
        Set<String> permissions
) {

    public static UpdateRoleCommand with(
            final String aId,
            final String aName,
            final String aDescription,
            final String aRoleType,
            final boolean isDefault,
            final Set<String> aPermissions
    ) {
        return new UpdateRoleCommand(aId, aName, aDescription, aRoleType, isDefault, aPermissions);
    }
}

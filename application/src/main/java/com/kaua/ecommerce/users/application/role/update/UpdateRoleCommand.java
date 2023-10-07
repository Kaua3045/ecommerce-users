package com.kaua.ecommerce.users.application.role.update;

import java.util.List;

public record UpdateRoleCommand(
        String id,
        String name,
        String description,
        String roleType,
        boolean isDefault,
        List<String> permissions
) {

    public static UpdateRoleCommand with(
            final String aId,
            final String aName,
            final String aDescription,
            final String aRoleType,
            final boolean isDefault,
            final List<String> aPermissions
    ) {
        return new UpdateRoleCommand(aId, aName, aDescription, aRoleType, isDefault, aPermissions);
    }
}

package com.kaua.ecommerce.users.application.role.update;

public record UpdateRoleCommand(
        String id,
        String name,
        String description,
        String roleType
) {

    public static UpdateRoleCommand with(
            final String aId,
            final String aName,
            final String aDescription,
            final String aRoleType
    ) {
        return new UpdateRoleCommand(aId, aName, aDescription, aRoleType);
    }
}

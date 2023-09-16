package com.kaua.ecommerce.users.application.role.create;

public record CreateRoleCommand(
        String name,
        String description,
        String roleType,
        boolean isDefault
) {

    public static CreateRoleCommand with(
            final String aName,
            final String aDescription,
            final String aRoleType,
            final boolean isDefault
    ) {
        return new CreateRoleCommand(aName, aDescription, aRoleType, isDefault);
    }
}

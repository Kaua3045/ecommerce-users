package com.kaua.ecommerce.users.application.role.create;

public record CreateRoleCommand(
        String name,
        String description,
        String roleType
) {

    public static CreateRoleCommand with(
            final String aName,
            final String aDescription,
            final String aRoleType
    ) {
        return new CreateRoleCommand(aName, aDescription, aRoleType);
    }
}

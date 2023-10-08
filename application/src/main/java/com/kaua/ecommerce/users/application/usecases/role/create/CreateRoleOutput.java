package com.kaua.ecommerce.users.application.usecases.role.create;

import com.kaua.ecommerce.users.domain.roles.Role;

public record CreateRoleOutput(String id) {

    public static CreateRoleOutput from(final Role aRole) {
        return new CreateRoleOutput(aRole.getId().getValue());
    }

    public static CreateRoleOutput from(final String aId) {
        return new CreateRoleOutput(aId);
    }
}

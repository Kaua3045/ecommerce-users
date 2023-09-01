package com.kaua.ecommerce.users.application.role.create;

import com.kaua.ecommerce.users.domain.roles.Role;

public record CreateRoleOutput(String id) {

    public static CreateRoleOutput from(final Role aRole) {
        return new CreateRoleOutput(aRole.getId().getValue());
    }
}

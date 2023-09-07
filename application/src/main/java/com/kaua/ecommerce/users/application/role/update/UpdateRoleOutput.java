package com.kaua.ecommerce.users.application.role.update;

import com.kaua.ecommerce.users.domain.roles.Role;

public record UpdateRoleOutput(String id) {

    public static UpdateRoleOutput from(final Role aRole) {
        return new UpdateRoleOutput(aRole.getId().getValue());
    }

    public static UpdateRoleOutput from(final String aId) {
        return new UpdateRoleOutput(aId);
    }
}

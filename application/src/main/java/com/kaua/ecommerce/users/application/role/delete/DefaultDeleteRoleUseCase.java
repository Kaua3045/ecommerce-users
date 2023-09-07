package com.kaua.ecommerce.users.application.role.delete;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;

import java.util.Objects;

public class DefaultDeleteRoleUseCase extends DeleteRoleUseCase {

    private final RoleGateway roleGateway;

    public DefaultDeleteRoleUseCase(final RoleGateway roleGateway) {
        this.roleGateway = Objects.requireNonNull(roleGateway);
    }

    @Override
    public void execute(DeleteRoleCommand input) {
        this.roleGateway.deleteById(input.id());
    }
}

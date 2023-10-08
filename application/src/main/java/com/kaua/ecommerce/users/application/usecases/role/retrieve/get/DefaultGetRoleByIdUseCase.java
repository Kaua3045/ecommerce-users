package com.kaua.ecommerce.users.application.usecases.role.retrieve.get;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.Role;

import java.util.Objects;

public class DefaultGetRoleByIdUseCase extends GetRoleByIdUseCase {

    private final RoleGateway roleGateway;

    public DefaultGetRoleByIdUseCase(final RoleGateway roleGateway) {
        this.roleGateway = Objects.requireNonNull(roleGateway);
    }

    @Override
    public GetRoleByIdOutput execute(GetRoleByIdCommand aCommand) {
        return this.roleGateway.findById(aCommand.id())
                .map(GetRoleByIdOutput::from)
                .orElseThrow(() -> NotFoundException.with(Role.class, aCommand.id()));
    }
}

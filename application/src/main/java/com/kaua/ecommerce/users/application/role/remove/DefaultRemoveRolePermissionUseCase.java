package com.kaua.ecommerce.users.application.role.remove;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.Role;

import java.util.Objects;

public class DefaultRemoveRolePermissionUseCase extends RemoveRolePermissionUseCase {

    private final RoleGateway roleGateway;

    public DefaultRemoveRolePermissionUseCase(final RoleGateway roleGateway) {
        this.roleGateway = Objects.requireNonNull(roleGateway);
    }

    @Override
    public void execute(RemoveRolePermissionCommand input) {
        final var aRole = this.roleGateway.findById(input.id())
                .orElseThrow(() -> NotFoundException.with(Role.class, input.id()));

        aRole.getPermissions()
                .stream()
                .filter(permission -> permission.getPermissionName().equals(input.permissionName()))
                .findFirst()
                .ifPresent(aRole::removePermission);

        this.roleGateway.update(aRole);
    }
}

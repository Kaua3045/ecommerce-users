package com.kaua.ecommerce.users.application.permission.delete;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;

import java.util.Objects;

public class DefaultDeletePermissionUseCase extends DeletePermissionUseCase {

    private final PermissionGateway permissionGateway;

    public DefaultDeletePermissionUseCase(final PermissionGateway permissionGateway) {
        this.permissionGateway = Objects.requireNonNull(permissionGateway);
    }

    @Override
    public void execute(DeletePermissionCommand input) {
        this.permissionGateway.deleteById(input.id());
    }
}

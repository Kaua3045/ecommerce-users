package com.kaua.ecommerce.users.application.permission.retrieve.get;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.permissions.Permission;

import java.util.Objects;

public class DefaultGetPermissionByIdUseCase extends GetPermissionByIdUseCase {

    private final PermissionGateway permissionGateway;

    public DefaultGetPermissionByIdUseCase(final PermissionGateway permissionGateway) {
        this.permissionGateway = Objects.requireNonNull(permissionGateway);
    }

    @Override
    public GetPermissionByIdOutput execute(GetPermissionByIdCommand aCommand) {
        return this.permissionGateway.findById(aCommand.id())
                .map(GetPermissionByIdOutput::from)
                .orElseThrow(() -> NotFoundException.with(Permission.class, aCommand.id()));
    }
}

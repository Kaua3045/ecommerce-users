package com.kaua.ecommerce.users.application.permission.update;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Objects;

public class DefaultUpdatePermissionUseCase extends UpdatePermissionUseCase {

    private final PermissionGateway permissionGateway;

    public DefaultUpdatePermissionUseCase(final PermissionGateway permissionGateway) {
        this.permissionGateway = Objects.requireNonNull(permissionGateway);
    }

    @Override
    public Either<NotificationHandler, UpdatePermissionOutput> execute(UpdatePermissionCommand aCommand) {
        final var notification = NotificationHandler.create();
        final var aPermission = this.permissionGateway.findById(aCommand.id())
                .orElseThrow(() -> NotFoundException.with(Permission.class, aCommand.id()));

        final var aPermissionUpdated = aPermission.update(aCommand.description());
        aPermissionUpdated.validate(notification);

        return notification.hasError()
                ? Either.left(notification)
                : Either.right(UpdatePermissionOutput.from(this.permissionGateway.update(aPermissionUpdated)));
    }
}

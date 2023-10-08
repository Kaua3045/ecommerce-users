package com.kaua.ecommerce.users.application.usecases.permission.create;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Objects;

public class DefaultCreatePermissionUseCase extends CreatePermissionUseCase {

    private final PermissionGateway permissionGateway;

    public DefaultCreatePermissionUseCase(final PermissionGateway permissionGateway) {
        this.permissionGateway = Objects.requireNonNull(permissionGateway);
    }

    @Override
    public Either<NotificationHandler, CreatePermissionOutput> execute(CreatePermissionCommand aCommand) {
        final var notification = NotificationHandler.create();

        if (this.permissionGateway.existsByName(aCommand.name())) {
            return Either.left(notification.append(new Error("Permission already exists")));
        }

        final var aPermission = Permission.newPermission(aCommand.name(), aCommand.description());
        aPermission.validate(notification);

        return notification.hasError()
                ? Either.left(notification)
                : Either.right(CreatePermissionOutput.from(this.permissionGateway.create(aPermission)));
    }
}

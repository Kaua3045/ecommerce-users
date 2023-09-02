package com.kaua.ecommerce.users.application.role.create;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Arrays;
import java.util.Objects;

public class DefaultCreateRoleUseCase extends CreateRoleUseCase {

    private final RoleGateway roleGateway;

    public DefaultCreateRoleUseCase(final RoleGateway roleGateway) {
        this.roleGateway = Objects.requireNonNull(roleGateway);
    }

    @Override
    public Either<NotificationHandler, CreateRoleOutput> execute(final CreateRoleCommand aCommand) {
        final var notification = NotificationHandler.create();


        if (roleGateway.existsByName(aCommand.name())) {
            return Either.left(notification.append(new Error("Role already exists")));
        }

        final var aRole = Role.newRole(
                aCommand.name(),
                aCommand.description(),
                getRoleType(aCommand.roleType())
        );
        aRole.validate(notification);

        return notification.hasError()
                ? Either.left(notification)
                : Either.right(CreateRoleOutput.from(this.roleGateway.create(aRole)));
    }

    private RoleTypes getRoleType(String roleType) {
        return RoleTypes.of(roleType)
                .orElseThrow(() -> DomainException
                        .with(new Error("RoleType not found, role types available: " +
                                Arrays.toString(RoleTypes.values()))));
    }
}
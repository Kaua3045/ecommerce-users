package com.kaua.ecommerce.users.application.role.update;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Arrays;
import java.util.Objects;

public class DefaultUpdateRoleUseCase extends UpdateRoleUseCase {

    private final RoleGateway roleGateway;

    public DefaultUpdateRoleUseCase(final RoleGateway roleGateway) {
        this.roleGateway = Objects.requireNonNull(roleGateway);
    }

    @Override
    public Either<NotificationHandler, UpdateRoleOutput> execute(final UpdateRoleCommand aCommand) {
        final var aNotification = NotificationHandler.create();

        if (aCommand.isDefault() && this.roleGateway.findDefaultRole().isPresent()) {
            return Either.left(aNotification.append(new Error("Default role already exists")));
        }

        final var aRole = this.roleGateway.findById(aCommand.id())
                .orElseThrow(() -> NotFoundException.with(Role.class, aCommand.id()));

        final var aRoleUpdated = aRole.update(
                aCommand.name(),
                aCommand.description(),
                getRoleType(aCommand.roleType()),
                aCommand.isDefault()
        );
        aRoleUpdated.validate(aNotification);


        return aNotification.hasError()
                ? Either.left(aNotification)
                : Either.right(UpdateRoleOutput.from(this.roleGateway.update(aRoleUpdated)));
    }

    private RoleTypes getRoleType(String roleType) {
        return RoleTypes.of(roleType)
                .orElseThrow(() -> DomainException
                        .with(new Error("RoleType not found, role types available: " +
                                Arrays.toString(RoleTypes.values()))));
    }
}

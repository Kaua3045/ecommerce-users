package com.kaua.ecommerce.users.application.usecases.role.update;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RolePermission;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.*;

public class DefaultUpdateRoleUseCase extends UpdateRoleUseCase {

    private final RoleGateway roleGateway;
    private final PermissionGateway permissionGateway;

    public DefaultUpdateRoleUseCase(final RoleGateway roleGateway, final PermissionGateway permissionGateway) {
        this.roleGateway = Objects.requireNonNull(roleGateway);
        this.permissionGateway = Objects.requireNonNull(permissionGateway);
    }

    @Override
    public Either<NotificationHandler, UpdateRoleOutput> execute(final UpdateRoleCommand aCommand) {
        final var aNotification = NotificationHandler.create();

        if (aCommand.isDefault() && this.roleGateway.findDefaultRole().isPresent()) {
            return Either.left(aNotification.append(new Error("Default role already exists")));
        }

        final var aRole = this.roleGateway.findById(aCommand.id())
                .orElseThrow(NotFoundException.with(Role.class, aCommand.id()));

        final var aRolePermissions = toRolePermission(aCommand.permissions(), aRole.getPermissions());

        final var aRoleUpdated = aRole.update(
                aCommand.name() == null || aCommand.name().isBlank()
                        ? aRole.getName() : aCommand.name(),
                aCommand.description(),
                aCommand.roleType() == null || aCommand.roleType().isBlank()
                        ? aRole.getRoleType() : getRoleType(aCommand.roleType()),
                aCommand.isDefault(),
                aRolePermissions.isEmpty() ? aRole.getPermissions() : aRolePermissions
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

    private Set<RolePermission> toRolePermission(final Set<String> permissions, final Set<RolePermission> aOldRolePermissions) {
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptySet();
        }

        final var retrievedPermissions = this.permissionGateway.findAllByIds(permissions);

        final var newRolePermissions = new HashSet<>(aOldRolePermissions);

        retrievedPermissions.forEach(permission -> {
            final var rolePermission = RolePermission.newRolePermission(permission.getId(), permission.getName());
            newRolePermissions.add(rolePermission);
        });

        return newRolePermissions;
    }
}

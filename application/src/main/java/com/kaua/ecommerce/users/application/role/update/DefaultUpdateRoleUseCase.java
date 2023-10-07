package com.kaua.ecommerce.users.application.role.update;

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
                .orElseThrow(() -> NotFoundException.with(Role.class, aCommand.id()));

        final var aRolePermissions = toRolePermission(aCommand.permissions());

        final var aRoleUpdated = aRole.update(
                aCommand.name(),
                aCommand.description(),
                getRoleType(aCommand.roleType()),
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

    private List<RolePermission> toRolePermission(final List<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptyList();
        }

        final var retrievedPermissions = this.permissionGateway.findAllByIds(permissions);

        if (retrievedPermissions.size() != permissions.size()) {
            final var missingPermissions = new ArrayList<>(permissions);
            missingPermissions.removeAll(retrievedPermissions
                    .stream()
                    .map(permission -> permission.getId().getValue())
                    .toList());

            final var missingPermissionsMessage = String.join(", ", missingPermissions);

            throw DomainException.with(new Error("Some permissions could not be found: %s".formatted(missingPermissionsMessage)));
        }

        return retrievedPermissions.stream()
                .map(permission -> RolePermission.newRolePermission(permission.getId(), permission.getName()))
                .toList();
    }
}

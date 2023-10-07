package com.kaua.ecommerce.users.application.role.create;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.exceptions.DomainException;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RolePermission;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultCreateRoleUseCase extends CreateRoleUseCase {

    private final RoleGateway roleGateway;
    private final PermissionGateway permissionGateway;

    public DefaultCreateRoleUseCase(final RoleGateway roleGateway, final PermissionGateway permissionGateway) {
        this.roleGateway = Objects.requireNonNull(roleGateway);
        this.permissionGateway = Objects.requireNonNull(permissionGateway);
    }

    @Override
    public Either<NotificationHandler, CreateRoleOutput> execute(final CreateRoleCommand aCommand) {
        final var notification = NotificationHandler.create();

        if (roleGateway.existsByName(aCommand.name())) {
            return Either.left(notification.append(new Error("Role already exists")));
        }

        if (aCommand.isDefault() && roleGateway.findDefaultRole().isPresent()) {
            return Either.left(notification.append(new Error("Default role already exists")));
        }

        final var aRole = Role.newRole(
                aCommand.name(),
                aCommand.description(),
                getRoleType(aCommand.roleType()),
                aCommand.isDefault()
        );
        aRole.validate(notification);

        return notification.hasError()
                ? Either.left(notification)
                : Either.right(CreateRoleOutput.from(create(aRole, aCommand.permissions())));
    }

    private RoleTypes getRoleType(String roleType) {
        return RoleTypes.of(roleType)
                .orElseThrow(() -> DomainException
                        .with(new Error("RoleType not found, role types available: " +
                                Arrays.toString(RoleTypes.values()))));
    }

    private Role create(final Role aRole, final Set<String> aPermissions) {
        aRole.addPermissions(toRolePermission(aPermissions));
        return this.roleGateway.create(aRole);
    }

    private Set<RolePermission> toRolePermission(final Set<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptySet();
        }

        final var retrievedPermissions = this.permissionGateway.findAllByIds(permissions);

        return retrievedPermissions.stream()
                .map(permission -> RolePermission.newRolePermission(permission.getId(), permission.getName()))
                .collect(Collectors.toSet());
    }
}

package com.kaua.ecommerce.users.application.account.update.role;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Objects;

public class DefaultUpdateAccountRoleUseCase extends UpdateAccountRoleUseCase {

    private final AccountGateway accountGateway;
    private final RoleGateway roleGateway;

    public DefaultUpdateAccountRoleUseCase(final AccountGateway accountGateway, final RoleGateway roleGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.roleGateway = Objects.requireNonNull(roleGateway);
    }

    @Override
    public Either<NotificationHandler, UpdateAccountRoleOutput> execute(UpdateAccountRoleCommand aCommand) {
        final var notification = NotificationHandler.create();

        final var aAccount = this.accountGateway.findById(aCommand.id())
                .orElseThrow(() -> NotFoundException.with(Account.class, aCommand.id()));

        final var aRole = this.roleGateway.findById(aCommand.roleId())
                .orElseThrow(() -> NotFoundException.with(Role.class, aCommand.roleId()));

        final var aAccountUpdated = aAccount.changeRole(aRole);
        aAccountUpdated.validate(notification);

        return notification.hasError()
                ? Either.left(notification)
                : Either.right(UpdateAccountRoleOutput.from(this.accountGateway.update(aAccountUpdated)));
    }
}

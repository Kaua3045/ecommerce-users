package com.kaua.ecommerce.users.application.usecases.account.update.role;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultUpdateAccountRoleUseCase extends UpdateAccountRoleUseCase {

    private final AccountGateway accountGateway;
    private final RoleGateway roleGateway;

    public DefaultUpdateAccountRoleUseCase(final AccountGateway accountGateway, final RoleGateway roleGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.roleGateway = Objects.requireNonNull(roleGateway);
    }

    @Override
    public UpdateAccountRoleOutput execute(UpdateAccountRoleCommand aCommand) {
        final var notification = new ThrowsValidationHandler();
        final var aAccount = this.accountGateway.findById(aCommand.id())
                .orElseThrow(() -> NotFoundException.with(Account.class, aCommand.id()));

        final var aRole = this.roleGateway.findById(aCommand.roleId())
                .orElseThrow(() -> NotFoundException.with(Role.class, aCommand.roleId()));

        final var aAccountUpdated = aAccount.changeRole(aRole);
        aAccountUpdated.validate(notification);

        return UpdateAccountRoleOutput.from(this.accountGateway.update(aAccountUpdated));
    }
}

package com.kaua.ecommerce.users.application.account.retrieve.get;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;

import java.util.Objects;

public class DefaultGetAccountUseCase extends GetAccountUseCase {

    private final AccountGateway accountGateway;

    public DefaultGetAccountUseCase(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public GetAccountOutput execute(GetAccountCommand aCommand) {
        return this.accountGateway.findById(aCommand.id())
                .map(GetAccountOutput::from)
                .orElseThrow(() -> NotFoundException.with(Account.class, aCommand.id()));
    }
}

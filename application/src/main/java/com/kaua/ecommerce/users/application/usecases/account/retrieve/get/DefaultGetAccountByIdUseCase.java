package com.kaua.ecommerce.users.application.usecases.account.retrieve.get;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;

import java.util.Objects;

public class DefaultGetAccountByIdUseCase extends GetAccountByIdUseCase {

    private final AccountGateway accountGateway;

    public DefaultGetAccountByIdUseCase(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public GetAccountByIdOutput execute(GetAccountByIdCommand aCommand) {
        return this.accountGateway.findById(aCommand.id())
                .map(GetAccountByIdOutput::from)
                .orElseThrow(() -> NotFoundException.with(Account.class, aCommand.id()));
    }
}

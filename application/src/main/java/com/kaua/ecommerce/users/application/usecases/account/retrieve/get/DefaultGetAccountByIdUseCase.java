package com.kaua.ecommerce.users.application.usecases.account.retrieve.get;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.CacheGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;

import java.util.Objects;

public class DefaultGetAccountByIdUseCase extends GetAccountByIdUseCase {

    private final AccountGateway accountGateway;
    private final CacheGateway<Account> accountCacheGateway;

    public DefaultGetAccountByIdUseCase(
            final AccountGateway accountGateway,
            final CacheGateway<Account> accountCacheGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.accountCacheGateway = Objects.requireNonNull(accountCacheGateway);
    }

    @Override
    public GetAccountByIdOutput execute(GetAccountByIdCommand aCommand) {
        return this.accountCacheGateway.get(aCommand.id())
                .map(GetAccountByIdOutput::from)
                .orElseGet(() ->
                        this.accountGateway.findById(aCommand.id())
                        .map(account -> {
                            this.accountCacheGateway.save(account);
                            return GetAccountByIdOutput.from(account);
                        })
                        .orElseThrow(NotFoundException.with(Account.class, aCommand.id())));
    }
}

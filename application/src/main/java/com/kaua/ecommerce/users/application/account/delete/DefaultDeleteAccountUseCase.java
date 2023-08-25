package com.kaua.ecommerce.users.application.account.delete;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;

import java.util.Objects;

public class DefaultDeleteAccountUseCase extends DeleteAccountUseCase {

    private final AccountGateway accountGateway;

    public DefaultDeleteAccountUseCase(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public void execute(DeleteAccountCommand input) {
        accountGateway.deleteById(input.id());
    }
}

package com.kaua.ecommerce.users.application.account.delete;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;

import java.util.Objects;

public class DefaultDeleteAccountUseCase extends DeleteAccountUseCase {

    private final AccountGateway accountGateway;
    private final AccountMailGateway accountMailGateway;

    public DefaultDeleteAccountUseCase(
            final AccountGateway accountGateway,
            final AccountMailGateway accountMailGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.accountMailGateway = Objects.requireNonNull(accountMailGateway);
    }

    @Override
    public void execute(DeleteAccountCommand input) {
        accountMailGateway.findAllByAccountId(input.id())
                .forEach(result -> accountMailGateway.deleteById(result.getId().getValue()));

        accountGateway.deleteById(input.id());
    }
}

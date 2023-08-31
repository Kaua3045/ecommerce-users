package com.kaua.ecommerce.users.application.account.delete;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.application.gateways.AvatarGateway;

import java.util.Objects;

public class DefaultDeleteAccountUseCase extends DeleteAccountUseCase {

    private final AccountGateway accountGateway;
    private final AccountMailGateway accountMailGateway;
    private final AvatarGateway avatarGateway;

    public DefaultDeleteAccountUseCase(
            final AccountGateway accountGateway,
            final AccountMailGateway accountMailGateway,
            final AvatarGateway avatarGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.accountMailGateway = Objects.requireNonNull(accountMailGateway);
        this.avatarGateway = Objects.requireNonNull(avatarGateway);
    }

    @Override
    public void execute(DeleteAccountCommand input) {
        this.accountMailGateway.findAllByAccountId(input.id())
                .forEach(result -> accountMailGateway.deleteById(result.getId().getValue()));

        this.accountGateway.deleteById(input.id());
        this.avatarGateway.delete(input.id());
    }
}

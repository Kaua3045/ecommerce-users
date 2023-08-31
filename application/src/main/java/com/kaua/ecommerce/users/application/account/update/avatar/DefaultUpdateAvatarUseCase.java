package com.kaua.ecommerce.users.application.account.update.avatar;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AvatarGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;

import java.util.Objects;

public class DefaultUpdateAvatarUseCase extends UpdateAvatarUseCase {

    private final AvatarGateway avatarGateway;
    private final AccountGateway accountGateway;

    public DefaultUpdateAvatarUseCase(final AvatarGateway avatarGateway, final AccountGateway accountGateway) {
        this.avatarGateway = Objects.requireNonNull(avatarGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public UpdateAvatarOutput execute(UpdateAvatarCommand aCommand) {
        final var aAccount = this.accountGateway.findById(aCommand.accountId())
                .orElseThrow(() -> NotFoundException.with(Account.class, aCommand.accountId()));

        final var avatarUrlStored = aCommand.resource() == null
                ? aAccount.getAvatarUrl()
                : this.avatarGateway.save(aCommand.accountId(), aCommand.resource());

        final var aAccountUpdated = aAccount.update(
                aAccount.getPassword(),
                avatarUrlStored
        );

        final var aAccountSaved = this.accountGateway.update(aAccountUpdated);

        return UpdateAvatarOutput.from(aAccountSaved);
    }
}

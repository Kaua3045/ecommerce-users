package com.kaua.ecommerce.users.application.usecases.account.update.avatar;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AvatarGateway;
import com.kaua.ecommerce.users.application.gateways.CacheGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;

import java.util.Objects;

public class DefaultUpdateAvatarUseCase extends UpdateAvatarUseCase {

    private final AvatarGateway avatarGateway;
    private final AccountGateway accountGateway;
    private final CacheGateway<Account> accountCacheGateway;

    public DefaultUpdateAvatarUseCase(
            final AvatarGateway avatarGateway,
            final AccountGateway accountGateway,
            final CacheGateway<Account> accountCacheGateway
    ) {
        this.avatarGateway = Objects.requireNonNull(avatarGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.accountCacheGateway = Objects.requireNonNull(accountCacheGateway);
    }

    @Override
    public UpdateAvatarOutput execute(UpdateAvatarCommand aCommand) {
        final var aAccount = this.accountGateway.findById(aCommand.accountId())
                .orElseThrow(NotFoundException.with(Account.class, aCommand.accountId()));

        final var avatarUrlStored = aCommand.resource() == null
                ? aAccount.getAvatarUrl()
                : this.avatarGateway.save(aCommand.accountId(), aCommand.resource());

        final var aAccountUpdated = aAccount.changeAvatarUrl(avatarUrlStored);

        final var aAccountSaved = this.accountGateway.update(aAccountUpdated);
        this.accountCacheGateway.save(aAccountUpdated);

        return UpdateAvatarOutput.from(aAccountSaved);
    }
}

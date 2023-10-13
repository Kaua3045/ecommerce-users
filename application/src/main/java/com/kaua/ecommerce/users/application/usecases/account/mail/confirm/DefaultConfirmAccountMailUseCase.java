package com.kaua.ecommerce.users.application.usecases.account.mail.confirm;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.application.gateways.CacheGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Objects;

public class DefaultConfirmAccountMailUseCase extends ConfirmAccountMailUseCase {

    private final AccountMailGateway accountMailGateway;
    private final AccountGateway accountGateway;
    private final CacheGateway<Account> accountCacheGateway;

    public DefaultConfirmAccountMailUseCase(
            final AccountMailGateway accountMailGateway,
            final AccountGateway accountGateway,
            final CacheGateway<Account> accountCacheGateway
    ) {
        this.accountMailGateway = Objects.requireNonNull(accountMailGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.accountCacheGateway = Objects.requireNonNull(accountCacheGateway);
    }

    @Override
    public Either<NotificationHandler, Boolean> execute(ConfirmAccountMailCommand aCommand) {
        final var notification = NotificationHandler.create();

        final var aAccountMail = this.accountMailGateway.findByToken(aCommand.token())
                .orElseThrow(NotFoundException.with(AccountMail.class, aCommand.token()));

        if (aAccountMail.isExpired()) {
            return Either.left(notification.append(new Error("Token expired")));
        }

        final var aAccount = aAccountMail.getAccount().confirm();

        this.accountGateway.update(aAccount);
        this.accountCacheGateway.save(aAccount);
        this.accountMailGateway.deleteById(aAccountMail.getId().getValue());

        return Either.right(true);
    }
}

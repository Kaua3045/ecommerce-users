package com.kaua.ecommerce.users.application.account.update.password.reset;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.application.gateways.EncrypterGateway;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Objects;

public class DefaultResetPasswordUseCase extends ResetPasswordUseCase {

    private final AccountGateway accountGateway;
    private final EncrypterGateway encrypterGateway;
    private final AccountMailGateway accountMailGateway;

    public DefaultResetPasswordUseCase(
            final AccountGateway accountGateway,
            final EncrypterGateway encrypterGateway,
            final AccountMailGateway accountMailGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.encrypterGateway = Objects.requireNonNull(encrypterGateway);
        this.accountMailGateway = Objects.requireNonNull(accountMailGateway);
    }

    @Override
    public Either<NotificationHandler, Boolean> execute(ResetPasswordCommand input) {
        final var notification = NotificationHandler.create();

        final var aAccountMail = this.accountMailGateway.findByToken(input.token())
                .orElseThrow(() -> NotFoundException.with(AccountMail.class, input.token()));

        if (aAccountMail.isExpired()) {
            return Either.left(notification.append(new Error("Token expired")));
        }

        final var aAccount = aAccountMail.getAccount().update(
                this.encrypterGateway.encrypt(input.newPassword()),
                aAccountMail.getAccount().getAvatarUrl()
        );

        this.accountGateway.update(aAccount);
        this.accountMailGateway.deleteById(aAccountMail.getId().getValue());

        return notification.hasError()
                ? Either.left(notification)
                : Either.right(true);
    }
}

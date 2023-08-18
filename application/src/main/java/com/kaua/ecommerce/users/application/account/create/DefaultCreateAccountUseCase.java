package com.kaua.ecommerce.users.application.account.create;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.EncrypterGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Objects;

public class DefaultCreateAccountUseCase extends CreateAccountUseCase {

    private final AccountGateway accountGateway;
    private final EncrypterGateway encrypterGateway;

    public DefaultCreateAccountUseCase(final AccountGateway accountGateway, final EncrypterGateway encrypterGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.encrypterGateway = Objects.requireNonNull(encrypterGateway);
    }

    @Override
    public Either<NotificationHandler, CreateAccountOutput> execute(final CreateAccountCommand aCommand) {
        final var notification = NotificationHandler.create();

        if (this.accountGateway.existsByEmail(aCommand.email())) {
            return Either.left(notification.append(new Error("'email' already exists")));
        }

        final var aAccount = Account.newAccount(
                aCommand.firstName(),
                aCommand.lastName(),
                aCommand.email(),
                aCommand.password()
        );
        aAccount.validate(notification);

        return notification.hasError()
                ? Either.left(notification)
                : Either.right(CreateAccountOutput.from(accountWithPasswordEncodded(aAccount)));
    }

    private Account accountWithPasswordEncodded(final Account aAccount) {
        this.accountGateway.create(
                Account.with(
                        aAccount.getId().getValue(),
                        aAccount.getFirstName(),
                        aAccount.getLastName(),
                        aAccount.getEmail(),
                        aAccount.getMailStatus(),
                        this.encrypterGateway.encrypt(aAccount.getPassword()),
                        aAccount.getAvatarUrl(),
                        null,
                        aAccount.getCreatedAt(),
                        aAccount.getUpdatedAt())
        );
        return aAccount;
    }
}

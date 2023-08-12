package com.kaua.ecommerce.users.application.account.create;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;
import com.kaua.ecommerce.users.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateAccountUseCase extends CreateAccountUseCase {

    private final AccountGateway accountGateway;

    public DefaultCreateAccountUseCase(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Either<NotificationHandler, CreateAccountOutput> execute(final CreateAccountCommand aCommand) {
        // TODO: testar já iniciar um um erro no notification, talvez até faz uma verificação antes, caso tenha erro já retorna
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
                : Either.right(CreateAccountOutput.from(this.accountGateway.create(aAccount)));
    }
}

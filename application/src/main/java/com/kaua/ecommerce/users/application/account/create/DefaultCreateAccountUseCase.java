package com.kaua.ecommerce.users.application.account.create;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;
import com.kaua.ecommerce.users.domain.validation.handler.ThrowsValidationHandler;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class DefaultCreateAccountUseCase extends CreateAccountUseCase {

    private final AccountGateway accountGateway;

    public DefaultCreateAccountUseCase(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Mono<CreateAccountOutput> execute(CreateAccountCommand aCommand) {
        final var aAccount = Account.newAccount(
                aCommand.firstName(),
                aCommand.lastName(),
                aCommand.email(),
                aCommand.password()
        );
        aAccount.validate(new ThrowsValidationHandler());

        return this.accountGateway.existsByEmail(aCommand.email())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("'email' already exists"));
                    }
                    return Mono.just(aAccount);
                }).flatMap(this.accountGateway::create)
                .map(CreateAccountOutput::from);
    }

//    @Override
//    public Either<NotificationHandler, CreateAccountOutput> execute(final CreateAccountCommand aCommand) {
//        final var notification = NotificationHandler.create();
//
//        if (this.accountGateway.existsByEmail(aCommand.email())) {
//            return Either.left(notification.append(new Error("'email' already exists")));
//        }
//
//        final var aAccount = Account.newAccount(
//                aCommand.firstName(),
//                aCommand.lastName(),
//                aCommand.email(),
//                aCommand.password()
//        );
//        aAccount.validate(notification);
//
//        return notification.hasError()
//                ? Either.left(notification)
//                : Either.right(CreateAccountOutput.from(this.accountGateway.create(aAccount)));
//    }
}

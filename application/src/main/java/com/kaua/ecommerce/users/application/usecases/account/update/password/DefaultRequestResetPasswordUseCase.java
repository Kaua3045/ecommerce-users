package com.kaua.ecommerce.users.application.usecases.account.update.password;

import com.kaua.ecommerce.users.application.usecases.account.mail.create.CreateAccountMailCommand;
import com.kaua.ecommerce.users.application.usecases.account.mail.create.CreateAccountMailOutput;
import com.kaua.ecommerce.users.application.usecases.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.utils.IdUtils;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DefaultRequestResetPasswordUseCase extends RequestResetPasswordUseCase {

    private final AccountGateway accountGateway;
    private final CreateAccountMailUseCase createAccountMailUseCase;

    public DefaultRequestResetPasswordUseCase(
            final AccountGateway accountGateway,
            final CreateAccountMailUseCase createAccountMailUseCase
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.createAccountMailUseCase = Objects.requireNonNull(createAccountMailUseCase);
    }

    @Override
    public Either<NotificationHandler, CreateAccountMailOutput> execute(RequestResetPasswordCommand input) {
        final var aAccount = accountGateway.findByEmail(input.email())
                .orElseThrow(() -> NotFoundException.with(Account.class, input.email()));

        final var aCommand = CreateAccountMailCommand.with(
                aAccount,
                IdUtils.generate().replace("-", ""),
                AccountMailType.PASSWORD_RESET,
                InstantUtils.now().plus(30, ChronoUnit.MINUTES)
        );

        return this.createAccountMailUseCase.execute(aCommand);
    }
}

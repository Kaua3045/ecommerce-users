package com.kaua.ecommerce.users.application.usecases.account.mail.confirm.request;

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

public class DefaultRequestAccountConfirmUseCase extends RequestAccountConfirmUseCase {

    private final AccountGateway accountGateway;
    private final CreateAccountMailUseCase createAccountMailUseCase;

    public DefaultRequestAccountConfirmUseCase(
            final AccountGateway accountGateway,
            final CreateAccountMailUseCase createAccountMailUseCase
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.createAccountMailUseCase = Objects.requireNonNull(createAccountMailUseCase);
    }

    @Override
    public Either<NotificationHandler, CreateAccountMailOutput> execute(RequestAccountConfirmCommand input) {
        final var aAccount = accountGateway.findById(input.id())
                .orElseThrow(() -> NotFoundException.with(Account.class, input.id()));

        final var aCommand = CreateAccountMailCommand.with(
                aAccount,
                IdUtils.generate().replace("-", ""),
                AccountMailType.ACCOUNT_CONFIRMATION,
                InstantUtils.now().plus(1, ChronoUnit.HOURS)
        );

        return this.createAccountMailUseCase.execute(aCommand);
    }
}

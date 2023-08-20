package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.account.create.CreateAccountCommand;
import com.kaua.ecommerce.users.application.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.account.mail.confirm.ConfirmAccountMailCommand;
import com.kaua.ecommerce.users.application.account.mail.confirm.ConfirmAccountMailUseCase;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailCommand;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.utils.IdUtils;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.api.AccountAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;

@RestController
public class AccountController implements AccountAPI {

    private final CreateAccountUseCase createAccountUseCase;
    private final CreateAccountMailUseCase createAccountMailUseCase;
    private final ConfirmAccountMailUseCase confirmAccountMailUseCase;

    public AccountController(
            final CreateAccountUseCase createAccountUseCase,
            final CreateAccountMailUseCase createAccountMailUseCase,
            final ConfirmAccountMailUseCase confirmAccountMailUseCase
    ) {
        this.createAccountUseCase = createAccountUseCase;
        this.createAccountMailUseCase = createAccountMailUseCase;
        this.confirmAccountMailUseCase = confirmAccountMailUseCase;
    }

    @Override
    public ResponseEntity<?> createAccount(CreateAccountApiInput input) {
        final var aCommand = CreateAccountCommand.with(
                input.firstName(),
                input.lastName(),
                input.email(),
                input.password()
        );

        final var aResult = this.createAccountUseCase.execute(aCommand);

        if (aResult.isRight()) {
            final var aCommandMail = CreateAccountMailCommand.with(
                    aResult.getRight().id(),
                    IdUtils.generate().replace("-", ""),
                    AccountMailType.ACCOUNT_CONFIRMATION,
                    "Account Confirmation",
                    InstantUtils.now().plus(1, ChronoUnit.HOURS)
            );

            final var aAccountMailResut = this.createAccountMailUseCase.execute(aCommandMail);
            // TODO: add debug log in aAccountMailResut.isLeft()
        }

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.status(HttpStatus.CREATED).body(aResult.getRight());
    }

    @Override
    public ResponseEntity<?> confirmAccount(String token) {
        final var aCommand = ConfirmAccountMailCommand.with(token);

        final var aResult = this.confirmAccountMailUseCase.execute(aCommand);

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.noContent().build();
    }
}

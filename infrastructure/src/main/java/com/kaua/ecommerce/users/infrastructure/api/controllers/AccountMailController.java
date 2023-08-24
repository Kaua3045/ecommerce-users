package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.account.mail.confirm.ConfirmAccountMailCommand;
import com.kaua.ecommerce.users.application.account.mail.confirm.ConfirmAccountMailUseCase;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailCommand;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.account.update.password.RequestResetPasswordCommand;
import com.kaua.ecommerce.users.application.account.update.password.RequestResetPasswordUseCase;
import com.kaua.ecommerce.users.application.account.update.password.reset.ResetPasswordCommand;
import com.kaua.ecommerce.users.application.account.update.password.reset.ResetPasswordUseCase;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.utils.IdUtils;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.infrastructure.accounts.models.RequestResetPasswordApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.ResetPasswordApiInput;
import com.kaua.ecommerce.users.infrastructure.api.AccountMailAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;

@RestController
public class AccountMailController implements AccountMailAPI {

    private final CreateAccountMailUseCase createAccountMailUseCase;
    private final ConfirmAccountMailUseCase confirmAccountMailUseCase;
    private final RequestResetPasswordUseCase requestResetPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    public AccountMailController(
            final CreateAccountMailUseCase createAccountMailUseCase,
            final ConfirmAccountMailUseCase confirmAccountMailUseCase,
            final RequestResetPasswordUseCase requestResetPasswordUseCase,
            final ResetPasswordUseCase resetPasswordUseCase
    ) {
        this.createAccountMailUseCase = createAccountMailUseCase;
        this.confirmAccountMailUseCase = confirmAccountMailUseCase;
        this.requestResetPasswordUseCase = requestResetPasswordUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    @Override
    public ResponseEntity<?> confirmAccount(String token) {
        final var aCommand = ConfirmAccountMailCommand.with(token);

        final var aResult = this.confirmAccountMailUseCase.execute(aCommand);

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> createAccountConfirmation(String accountId) {
        final var aCommand = CreateAccountMailCommand.with(
                accountId,
                IdUtils.generate().replace("-", ""),
                AccountMailType.ACCOUNT_CONFIRMATION,
                InstantUtils.now().plus(1, ChronoUnit.HOURS)
        );

        final var aResult = this.createAccountMailUseCase.execute(aCommand);

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.status(HttpStatus.CREATED).body(aResult.getRight());
    }

    @Override
    public ResponseEntity<?> requestResetPassword(RequestResetPasswordApiInput input) {
        this.requestResetPasswordUseCase.execute(RequestResetPasswordCommand.with(input.email()));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordApiInput input, String token) {
        final var aResult = this.resetPasswordUseCase.execute(ResetPasswordCommand
                .with(token, input.password()));

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.noContent().build();
    }
}

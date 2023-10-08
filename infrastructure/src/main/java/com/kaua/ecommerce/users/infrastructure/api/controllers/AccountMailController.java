package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.usecases.account.mail.confirm.ConfirmAccountMailCommand;
import com.kaua.ecommerce.users.application.usecases.account.mail.confirm.ConfirmAccountMailUseCase;
import com.kaua.ecommerce.users.application.usecases.account.mail.confirm.request.RequestAccountConfirmCommand;
import com.kaua.ecommerce.users.application.usecases.account.mail.confirm.request.RequestAccountConfirmUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.password.RequestResetPasswordCommand;
import com.kaua.ecommerce.users.application.usecases.account.update.password.RequestResetPasswordUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.password.reset.ResetPasswordCommand;
import com.kaua.ecommerce.users.application.usecases.account.update.password.reset.ResetPasswordUseCase;
import com.kaua.ecommerce.users.infrastructure.accounts.models.RequestResetPasswordApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.ResetPasswordApiInput;
import com.kaua.ecommerce.users.infrastructure.api.AccountMailAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountMailController implements AccountMailAPI {

    private final RequestAccountConfirmUseCase requestAccountConfirmUseCase;
    private final ConfirmAccountMailUseCase confirmAccountMailUseCase;
    private final RequestResetPasswordUseCase requestResetPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    public AccountMailController(
            final RequestAccountConfirmUseCase requestAccountConfirmUseCase,
            final ConfirmAccountMailUseCase confirmAccountMailUseCase,
            final RequestResetPasswordUseCase requestResetPasswordUseCase,
            final ResetPasswordUseCase resetPasswordUseCase
    ) {
        this.requestAccountConfirmUseCase = requestAccountConfirmUseCase;
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
        final var aResult = this.requestAccountConfirmUseCase
                .execute(RequestAccountConfirmCommand.with(accountId));

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.status(HttpStatus.CREATED).body(aResult.getRight());
    }

    @Override
    public ResponseEntity<?> requestResetPassword(RequestResetPasswordApiInput input) {
        final var aResult = this.requestResetPasswordUseCase
                .execute(RequestResetPasswordCommand.with(input.email()));

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.status(HttpStatus.CREATED).body(aResult.getRight());
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

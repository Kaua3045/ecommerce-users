package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.account.create.CreateAccountCommand;
import com.kaua.ecommerce.users.application.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.account.update.password.RequestResetPasswordCommand;
import com.kaua.ecommerce.users.application.account.update.password.RequestResetPasswordUseCase;
import com.kaua.ecommerce.users.application.account.update.password.reset.ResetPasswordCommand;
import com.kaua.ecommerce.users.application.account.update.password.reset.ResetPasswordUseCase;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.RequestResetPasswordApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.ResetPasswordApiInput;
import com.kaua.ecommerce.users.infrastructure.api.AccountAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController implements AccountAPI {

    private final CreateAccountUseCase createAccountUseCase;
    private final RequestResetPasswordUseCase requestResetPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    public AccountController(
            final CreateAccountUseCase createAccountUseCase,
            final RequestResetPasswordUseCase requestResetPasswordUseCase,
            final ResetPasswordUseCase resetPasswordUseCase
    ) {
        this.createAccountUseCase = createAccountUseCase;
        this.requestResetPasswordUseCase = requestResetPasswordUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
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
                .with(input.password(), token));

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.noContent().build();
    }
}

package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.account.create.CreateAccountCommand;
import com.kaua.ecommerce.users.application.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.account.delete.DeleteAccountCommand;
import com.kaua.ecommerce.users.application.account.delete.DeleteAccountUseCase;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.api.AccountAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController implements AccountAPI {

    private final CreateAccountUseCase createAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;

    public AccountController(
            final CreateAccountUseCase createAccountUseCase,
            final DeleteAccountUseCase deleteAccountUseCase
    ) {
        this.createAccountUseCase = createAccountUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
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
    public ResponseEntity<?> deleteAccount(String id) {
        this.deleteAccountUseCase.execute(DeleteAccountCommand.with(id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

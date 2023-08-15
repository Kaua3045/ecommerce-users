package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.account.code.CreateAccountCodeCommand;
import com.kaua.ecommerce.users.application.account.code.CreateAccountCodeOutput;
import com.kaua.ecommerce.users.application.account.code.CreateAccountCodeUseCase;
import com.kaua.ecommerce.users.application.account.create.CreateAccountCommand;
import com.kaua.ecommerce.users.application.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiOutput;
import com.kaua.ecommerce.users.infrastructure.api.AccountAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController implements AccountAPI {

    private final CreateAccountUseCase createAccountUseCase;
    private final CreateAccountCodeUseCase createAccountCodeUseCase;

    public AccountController(final CreateAccountUseCase createAccountUseCase, final CreateAccountCodeUseCase createAccountCodeUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.createAccountCodeUseCase = createAccountCodeUseCase;
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
                : ResponseEntity.status(HttpStatus.CREATED).body(createAccountOutput(aResult.getRight().id()));
    }

    private CreateAccountApiOutput createAccountOutput(String id) {
        final var aCode = this.createAccountCodeUseCase.execute(
                CreateAccountCodeCommand.with(
                        RandomStringUtils.generateValue(36),
                        RandomStringUtils.generateValue(60),
                        id)
        );

        return new CreateAccountApiOutput(
                id,
                aCode.code(),
                aCode.codeChallenge());
    }
}

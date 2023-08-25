package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.account.create.CreateAccountCommand;
import com.kaua.ecommerce.users.application.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.account.delete.DeleteAccountCommand;
import com.kaua.ecommerce.users.application.account.delete.DeleteAccountUseCase;
import com.kaua.ecommerce.users.application.account.retrieve.get.GetAccountByIdCommand;
import com.kaua.ecommerce.users.application.account.retrieve.get.GetAccountByIdUseCase;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.GetAccountPresenter;
import com.kaua.ecommerce.users.infrastructure.accounts.presenters.AccountApiPresenter;
import com.kaua.ecommerce.users.infrastructure.api.AccountAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController implements AccountAPI {

    private final CreateAccountUseCase createAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;

    public AccountController(
            final CreateAccountUseCase createAccountUseCase,
            final DeleteAccountUseCase deleteAccountUseCase,
            final GetAccountByIdUseCase getAccountByIdUseCase
    ) {
        this.createAccountUseCase = createAccountUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
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
    public GetAccountPresenter getAccount(String id) {
        return AccountApiPresenter.present(this.getAccountByIdUseCase
                .execute(GetAccountByIdCommand.with(id)));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(String id) {
        this.deleteAccountUseCase.execute(DeleteAccountCommand.with(id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

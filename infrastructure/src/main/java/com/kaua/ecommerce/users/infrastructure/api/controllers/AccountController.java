package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.usecases.account.create.CreateAccountCommand;
import com.kaua.ecommerce.users.application.usecases.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.usecases.account.delete.DeleteAccountCommand;
import com.kaua.ecommerce.users.application.usecases.account.delete.DeleteAccountUseCase;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.get.GetAccountByIdCommand;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.get.GetAccountByIdUseCase;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.list.ListAccountsUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.avatar.UpdateAvatarCommand;
import com.kaua.ecommerce.users.application.usecases.account.update.avatar.UpdateAvatarUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.role.UpdateAccountRoleCommand;
import com.kaua.ecommerce.users.application.usecases.account.update.role.UpdateAccountRoleUseCase;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.GetAccountPresenter;
import com.kaua.ecommerce.users.infrastructure.accounts.models.ListAccountsPresenter;
import com.kaua.ecommerce.users.infrastructure.accounts.models.UpdateAccountRoleApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.presenters.AccountApiPresenter;
import com.kaua.ecommerce.users.infrastructure.api.AccountAPI;
import com.kaua.ecommerce.users.infrastructure.utils.ResourceOf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AccountController implements AccountAPI {

    private final CreateAccountUseCase createAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final UpdateAvatarUseCase updateAvatarUseCase;
    private final UpdateAccountRoleUseCase updateAccountRoleUseCase;
    private final ListAccountsUseCase listAccountsUseCase;

    public AccountController(
            final CreateAccountUseCase createAccountUseCase,
            final DeleteAccountUseCase deleteAccountUseCase,
            final GetAccountByIdUseCase getAccountByIdUseCase,
            final UpdateAvatarUseCase updateAvatarUseCase,
            final UpdateAccountRoleUseCase updateAccountRoleUseCase,
            final ListAccountsUseCase listAccountsUseCase
    ) {
        this.createAccountUseCase = createAccountUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.updateAvatarUseCase = updateAvatarUseCase;
        this.updateAccountRoleUseCase = updateAccountRoleUseCase;
        this.listAccountsUseCase = listAccountsUseCase;
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
    public Pagination<ListAccountsPresenter> listAccounts(String search, int page, int perPage, String sort, String direction) {
        return this.listAccountsUseCase
                .execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(AccountApiPresenter::present);
    }

    @Override
    public ResponseEntity<?> updateAccountAvatar(String id, MultipartFile avatarFile) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.updateAvatarUseCase.execute(UpdateAvatarCommand.with(
                        id, ResourceOf.with(avatarFile))));
    }

    @Override
    public ResponseEntity<?> updateAccountRole(String id, UpdateAccountRoleApiInput input) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.updateAccountRoleUseCase.execute(UpdateAccountRoleCommand.with(
                        id, input.roleId())));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(String id) {
        this.deleteAccountUseCase.execute(DeleteAccountCommand.with(id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

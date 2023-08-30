package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.account.create.CreateAccountCommand;
import com.kaua.ecommerce.users.application.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.account.delete.DeleteAccountCommand;
import com.kaua.ecommerce.users.application.account.delete.DeleteAccountUseCase;
import com.kaua.ecommerce.users.application.account.retrieve.get.GetAccountByIdCommand;
import com.kaua.ecommerce.users.application.account.retrieve.get.GetAccountByIdUseCase;
import com.kaua.ecommerce.users.application.account.update.avatar.UpdateAvatarCommand;
import com.kaua.ecommerce.users.application.account.update.avatar.UpdateAvatarUseCase;
import com.kaua.ecommerce.users.domain.utils.Resource;
import com.kaua.ecommerce.users.infrastructure.accounts.models.CreateAccountApiInput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.GetAccountPresenter;
import com.kaua.ecommerce.users.infrastructure.accounts.presenters.AccountApiPresenter;
import com.kaua.ecommerce.users.infrastructure.api.AccountAPI;
import com.kaua.ecommerce.users.infrastructure.exceptions.ImageSizeNotValidException;
import com.kaua.ecommerce.users.infrastructure.exceptions.ImageTypeNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AccountController implements AccountAPI {

    private final CreateAccountUseCase createAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final UpdateAvatarUseCase updateAvatarUseCase;

    public AccountController(
            final CreateAccountUseCase createAccountUseCase,
            final DeleteAccountUseCase deleteAccountUseCase,
            final GetAccountByIdUseCase getAccountByIdUseCase,
            final UpdateAvatarUseCase updateAvatarUseCase
    ) {
        this.createAccountUseCase = createAccountUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.updateAvatarUseCase = updateAvatarUseCase;
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
    public ResponseEntity<?> updateAccountAvatar(String id, MultipartFile avatarFile) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.updateAvatarUseCase.execute(UpdateAvatarCommand.with(
                        id, resourceOf(avatarFile))));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(String id) {
        this.deleteAccountUseCase.execute(DeleteAccountCommand.with(id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private Resource resourceOf(final MultipartFile part) {
        if (part == null) {
            return null;
        }

        isValidImage(part);

        try {
            return Resource.with(
                    part.getInputStream(),
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private void isValidImage(final MultipartFile part) {
        final var imageTypes = List.of(
                "image/jpeg",
                "image/jpg",
                "image/png"
        );

        final var validImageType = imageTypes.contains(part.getContentType());
        final var IMAGE_SIZE = 600;
        final var validImageSize = part.getSize() > IMAGE_SIZE * 1024;

        if (!validImageType) {
            throw new ImageTypeNotValidException();
        }

        if (validImageSize) {
            throw new ImageSizeNotValidException();
        }
    }
}

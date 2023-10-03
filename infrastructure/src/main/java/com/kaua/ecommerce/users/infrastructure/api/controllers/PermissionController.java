package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.permission.create.CreatePermissionCommand;
import com.kaua.ecommerce.users.application.permission.create.CreatePermissionUseCase;
import com.kaua.ecommerce.users.application.permission.delete.DeletePermissionCommand;
import com.kaua.ecommerce.users.application.permission.delete.DeletePermissionUseCase;
import com.kaua.ecommerce.users.application.permission.retrieve.get.GetPermissionByIdCommand;
import com.kaua.ecommerce.users.application.permission.retrieve.get.GetPermissionByIdUseCase;
import com.kaua.ecommerce.users.infrastructure.api.PermissionAPI;
import com.kaua.ecommerce.users.infrastructure.permissions.models.CreatePermissionApiInput;
import com.kaua.ecommerce.users.infrastructure.permissions.models.GetPermissionResponse;
import com.kaua.ecommerce.users.infrastructure.permissions.presenters.PermissionApiPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PermissionController implements PermissionAPI {

    private final CreatePermissionUseCase createPermissionUseCase;
    private final DeletePermissionUseCase deletePermissionUseCase;
    private final GetPermissionByIdUseCase getPermissionByIdUseCase;

    public PermissionController(
            final CreatePermissionUseCase createPermissionUseCase,
            final DeletePermissionUseCase deletePermissionUseCase,
            final GetPermissionByIdUseCase getPermissionByIdUseCase
    ) {
        this.createPermissionUseCase = createPermissionUseCase;
        this.deletePermissionUseCase = deletePermissionUseCase;
        this.getPermissionByIdUseCase = getPermissionByIdUseCase;
    }

    @Override
    public ResponseEntity<?> createPermission(CreatePermissionApiInput input) {
        final var aCommand = CreatePermissionCommand.with(input.name(), input.description());

        final var aResult = this.createPermissionUseCase.execute(aCommand);

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.status(HttpStatus.CREATED).body(aResult.getRight());
    }

    @Override
    public GetPermissionResponse getPermission(String id) {
        return PermissionApiPresenter.present(this.getPermissionByIdUseCase
                .execute(GetPermissionByIdCommand.with(id)));
    }

    @Override
    public ResponseEntity<?> deletePermission(String id) {
        this.deletePermissionUseCase.execute(DeletePermissionCommand.with(id));
        return ResponseEntity.ok().build();
    }
}

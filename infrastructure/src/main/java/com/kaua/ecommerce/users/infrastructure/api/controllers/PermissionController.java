package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.usecases.permission.create.CreatePermissionCommand;
import com.kaua.ecommerce.users.application.usecases.permission.create.CreatePermissionUseCase;
import com.kaua.ecommerce.users.application.usecases.permission.delete.DeletePermissionCommand;
import com.kaua.ecommerce.users.application.usecases.permission.delete.DeletePermissionUseCase;
import com.kaua.ecommerce.users.application.usecases.permission.retrieve.get.GetPermissionByIdCommand;
import com.kaua.ecommerce.users.application.usecases.permission.retrieve.get.GetPermissionByIdUseCase;
import com.kaua.ecommerce.users.application.usecases.permission.retrieve.list.ListPermissionsUseCase;
import com.kaua.ecommerce.users.application.usecases.permission.update.UpdatePermissionCommand;
import com.kaua.ecommerce.users.application.usecases.permission.update.UpdatePermissionUseCase;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.infrastructure.api.PermissionAPI;
import com.kaua.ecommerce.users.infrastructure.permissions.models.CreatePermissionApiInput;
import com.kaua.ecommerce.users.infrastructure.permissions.models.GetPermissionResponse;
import com.kaua.ecommerce.users.infrastructure.permissions.models.ListPermissionResponse;
import com.kaua.ecommerce.users.infrastructure.permissions.models.UpdatePermissionApiInput;
import com.kaua.ecommerce.users.infrastructure.permissions.presenters.PermissionApiPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PermissionController implements PermissionAPI {

    private final CreatePermissionUseCase createPermissionUseCase;
    private final DeletePermissionUseCase deletePermissionUseCase;
    private final GetPermissionByIdUseCase getPermissionByIdUseCase;
    private final UpdatePermissionUseCase updatePermissionUseCase;
    private final ListPermissionsUseCase listPermissionsUseCase;

    public PermissionController(
            final CreatePermissionUseCase createPermissionUseCase,
            final DeletePermissionUseCase deletePermissionUseCase,
            final GetPermissionByIdUseCase getPermissionByIdUseCase,
            final UpdatePermissionUseCase updatePermissionUseCase,
            final ListPermissionsUseCase listPermissionsUseCase
    ) {
        this.createPermissionUseCase = createPermissionUseCase;
        this.deletePermissionUseCase = deletePermissionUseCase;
        this.getPermissionByIdUseCase = getPermissionByIdUseCase;
        this.updatePermissionUseCase = updatePermissionUseCase;
        this.listPermissionsUseCase = listPermissionsUseCase;
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
    public Pagination<ListPermissionResponse> listPermissions(
            String search,
            int page,
            int perPage,
            String sort,
            String direction
    ) {
        return this.listPermissionsUseCase
                .execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(PermissionApiPresenter::present);
    }

    @Override
    public ResponseEntity<?> updatePermission(String id, UpdatePermissionApiInput input) {
        final var aCommand = UpdatePermissionCommand.with(id, input.description());

        final var aResult = this.updatePermissionUseCase.execute(aCommand);

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.ok().body(aResult.getRight());
    }

    @Override
    public ResponseEntity<?> deletePermission(String id) {
        this.deletePermissionUseCase.execute(DeletePermissionCommand.with(id));
        return ResponseEntity.ok().build();
    }
}

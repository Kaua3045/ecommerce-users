package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.usecases.role.create.CreateRoleCommand;
import com.kaua.ecommerce.users.application.usecases.role.create.CreateRoleUseCase;
import com.kaua.ecommerce.users.application.usecases.role.delete.DeleteRoleCommand;
import com.kaua.ecommerce.users.application.usecases.role.delete.DeleteRoleUseCase;
import com.kaua.ecommerce.users.application.usecases.role.remove.RemoveRolePermissionCommand;
import com.kaua.ecommerce.users.application.usecases.role.remove.RemoveRolePermissionUseCase;
import com.kaua.ecommerce.users.application.usecases.role.retrieve.get.GetRoleByIdCommand;
import com.kaua.ecommerce.users.application.usecases.role.retrieve.get.GetRoleByIdUseCase;
import com.kaua.ecommerce.users.application.usecases.role.retrieve.list.ListRolesUseCase;
import com.kaua.ecommerce.users.application.usecases.role.update.UpdateRoleCommand;
import com.kaua.ecommerce.users.application.usecases.role.update.UpdateRoleUseCase;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.infrastructure.api.RoleAPI;
import com.kaua.ecommerce.users.infrastructure.roles.models.*;
import com.kaua.ecommerce.users.infrastructure.roles.presenters.RoleApiPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController implements RoleAPI {

    private final CreateRoleUseCase createRoleUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final GetRoleByIdUseCase getRoleByIdUseCase;
    private final ListRolesUseCase listRolesUseCase;
    private final RemoveRolePermissionUseCase removeRolePermissionUseCase;

    public RoleController(
            final CreateRoleUseCase createRoleUseCase,
            final UpdateRoleUseCase updateRoleUseCase,
            final DeleteRoleUseCase deleteRoleUseCase,
            final GetRoleByIdUseCase getRoleByIdUseCase,
            final ListRolesUseCase listRolesUseCase,
            final RemoveRolePermissionUseCase removeRolePermissionUseCase
    ) {
        this.createRoleUseCase = createRoleUseCase;
        this.updateRoleUseCase = updateRoleUseCase;
        this.deleteRoleUseCase = deleteRoleUseCase;
        this.getRoleByIdUseCase = getRoleByIdUseCase;
        this.listRolesUseCase = listRolesUseCase;
        this.removeRolePermissionUseCase = removeRolePermissionUseCase;
    }

    @Override
    public ResponseEntity<?> createRole(CreateRoleApiInput input) {
        final var aCommand = CreateRoleCommand.with(
                input.name(),
                input.description(),
                input.roleType(),
                input.isDefault(),
                input.permissions()
        );

        final var aResult = this.createRoleUseCase.execute(aCommand);

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.status(HttpStatus.CREATED).body(aResult.getRight());
    }

    @Override
    public ResponseEntity<?> updateRole(String id, UpdateRoleApiInput input) {
        final var aCommand = UpdateRoleCommand.with(
                id,
                input.name(),
                input.description(),
                input.roleType(),
                input.isDefault(),
                input.permissions()
        );

        final var aResult = this.updateRoleUseCase.execute(aCommand);

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.ok().body(aResult.getRight());
    }

    @Override
    public GetRoleResponse getRole(String id) {
        return RoleApiPresenter.present(this.getRoleByIdUseCase
                .execute(GetRoleByIdCommand.with(id)));
    }

    @Override
    public Pagination<ListRoleResponse> listRoles(String search, int page, int perPage, String sort, String direction) {
        return this.listRolesUseCase
                .execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(RoleApiPresenter::present);
    }

    @Override
    public ResponseEntity<?> deleteRole(String id) {
        this.deleteRoleUseCase.execute(DeleteRoleCommand.with(id));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> removeRolePermission(String roleId, RemoveRolePermissionApiInput input) {
        this.removeRolePermissionUseCase.execute(RemoveRolePermissionCommand.with(roleId, input.permissionName()));
        return ResponseEntity.ok().build();
    }
}

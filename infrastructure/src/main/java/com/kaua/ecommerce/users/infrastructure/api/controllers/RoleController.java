package com.kaua.ecommerce.users.infrastructure.api.controllers;

import com.kaua.ecommerce.users.application.role.create.CreateRoleCommand;
import com.kaua.ecommerce.users.application.role.create.CreateRoleUseCase;
import com.kaua.ecommerce.users.application.role.delete.DeleteRoleCommand;
import com.kaua.ecommerce.users.application.role.delete.DeleteRoleUseCase;
import com.kaua.ecommerce.users.application.role.update.UpdateRoleCommand;
import com.kaua.ecommerce.users.application.role.update.UpdateRoleUseCase;
import com.kaua.ecommerce.users.infrastructure.api.RoleAPI;
import com.kaua.ecommerce.users.infrastructure.roles.models.CreateRoleApiInput;
import com.kaua.ecommerce.users.infrastructure.roles.models.UpdateRoleApiInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController implements RoleAPI {

    private final CreateRoleUseCase createRoleUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;

    public RoleController(
            final CreateRoleUseCase createRoleUseCase,
            final UpdateRoleUseCase updateRoleUseCase,
            final DeleteRoleUseCase deleteRoleUseCase
    ) {
        this.createRoleUseCase = createRoleUseCase;
        this.updateRoleUseCase = updateRoleUseCase;
        this.deleteRoleUseCase = deleteRoleUseCase;
    }

    @Override
    public ResponseEntity<?> createRole(CreateRoleApiInput input) {
        final var aCommand = CreateRoleCommand.with(
                input.name(),
                input.description(),
                input.roleType()
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
                input.roleType()
        );

        final var aResult = this.updateRoleUseCase.execute(aCommand);

        return aResult.isLeft()
                ? ResponseEntity.unprocessableEntity().body(aResult.getLeft())
                : ResponseEntity.ok().body(aResult.getRight());
    }

    @Override
    public ResponseEntity<?> deleteRole(String id) {
        this.deleteRoleUseCase.execute(DeleteRoleCommand.with(id));
        return ResponseEntity.ok().build();
    }
}

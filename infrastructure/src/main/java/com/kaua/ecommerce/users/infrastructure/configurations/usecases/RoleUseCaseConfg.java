package com.kaua.ecommerce.users.infrastructure.configurations.usecases;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.application.usecases.role.create.CreateRoleUseCase;
import com.kaua.ecommerce.users.application.usecases.role.create.DefaultCreateRoleUseCase;
import com.kaua.ecommerce.users.application.usecases.role.delete.DefaultDeleteRoleUseCase;
import com.kaua.ecommerce.users.application.usecases.role.delete.DeleteRoleUseCase;
import com.kaua.ecommerce.users.application.usecases.role.remove.DefaultRemoveRolePermissionUseCase;
import com.kaua.ecommerce.users.application.usecases.role.remove.RemoveRolePermissionUseCase;
import com.kaua.ecommerce.users.application.usecases.role.retrieve.get.DefaultGetRoleByIdUseCase;
import com.kaua.ecommerce.users.application.usecases.role.retrieve.get.GetRoleByIdUseCase;
import com.kaua.ecommerce.users.application.usecases.role.retrieve.list.DefaultListRolesUseCase;
import com.kaua.ecommerce.users.application.usecases.role.retrieve.list.ListRolesUseCase;
import com.kaua.ecommerce.users.application.usecases.role.update.DefaultUpdateRoleUseCase;
import com.kaua.ecommerce.users.application.usecases.role.update.UpdateRoleUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class RoleUseCaseConfg {

    private final RoleGateway roleGateway;
    private final PermissionGateway permissionGateway;

    public RoleUseCaseConfg(final RoleGateway roleGateway, final PermissionGateway permissionGateway) {
        this.roleGateway = Objects.requireNonNull(roleGateway);
        this.permissionGateway = Objects.requireNonNull(permissionGateway);
    }

    @Bean
    public CreateRoleUseCase createRoleUseCase() {
        return new DefaultCreateRoleUseCase(roleGateway, permissionGateway);
    }

    @Bean
    public UpdateRoleUseCase updateRoleUseCase() {
        return new DefaultUpdateRoleUseCase(roleGateway, permissionGateway);
    }

    @Bean
    public GetRoleByIdUseCase getRoleByIdUseCase() {
        return new DefaultGetRoleByIdUseCase(roleGateway);
    }

    @Bean
    public ListRolesUseCase listRolesUseCase() {
        return new DefaultListRolesUseCase(roleGateway);
    }

    @Bean
    public DeleteRoleUseCase deleteRoleUseCase() {
        return new DefaultDeleteRoleUseCase(roleGateway);
    }

    @Bean
    public RemoveRolePermissionUseCase removeRolePermissionUseCase() {
        return new DefaultRemoveRolePermissionUseCase(roleGateway);
    }
}

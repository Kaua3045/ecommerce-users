package com.kaua.ecommerce.users.infrastructure.configurations.usecases;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.application.permission.create.CreatePermissionUseCase;
import com.kaua.ecommerce.users.application.permission.create.DefaultCreatePermissionUseCase;
import com.kaua.ecommerce.users.application.permission.delete.DefaultDeletePermissionUseCase;
import com.kaua.ecommerce.users.application.permission.delete.DeletePermissionUseCase;
import com.kaua.ecommerce.users.application.permission.retrieve.get.DefaultGetPermissionByIdUseCase;
import com.kaua.ecommerce.users.application.permission.retrieve.get.GetPermissionByIdUseCase;
import com.kaua.ecommerce.users.application.permission.retrieve.list.DefaultListPermissionsUseCase;
import com.kaua.ecommerce.users.application.permission.retrieve.list.ListPermissionsUseCase;
import com.kaua.ecommerce.users.application.permission.update.DefaultUpdatePermissionUseCase;
import com.kaua.ecommerce.users.application.permission.update.UpdatePermissionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class PermissionUseCaseConfig {

    private final PermissionGateway permissionGateway;

    public PermissionUseCaseConfig(final PermissionGateway permissionGateway) {
        this.permissionGateway = Objects.requireNonNull(permissionGateway);
    }

    @Bean
    public CreatePermissionUseCase createPermissionUseCase() {
        return new DefaultCreatePermissionUseCase(permissionGateway);
    }

    @Bean
    public GetPermissionByIdUseCase getPermissionByIdUseCase() {
        return new DefaultGetPermissionByIdUseCase(permissionGateway);
    }

    @Bean
    public ListPermissionsUseCase listPermissionsUseCase() {
        return new DefaultListPermissionsUseCase(permissionGateway);
    }

    @Bean
    public UpdatePermissionUseCase updatePermissionUseCase() {
        return new DefaultUpdatePermissionUseCase(permissionGateway);
    }

    @Bean
    public DeletePermissionUseCase deletePermissionUseCase() {
        return new DefaultDeletePermissionUseCase(permissionGateway);
    }
}

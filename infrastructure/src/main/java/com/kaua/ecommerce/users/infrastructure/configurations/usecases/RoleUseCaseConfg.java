package com.kaua.ecommerce.users.infrastructure.configurations.usecases;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.application.role.create.CreateRoleUseCase;
import com.kaua.ecommerce.users.application.role.create.DefaultCreateRoleUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class RoleUseCaseConfg {

    private final RoleGateway roleGateway;

    public RoleUseCaseConfg(final RoleGateway roleGateway) {
        this.roleGateway = Objects.requireNonNull(roleGateway);
    }

    @Bean
    public CreateRoleUseCase createRoleUseCase() {
        return new DefaultCreateRoleUseCase(roleGateway);
    }
}

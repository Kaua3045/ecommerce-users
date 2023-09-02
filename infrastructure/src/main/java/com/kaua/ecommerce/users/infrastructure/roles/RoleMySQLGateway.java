package com.kaua.ecommerce.users.infrastructure.roles;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RoleMySQLGateway implements RoleGateway {

    private final RoleJpaRepository roleRepository;

    public RoleMySQLGateway(final RoleJpaRepository roleRepository) {
        this.roleRepository = Objects.requireNonNull(roleRepository);
    }

    @Override
    public Role create(Role aRole) {
        return this.roleRepository.save(RoleJpaEntity.toEntity(aRole)).toDomain();
    }

    @Override
    public boolean existsByName(String aName) {
        return this.roleRepository.existsByName(aName);
    }
}
package com.kaua.ecommerce.users.infrastructure.permissions;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PermissionMySQLGateway implements PermissionGateway {

    private final PermissionJpaRepository permissionRepository;

    public PermissionMySQLGateway(final PermissionJpaRepository permissionRepository) {
        this.permissionRepository = Objects.requireNonNull(permissionRepository);
    }

    @Override
    public Permission create(Permission aPermission) {
        return this.permissionRepository.save(PermissionJpaEntity.toEntity(aPermission)).toDomain();
    }

    @Override
    public boolean existsByName(String aName) {
        return this.permissionRepository.existsByName(aName);
    }
}

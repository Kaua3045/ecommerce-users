package com.kaua.ecommerce.users.infrastructure.permissions;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

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

    @Override
    public Optional<Permission> findById(String aId) {
        return this.permissionRepository.findById(aId).map(PermissionJpaEntity::toDomain);
    }

    @Override
    public void deleteById(String aId) {
        if (this.permissionRepository.existsById(aId)) {
            this.permissionRepository.deleteById(aId);
        }
    }
}

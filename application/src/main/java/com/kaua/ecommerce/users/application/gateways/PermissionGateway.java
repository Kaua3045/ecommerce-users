package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.permissions.Permission;

import java.util.Optional;

public interface PermissionGateway {

    Permission create(Permission aPermission);

    boolean existsByName(String aName);

    Optional<Permission> findById(String aId);

    void deleteById(String aId);
}

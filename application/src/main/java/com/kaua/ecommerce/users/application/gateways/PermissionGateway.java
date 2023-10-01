package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.permissions.Permission;

public interface PermissionGateway {

    Permission create(Permission aPermission);

    boolean existsByName(String aName);

    void deleteById(String aId);
}

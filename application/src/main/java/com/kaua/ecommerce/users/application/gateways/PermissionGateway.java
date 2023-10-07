package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.domain.permissions.Permission;

import java.util.Optional;
import java.util.Set;

public interface PermissionGateway {

    Permission create(Permission aPermission);

    boolean existsByName(String aName);

    Optional<Permission> findById(String aId);

    Pagination<Permission> findAll(SearchQuery aQuery);

    Permission update(Permission aPermission);

    void deleteById(String aId);

    Set<Permission> findAllByIds(Set<String> permissions);
}

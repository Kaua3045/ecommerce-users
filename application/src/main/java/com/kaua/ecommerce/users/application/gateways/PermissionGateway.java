package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.domain.permissions.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionGateway {

    Permission create(Permission aPermission);

    boolean existsByName(String aName);

    Optional<Permission> findById(String aId);

    Pagination<Permission> findAll(SearchQuery aQuery);

    Permission update(Permission aPermission);

    void deleteById(String aId);

    List<Permission> findAllByIds(List<String> permissions);
}

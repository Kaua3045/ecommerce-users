package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;

import java.util.Optional;

public interface RoleGateway {

    Role create(Role aRole);

    boolean existsByName(String aName);

    Optional<Role> findById(String aId);

    Optional<Role> findDefaultRole();

    Pagination<Role> findAll(SearchQuery aQuery);

    Role update(Role aRole);

    void deleteById(String aId);
}

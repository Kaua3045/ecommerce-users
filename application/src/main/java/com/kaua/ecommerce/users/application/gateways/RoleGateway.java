package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleSearchQuery;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;

import java.util.Optional;

public interface RoleGateway {

    Role create(Role aRole);

    boolean existsByName(String aName);

    Optional<Role> findById(String aId);

    Optional<Role> findDefaultRole();

    Pagination<Role> findAll(RoleSearchQuery aQuery);

    Role update(Role aRole);

    void deleteById(String aId);
}

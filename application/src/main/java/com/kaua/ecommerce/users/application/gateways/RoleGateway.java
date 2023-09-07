package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.roles.Role;

import java.util.Optional;

public interface RoleGateway {

    Role create(Role aRole);

    boolean existsByName(String aName);

    Optional<Role> findById(String aId);

    Role update(Role aRole);
}

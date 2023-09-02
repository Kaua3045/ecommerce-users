package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.roles.Role;

public interface RoleGateway {

    Role create(Role aRole);

    boolean existsByName(String aName);
}

package com.kaua.ecommerce.users.domain.roles;

import java.util.Arrays;
import java.util.Optional;

public enum RoleTypes {

    COMMON,
    EMPLOYEES;

    public static Optional<RoleTypes> of(final String aRoleType) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(aRoleType))
                .findFirst();
    }
}

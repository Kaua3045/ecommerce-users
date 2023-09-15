package com.kaua.ecommerce.users.domain.roles;

public record RoleSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}

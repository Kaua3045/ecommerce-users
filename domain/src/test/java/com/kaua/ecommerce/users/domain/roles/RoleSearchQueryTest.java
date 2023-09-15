package com.kaua.ecommerce.users.domain.roles;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoleSearchQueryTest {

    @Test
    void givenAValidValues_whenCallNewRoleSearchQuery_shouldReturnARoleSearchQueryInstancie() {
        final var page = 0;
        final var perPage = 10;
        final var terms = "";
        final var sort = "createdAt";
        final var direction = "asc";

        final var aQuery = new RoleSearchQuery(page, perPage, terms, sort, direction);

        Assertions.assertEquals(page, aQuery.page());
        Assertions.assertEquals(perPage, aQuery.perPage());
        Assertions.assertEquals(terms, aQuery.terms());
        Assertions.assertEquals(sort, aQuery.sort());
        Assertions.assertEquals(direction, aQuery.direction());
    }
}

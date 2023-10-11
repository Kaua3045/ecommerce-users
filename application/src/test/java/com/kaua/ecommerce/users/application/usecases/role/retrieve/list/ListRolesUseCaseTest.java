package com.kaua.ecommerce.users.application.usecases.role.retrieve.list;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListRolesUseCaseTest {

    @InjectMocks
    private DefaultListRolesUseCase useCase;

    @Mock
    private RoleGateway roleGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(roleGateway);
    }

    @Test
    void givenAValidQuery_whenCallListRoles_thenShouldReturnRoles() {
        final var roles = List.of(Role.newRole("admin", null, RoleTypes.EMPLOYEES, false),
                Role.newRole("User", "Common user", RoleTypes.COMMON, true));

        final var page = 0;
        final var perPage = 10;
        final var totalPages = 1;
        final var terms = "";
        final var sort = "createdAt";
        final var direction = "asc";

        final var aQuery = new SearchQuery(page, perPage, terms, sort, direction);
        final var pagination = new Pagination<>(page, perPage, totalPages, roles.size(), roles);

        final var itemsCount = 2;
        final var resultItems = pagination.map(ListRolesOutput::from);

        Mockito.when(roleGateway.findAll(aQuery)).thenReturn(pagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(itemsCount, actualResult.totalItems());
        Assertions.assertEquals(resultItems, actualResult);
        Assertions.assertEquals(page, actualResult.currentPage());
        Assertions.assertEquals(perPage, actualResult.perPage());
        Assertions.assertEquals(totalPages, actualResult.totalPages());
        Assertions.assertEquals(roles.size(), actualResult.items().size());
    }

    @Test
    void givenAValidQuery_whenHasNoResult_thenShouldReturnEmptyRoles() {
        final var roles = List.<Role>of();

        final var page = 0;
        final var perPage = 10;
        final var totalPages = 1;
        final var terms = "";
        final var sort = "createdAt";
        final var direction = "asc";

        final var aQuery = new SearchQuery(page, perPage, terms, sort, direction);
        final var pagination = new Pagination<>(page, perPage, totalPages, roles.size(), roles);

        final var itemsCount = 0;
        final var resultItems = pagination.map(ListRolesOutput::from);

        Mockito.when(roleGateway.findAll(aQuery)).thenReturn(pagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(itemsCount, actualResult.totalItems());
        Assertions.assertEquals(resultItems, actualResult);
        Assertions.assertEquals(page, actualResult.currentPage());
        Assertions.assertEquals(perPage, actualResult.perPage());
        Assertions.assertEquals(totalPages, actualResult.totalPages());
        Assertions.assertEquals(roles.size(), actualResult.items().size());
    }
}

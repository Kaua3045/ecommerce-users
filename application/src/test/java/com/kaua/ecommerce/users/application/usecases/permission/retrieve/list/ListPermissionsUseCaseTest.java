package com.kaua.ecommerce.users.application.usecases.permission.retrieve.list;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.application.usecases.permission.retrieve.list.DefaultListPermissionsUseCase;
import com.kaua.ecommerce.users.application.usecases.permission.retrieve.list.ListPermissionsOutput;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.domain.permissions.Permission;
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
public class ListPermissionsUseCaseTest {

    @InjectMocks
    private DefaultListPermissionsUseCase useCase;

    @Mock
    private PermissionGateway permissionGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(permissionGateway);
    }

    @Test
    void givenAValidQuery_whenCallListPermission_thenShouldReturnPermissions() {
        final var permissions = List.of(Permission.newPermission("create-an-admin-user", null),
                Permission.newPermission("create-a-customer-user", "Create a customer user"));

        final var page = 0;
        final var perPage = 10;
        final var totalPages = 1;
        final var terms = "";
        final var sort = "createdAt";
        final var direction = "asc";

        final var aQuery = new SearchQuery(page, perPage, terms, sort, direction);
        final var pagination = new Pagination<>(page, perPage, totalPages, permissions.size(), permissions);

        final var itemsCount = 2;
        final var resultItemsAfterCallUseCase = pagination.map(ListPermissionsOutput::from);

        Mockito.when(permissionGateway.findAll(aQuery)).thenReturn(pagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(itemsCount, actualResult.totalItems());
        Assertions.assertEquals(resultItemsAfterCallUseCase, actualResult);
        Assertions.assertEquals(page, actualResult.currentPage());
        Assertions.assertEquals(perPage, actualResult.perPage());
        Assertions.assertEquals(totalPages, actualResult.totalPages());
        Assertions.assertEquals(permissions.size(), actualResult.items().size());
    }

    @Test
    void givenAValidQuery_whenHasNoResult_thenShouldReturnEmptyPermissions() {
        final var permissions = List.<Permission>of();

        final var page = 0;
        final var perPage = 10;
        final var totalPages = 1;
        final var terms = "";
        final var sort = "createdAt";
        final var direction = "asc";

        final var aQuery = new SearchQuery(page, perPage, terms, sort, direction);
        final var pagination = new Pagination<>(page, perPage, totalPages, permissions.size(), permissions);

        final var itemsCount = 0;
        final var resultItemsAfterCallUseCase = pagination.map(ListPermissionsOutput::from);

        Mockito.when(permissionGateway.findAll(aQuery)).thenReturn(pagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(itemsCount, actualResult.totalItems());
        Assertions.assertEquals(resultItemsAfterCallUseCase, actualResult);
        Assertions.assertEquals(page, actualResult.currentPage());
        Assertions.assertEquals(perPage, actualResult.perPage());
        Assertions.assertEquals(totalPages, actualResult.totalPages());
        Assertions.assertEquals(permissions.size(), actualResult.items().size());
    }
}

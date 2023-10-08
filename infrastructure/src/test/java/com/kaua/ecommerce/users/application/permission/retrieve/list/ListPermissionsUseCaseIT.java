package com.kaua.ecommerce.users.application.permission.retrieve.list;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.application.usecases.permission.retrieve.list.ListPermissionsUseCase;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class ListPermissionsUseCaseIT {

    @Autowired
    private ListPermissionsUseCase listPermissionsUseCase;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @BeforeEach
    void mockUp() {
        final var permissions = List.of(
                PermissionJpaEntity.toEntity(Permission.newPermission("customer-all", "Customer all")),
                PermissionJpaEntity.toEntity(Permission.newPermission("admin-all", "Admin all")),
                PermissionJpaEntity.toEntity(Permission.newPermission("ceo", null))
        );

        permissionRepository.saveAllAndFlush(permissions);
    }

    @Test
    void givenAValidTerm_whenTermDoesNotMatchsPrePersisted_shouldReturnEmptyPage() {
        final var aPage = 0;
        final var aPerPage = 10;
        final var aTerm = "invalid";
        final var aSort = "name";
        final var aDirection = "asc";
        final var aItemsCount = 0;
        final var aTotalItems = 0;
        final var aTotalPage = 0;

        final var aQuery = new SearchQuery(
                aPage,
                aPerPage,
                aTerm,
                aSort,
                aDirection
        );

        final var actualResult = this.listPermissionsUseCase.execute(aQuery);

        Assertions.assertEquals(aItemsCount, actualResult.items().size());
        Assertions.assertEquals(aTotalItems, actualResult.totalItems());
        Assertions.assertEquals(aTotalPage, actualResult.totalPages());
        Assertions.assertEquals(aPage, actualResult.currentPage());
        Assertions.assertEquals(aPerPage, actualResult.perPage());
    }

    @ParameterizedTest
    @CsvSource({
            "cust,0,10,1,1,1,customer-all",
            "adm,0,10,1,1,1,admin-all",
            "ce,0,10,1,1,1,ceo"
    })
    void givenAValidTerm_whenCallListPermission_shouldReturnPermissionFiltered(
            final String aTerms,
            final int aPage,
            final int aPerPage,
            final int aItemsCount,
            final int aTotalItems,
            final int aTotalPages,
            final String aPermissionName
    ) {
        final var aSort = "name";
        final var aDirection = "asc";

        final var aQuery = new SearchQuery(
                aPage,
                aPerPage,
                aTerms,
                aSort,
                aDirection
        );

        final var actualResult = this.listPermissionsUseCase.execute(aQuery);

        Assertions.assertEquals(aItemsCount, actualResult.items().size());
        Assertions.assertEquals(aTotalItems, actualResult.totalItems());
        Assertions.assertEquals(aPage, actualResult.currentPage());
        Assertions.assertEquals(aPerPage, actualResult.perPage());
        Assertions.assertEquals(aTotalPages, actualResult.totalPages());
        Assertions.assertEquals(aPermissionName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,3,admin-all",
            "1,1,1,3,3,ceo",
            "2,1,1,3,3,customer-all"
    })
    void givenAValidPage_whenCallListPermissions_shouldReturnPermissionsPaginated(
            final int aPage,
            final int aPerPage,
            final int aItemsCount,
            final int aTotalItems,
            final int aTotalPages,
            final String aPermissionName
    ) {
        final var aSort = "name";
        final var aDirection = "asc";
        final var aTerms = "";

        final var aQuery = new SearchQuery(
                aPage,
                aPerPage,
                aTerms,
                aSort,
                aDirection
        );

        final var actualResult = this.listPermissionsUseCase.execute(aQuery);

        Assertions.assertEquals(aItemsCount, actualResult.items().size());
        Assertions.assertEquals(aTotalItems, actualResult.totalItems());
        Assertions.assertEquals(aPage, actualResult.currentPage());
        Assertions.assertEquals(aPerPage, actualResult.perPage());
        Assertions.assertEquals(aTotalPages, actualResult.totalPages());
        Assertions.assertEquals(aPermissionName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,3,3,1,admin-all",
            "name,desc,0,10,3,3,1,customer-all",
            "createdAt,asc,0,10,3,3,1,customer-all",
            "createdAt,desc,0,10,3,3,1,ceo"
    })
    void givenAValidSortAndDirection_whenCallListPermissions_shouldReturnPermissionsOrdered(
            final String aSort,
            final String aDirection,
            final int aPage,
            final int aPerPage,
            final int aItemsCount,
            final int aTotalItems,
            final int aTotalPages,
            final String aPermissionName
    ) {
        final var aTerms = "";

        final var aQuery = new SearchQuery(
                aPage,
                aPerPage,
                aTerms,
                aSort,
                aDirection
        );

        final var actualResult = this.listPermissionsUseCase.execute(aQuery);

        Assertions.assertEquals(aItemsCount, actualResult.items().size());
        Assertions.assertEquals(aTotalItems, actualResult.totalItems());
        Assertions.assertEquals(aPage, actualResult.currentPage());
        Assertions.assertEquals(aPerPage, actualResult.perPage());
        Assertions.assertEquals(aTotalPages, actualResult.totalPages());
        Assertions.assertEquals(aPermissionName, actualResult.items().get(0).name());
    }
}

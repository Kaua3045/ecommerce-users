package com.kaua.ecommerce.users.application.role.retrieve.list;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleSearchQuery;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class ListRolesUseCaseIT {

    @Autowired
    private ListRolesUseCase listRolesUseCase;

    @Autowired
    private RoleJpaRepository roleRepository;

    @BeforeEach
    void mockUp() {
        final var roles = List.of(
                RoleJpaEntity.toEntity(Role.newRole("User", null, RoleTypes.COMMON, true)),
                RoleJpaEntity.toEntity(Role.newRole("Admin", null, RoleTypes.EMPLOYEES, false)),
                RoleJpaEntity.toEntity(Role.newRole("ceo", "Chief Executive Officer", RoleTypes.EMPLOYEES, false))
        );

        roleRepository.saveAllAndFlush(roles);
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

        final var aQuery = new RoleSearchQuery(
                aPage,
                aPerPage,
                aTerm,
                aSort,
                aDirection
        );

        final var actualResult = this.listRolesUseCase.execute(aQuery);

        Assertions.assertEquals(aItemsCount, actualResult.items().size());
        Assertions.assertEquals(aTotalItems, actualResult.totalItems());
        Assertions.assertEquals(aTotalPage, actualResult.totalPages());
        Assertions.assertEquals(aPage, actualResult.currentPage());
        Assertions.assertEquals(aPerPage, actualResult.perPage());
    }

    @ParameterizedTest
    @CsvSource({
            "use,0,10,1,1,1,User",
            "adm,0,10,1,1,1,Admin",
            "ce,0,10,1,1,1,ceo"
    })
    void givenAValidTerm_whenCallListRoles_shouldReturnRolesFiltered(
            final String aTerms,
            final int aPage,
            final int aPerPage,
            final int aItemsCount,
            final int aTotalItems,
            final int aTotalPages,
            final String aRoleName
    ) {
        final var aSort = "name";
        final var aDirection = "asc";

        final var aQuery = new RoleSearchQuery(
                aPage,
                aPerPage,
                aTerms,
                aSort,
                aDirection
        );

        final var actualResult = this.listRolesUseCase.execute(aQuery);

        Assertions.assertEquals(aItemsCount, actualResult.items().size());
        Assertions.assertEquals(aTotalItems, actualResult.totalItems());
        Assertions.assertEquals(aPage, actualResult.currentPage());
        Assertions.assertEquals(aPerPage, actualResult.perPage());
        Assertions.assertEquals(aTotalPages, actualResult.totalPages());
        Assertions.assertEquals(aRoleName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,3,3,1,Admin",
            "name,desc,0,10,3,3,1,ceo",
            "createdAt,asc,0,10,3,3,1,User",
            "createdAt,desc,0,10,3,3,1,ceo",
            "roleType,asc,0,10,3,3,1,User",
            "roleType,desc,0,10,3,3,1,Admin"
    })
    void givenAValidSortAndDirection_whenCallListRoles_shouldReturnRolesOrdered(
            final String aSort,
            final String aDirection,
            final int aPage,
            final int aPerPage,
            final int aItemsCount,
            final int aTotalItems,
            final int aTotalPages,
            final String aRoleName
    ) {
        final var aTerms = "";

        final var aQuery = new RoleSearchQuery(
                aPage,
                aPerPage,
                aTerms,
                aSort,
                aDirection
        );

        final var actualResult = this.listRolesUseCase.execute(aQuery);

        Assertions.assertEquals(aItemsCount, actualResult.items().size());
        Assertions.assertEquals(aTotalItems, actualResult.totalItems());
        Assertions.assertEquals(aPage, actualResult.currentPage());
        Assertions.assertEquals(aPerPage, actualResult.perPage());
        Assertions.assertEquals(aTotalPages, actualResult.totalPages());
        Assertions.assertEquals(aRoleName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,3,Admin",
            "1,1,1,3,3,User",
            "2,1,1,3,3,ceo"
    })
    void givenAValidPage_whenCallListRoles_shouldReturnRolesPaginated(
            final int aPage,
            final int aPerPage,
            final int aItemsCount,
            final int aTotalItems,
            final int aTotalPages,
            final String aRoleName
    ) {
        final var aSort = "name";
        final var aDirection = "asc";
        final var aTerms = "";

        final var aQuery = new RoleSearchQuery(
                aPage,
                aPerPage,
                aTerms,
                aSort,
                aDirection
        );

        final var actualResult = this.listRolesUseCase.execute(aQuery);

        Assertions.assertEquals(aItemsCount, actualResult.items().size());
        Assertions.assertEquals(aTotalItems, actualResult.totalItems());
        Assertions.assertEquals(aPage, actualResult.currentPage());
        Assertions.assertEquals(aPerPage, actualResult.perPage());
        Assertions.assertEquals(aTotalPages, actualResult.totalPages());
        Assertions.assertEquals(aRoleName, actualResult.items().get(0).name());
    }
}

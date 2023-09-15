package com.kaua.ecommerce.users.application.role.delete;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleID;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class DeleteRoleUseCaseIT {

    @Autowired
    private DeleteRoleUseCase deleteRoleUseCase;

    @Autowired
    private RoleJpaRepository roleRepository;

    @Test
    void givenAValidId_whenCallDeleteRole_shouldBeOk() {
        final var aRole = Role.newRole(
                "ceo",
                "Chief Executive Officer",
                RoleTypes.EMPLOYEES
        );
        final var aId = aRole.getId().getValue();

        roleRepository.saveAndFlush(RoleJpaEntity.toEntity(aRole));

        Assertions.assertEquals(1, roleRepository.count());

        Assertions.assertDoesNotThrow(() -> this.deleteRoleUseCase.execute(DeleteRoleCommand.with(aId)));

        Assertions.assertEquals(0, roleRepository.count());
    }

    @Test
    void givenAInvalidId_whenCallDeleteRole_shouldBeOk() {
        final var aId = RoleID.from("123").getValue();

        Assertions.assertEquals(0, roleRepository.count());

        Assertions.assertDoesNotThrow(() -> this.deleteRoleUseCase.execute(DeleteRoleCommand.with(aId)));

        Assertions.assertEquals(0, roleRepository.count());
    }
}

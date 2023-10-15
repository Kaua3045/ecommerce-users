package com.kaua.ecommerce.users.application.role.delete;

import com.kaua.ecommerce.users.CacheGatewayTest;
import com.kaua.ecommerce.users.application.usecases.role.delete.DeleteRoleCommand;
import com.kaua.ecommerce.users.application.usecases.role.delete.DeleteRoleUseCase;
import com.kaua.ecommerce.users.config.CacheTestConfiguration;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleID;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

@CacheGatewayTest
public class DeleteRoleUseCaseIT extends CacheTestConfiguration {

    @BeforeEach
    void setup() {
        init();
    }

    @AfterEach
    void cleanUp() {
        stop();
    }

    @Autowired
    private DeleteRoleUseCase deleteRoleUseCase;

    @Autowired
    private RoleJpaRepository roleRepository;

    @Test
    void givenAValidId_whenCallDeleteRole_shouldBeOk() {
        final var aRole = Role.newRole(
                "ceo",
                "Chief Executive Officer",
                RoleTypes.EMPLOYEES,
                false
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

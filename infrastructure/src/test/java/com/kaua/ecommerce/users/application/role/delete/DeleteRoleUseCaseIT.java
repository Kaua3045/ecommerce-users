package com.kaua.ecommerce.users.application.role.delete;

import com.kaua.ecommerce.users.CacheGatewayTest;
import com.kaua.ecommerce.users.application.usecases.role.delete.DeleteRoleCommand;
import com.kaua.ecommerce.users.application.usecases.role.delete.DeleteRoleUseCase;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleID;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@CacheGatewayTest
@Testcontainers
public class DeleteRoleUseCaseIT {

    @Container
    private static final GenericContainer<?> redis = new GenericContainer<>(
            DockerImageName.parse("redis:alpine"))
            .withExposedPorts(6379)
            .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*", 1));

    @DynamicPropertySource
    public static void redisProperties(final DynamicPropertyRegistry propertySources) {
        propertySources.add("redis.hosts", redis::getHost);
        propertySources.add("redis.ports", redis::getFirstMappedPort);
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

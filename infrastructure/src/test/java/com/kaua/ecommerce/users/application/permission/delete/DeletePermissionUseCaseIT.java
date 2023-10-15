package com.kaua.ecommerce.users.application.permission.delete;

import com.kaua.ecommerce.users.CacheGatewayTest;
import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.application.usecases.permission.delete.DeletePermissionCommand;
import com.kaua.ecommerce.users.application.usecases.permission.delete.DeletePermissionUseCase;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.domain.permissions.PermissionID;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
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
public class DeletePermissionUseCaseIT {

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
    private DeletePermissionUseCase deletePermissionUseCase;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @Test
    void givenAValidId_whenCallDeletePermission_shouldBeOk() {
        final var aPermission = Permission.newPermission("create-role", "create a new role");
        final var aId = aPermission.getId().getValue();

        permissionRepository.saveAndFlush(PermissionJpaEntity.toEntity(aPermission));

        Assertions.assertEquals(1, permissionRepository.count());

        Assertions.assertDoesNotThrow(() -> this.deletePermissionUseCase.execute(DeletePermissionCommand.with(aId)));

        Assertions.assertEquals(0, permissionRepository.count());
    }

    @Test
    void givenAInvalidId_whenCallDeletePermission_shouldBeOk() {
        final var aId = PermissionID.from("123").getValue();

        Assertions.assertEquals(0, permissionRepository.count());

        Assertions.assertDoesNotThrow(() -> this.deletePermissionUseCase.execute(DeletePermissionCommand.with(aId)));

        Assertions.assertEquals(0, permissionRepository.count());
    }
}

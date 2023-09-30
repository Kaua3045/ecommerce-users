package com.kaua.ecommerce.users.infrastructure.permission;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.infrastructure.permissions.PermissionMySQLGateway;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class PermissionMySQLGatewayTest {

    @Autowired
    private PermissionMySQLGateway permissionGateway;

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @Test
    void givenAValidPermissionWithDescription_whenCallCreate_shouldReturnANewPermission() {
        final var aName = "create-role";
        final var aDescription = "Create a new role";

        final var aPermission = Permission.newPermission(aName, aDescription);

        Assertions.assertEquals(0, permissionRepository.count());

        final var actualPermission = permissionGateway.create(aPermission);

        Assertions.assertEquals(1, permissionRepository.count());

        Assertions.assertEquals(aPermission.getId(), actualPermission.getId());
        Assertions.assertEquals(aPermission.getName(), actualPermission.getName());
        Assertions.assertEquals(aPermission.getDescription(), actualPermission.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualPermission.getCreatedAt());
        Assertions.assertEquals(aPermission.getUpdatedAt(), actualPermission.getUpdatedAt());

        final var actualEntity = permissionRepository.findById(actualPermission.getId().getValue()).get();

        Assertions.assertEquals(aPermission.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aName, actualEntity.getName());
        Assertions.assertEquals(aDescription, actualEntity.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aPermission.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    void givenAValidPermissionWithNullDescription_whenCallCreate_shouldReturnANewPermission() {
        final var aName = "create-role";
        final String aDescription = null;

        final var aPermission = Permission.newPermission(aName, aDescription);

        Assertions.assertEquals(0, permissionRepository.count());

        final var actualPermission = permissionGateway.create(aPermission);

        Assertions.assertEquals(1, permissionRepository.count());

        Assertions.assertEquals(aPermission.getId(), actualPermission.getId());
        Assertions.assertEquals(aPermission.getName(), actualPermission.getName());
        Assertions.assertEquals(aPermission.getDescription(), actualPermission.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualPermission.getCreatedAt());
        Assertions.assertEquals(aPermission.getUpdatedAt(), actualPermission.getUpdatedAt());

        final var actualEntity = permissionRepository.findById(actualPermission.getId().getValue()).get();

        Assertions.assertEquals(aPermission.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aName, actualEntity.getName());
        Assertions.assertEquals(aDescription, actualEntity.getDescription());
        Assertions.assertEquals(aPermission.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aPermission.getUpdatedAt(), actualEntity.getUpdatedAt());
    }


    @Test
    void givenAValidNameButNotExistis_whenCallExistsByName_shouldReturnFalse() {
        final var aName = "create-role";

        Assertions.assertEquals(0, permissionRepository.count());
        Assertions.assertFalse(permissionGateway.existsByName(aName));
    }

    @Test
    void givenAValidExistingName_whenCallExistsByName_shouldReturnTrue() {
        final var aName = "create-role";
        final var aDescription = "Create a new role";

        final var aPermission = Permission.newPermission(aName, aDescription);

        Assertions.assertEquals(0, permissionRepository.count());
        permissionRepository.save(PermissionJpaEntity.toEntity(aPermission));
        Assertions.assertEquals(1, permissionRepository.count());

        Assertions.assertTrue(permissionGateway.existsByName(aName));
    }

    @Test
    void givenAPrePersistedPermission_whenCallDeleteById_shouldBeOk() {
        final var aPermission = Permission.newPermission("create-role", "Create a new role");
        final var aId = aPermission.getId().getValue();

        permissionRepository.saveAndFlush(PermissionJpaEntity.toEntity(aPermission));

        Assertions.assertEquals(1, permissionRepository.count());

        Assertions.assertDoesNotThrow(() -> permissionGateway.deleteById(aId));

        Assertions.assertEquals(0, permissionRepository.count());
    }

    @Test
    void givenAnNotPrePersistedPermission_whenCallDeleteById_shouldBeOk() {
        final var aId = "123";

        Assertions.assertEquals(0, permissionRepository.count());

        Assertions.assertDoesNotThrow(() -> permissionGateway.deleteById(aId));

        Assertions.assertEquals(0, permissionRepository.count());
    }
}

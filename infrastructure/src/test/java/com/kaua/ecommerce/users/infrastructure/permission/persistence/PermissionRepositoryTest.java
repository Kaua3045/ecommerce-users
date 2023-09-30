package com.kaua.ecommerce.users.infrastructure.permission.persistence;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import org.hibernate.PropertyValueException;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

@IntegrationTest
public class PermissionRepositoryTest {

    @Autowired
    private PermissionJpaRepository permissionRepository;

    @Test
    void givenAnInvalidNullName_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "name";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity.name";

        final var aPermission = Permission.newPermission(
                "create-role",
                "Create a new role"
        );

        final var aEntity = PermissionJpaEntity.toEntity(aPermission);
        aEntity.setName(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> permissionRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidCreatedAt_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "createdAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity.createdAt";

        final var aPermission = Permission.newPermission(
                "create-role",
                "Create a new role"
        );

        final var aEntity = PermissionJpaEntity.toEntity(aPermission);
        aEntity.setCreatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> permissionRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidUpdatedAt_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "updatedAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity.updatedAt";

        final var aPermission = Permission.newPermission(
                "create-role",
                "Create a new role"
        );

        final var aEntity = PermissionJpaEntity.toEntity(aPermission);
        aEntity.setUpdatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> permissionRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAValidNullDescription_whenCallSave_shouldDoesNotThrowException() {
        final var aPermission = Permission.newPermission(
                "create-role",
                "Create a new role"
        );

        final var aEntity = PermissionJpaEntity.toEntity(aPermission);
        aEntity.setDescription(null);

        final var actualOutput = Assertions.assertDoesNotThrow(() -> permissionRepository.save(aEntity));

        Assertions.assertEquals(aEntity.getDescription(), actualOutput.getDescription());
    }

    @Test
    void givenAnInvalidNullId_whenCallSave_shouldReturnAnException() {
        final var expectedErrorMessage = "ids for this class must be manually assigned before calling save(): com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity";

        final var aPermission = Permission.newPermission(
                "create-role",
                "Create a new role"
        );

        final var aEntity = PermissionJpaEntity.toEntity(aPermission);
        aEntity.setId(null);

        final var actualException = Assertions.assertThrows(JpaSystemException.class,
                () -> permissionRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(IdentifierGenerationException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }
}

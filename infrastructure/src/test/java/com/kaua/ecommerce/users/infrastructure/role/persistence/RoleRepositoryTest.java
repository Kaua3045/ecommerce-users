package com.kaua.ecommerce.users.infrastructure.role.persistence;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

@IntegrationTest
public class RoleRepositoryTest {

    @Autowired
    private RoleJpaRepository roleRepository;

    @Test
    void givenAnInvalidNullName_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "name";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity.name";

        final var aRole = Role.newRole(
                "ceo",
                "Chief Executive Officer",
                RoleTypes.EMPLOYEES
        );

        final var aEntity = RoleJpaEntity.toEntity(aRole);
        aEntity.setName(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> roleRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnExistingName_whenCallSave_shouldReturnAnException() {
        final var expectedErrorMessage = "could not execute statement [Unique index or primary key violation: \"public.CONSTRAINT_INDEX_6 ON public.roles(name NULLS FIRST) VALUES ( /* 1 */ 'ceo' )\"; SQL statement:\n" +
                "insert into roles (created_at,description,name,role_type,updated_at,role_id) values (?,?,?,?,?,?) [23505-214]] [insert into roles (created_at,description,name,role_type,updated_at,role_id) values (?,?,?,?,?,?)]";

        final var aRoleOne = Role.newRole(
                "ceo",
                "Chief Executive Officer",
                RoleTypes.EMPLOYEES
        );

        final var aRoleTwo = Role.newRole(
                "ceo",
                "Chief Executive Officer",
                RoleTypes.EMPLOYEES
        );

        final var aEntityOne = RoleJpaEntity.toEntity(aRoleOne);
        roleRepository.save(aEntityOne);

        final var aEntityTwo = RoleJpaEntity.toEntity(aRoleTwo);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> roleRepository.save(aEntityTwo));

        final var actualCause = Assertions.assertInstanceOf(ConstraintViolationException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullRoleType_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "roleType";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity.roleType";

        final var aRole = Role.newRole(
                "ceo",
                "Chief Executive Officer",
                RoleTypes.EMPLOYEES
        );

        final var aEntity = RoleJpaEntity.toEntity(aRole);
        aEntity.setRoleType(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> roleRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidCreatedAt_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "createdAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity.createdAt";

        final var aRole = Role.newRole(
                "ceo",
                "Chief Executive Officer",
                RoleTypes.EMPLOYEES
        );

        final var aEntity = RoleJpaEntity.toEntity(aRole);
        aEntity.setCreatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> roleRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidUpdatedAt_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "updatedAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity.updatedAt";

        final var aRole = Role.newRole(
                "ceo",
                "Chief Executive Officer",
                RoleTypes.EMPLOYEES
        );

        final var aEntity = RoleJpaEntity.toEntity(aRole);
        aEntity.setUpdatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> roleRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAValidNullDescription_whenCallSave_shouldDoesNotThrowException() {
        final var aRole = Role.newRole(
                "ceo",
                "Chief Executive Officer",
                RoleTypes.EMPLOYEES
        );

        final var aEntity = RoleJpaEntity.toEntity(aRole);
        aEntity.setDescription(null);

        final var actualOutput = Assertions.assertDoesNotThrow(() -> roleRepository.save(aEntity));

        Assertions.assertEquals(aEntity.getDescription(), actualOutput.getDescription());
    }

    @Test
    void givenAnInvalidNullId_whenCallSave_shouldReturnAnException() {
        final var expectedErrorMessage = "ids for this class must be manually assigned before calling save(): com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity";

        final var aRole = Role.newRole(
                "ceo",
                "Chief Executive Officer",
                RoleTypes.EMPLOYEES
        );

        final var aEntity = RoleJpaEntity.toEntity(aRole);
        aEntity.setId(null);

        final var actualException = Assertions.assertThrows(JpaSystemException.class,
                () -> roleRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(IdentifierGenerationException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }
}

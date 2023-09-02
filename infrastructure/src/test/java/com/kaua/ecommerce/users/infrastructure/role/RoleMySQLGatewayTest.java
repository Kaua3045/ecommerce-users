package com.kaua.ecommerce.users.infrastructure.role;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import com.kaua.ecommerce.users.infrastructure.roles.RoleMySQLGateway;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class RoleMySQLGatewayTest {

    @Autowired
    private RoleMySQLGateway roleGateway;

    @Autowired
    private RoleJpaRepository roleRepository;

    @Test
    void givenAValidRoleWithDescription_whenCallCreate_shouldReturnANewRole() {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;

        final var aRole = Role.newRole(aName, aDescription, aRoleType);

        Assertions.assertEquals(0, roleRepository.count());

        final var actualRole = roleGateway.create(aRole);

        Assertions.assertEquals(1, roleRepository.count());

        Assertions.assertEquals(aRole.getId(), actualRole.getId());
        Assertions.assertEquals(aRole.getName(), actualRole.getName());
        Assertions.assertEquals(aRole.getDescription(), actualRole.getDescription());
        Assertions.assertEquals(aRole.getRoleType(), actualRole.getRoleType());
        Assertions.assertEquals(aRole.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertEquals(aRole.getUpdatedAt(), actualRole.getUpdatedAt());

        final var actualEntity = roleRepository.findById(actualRole.getId().getValue()).get();

        Assertions.assertEquals(aRole.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aName, actualEntity.getName());
        Assertions.assertEquals(aDescription, actualEntity.getDescription());
        Assertions.assertEquals(aRoleType, actualEntity.getRoleType());
        Assertions.assertEquals(aRole.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aRole.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    void givenAValidRoleWithNullDescription_whenCallCreate_shouldReturnANewRole() {
        final var aName = "ceo";
        final String aDescription = null;
        final var aRoleType = RoleTypes.EMPLOYEES;

        final var aRole = Role.newRole(aName, aDescription, aRoleType);

        Assertions.assertEquals(0, roleRepository.count());

        final var actualRole = roleGateway.create(aRole);

        Assertions.assertEquals(1, roleRepository.count());

        Assertions.assertEquals(aRole.getId(), actualRole.getId());
        Assertions.assertEquals(aRole.getName(), actualRole.getName());
        Assertions.assertEquals(aRole.getDescription(), actualRole.getDescription());
        Assertions.assertEquals(aRole.getRoleType(), actualRole.getRoleType());
        Assertions.assertEquals(aRole.getCreatedAt(), actualRole.getCreatedAt());
        Assertions.assertEquals(aRole.getUpdatedAt(), actualRole.getUpdatedAt());

        final var actualEntity = roleRepository.findById(actualRole.getId().getValue()).get();

        Assertions.assertEquals(aRole.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aName, actualEntity.getName());
        Assertions.assertEquals(aDescription, actualEntity.getDescription());
        Assertions.assertEquals(aRoleType, actualEntity.getRoleType());
        Assertions.assertEquals(aRole.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aRole.getUpdatedAt(), actualEntity.getUpdatedAt());
    }


    @Test
    void givenAValidNameButNotExistis_whenCallExistsByName_shouldReturnFalse() {
        final var aName = "ceo";

        Assertions.assertEquals(0, roleRepository.count());
        Assertions.assertFalse(roleRepository.existsByName(aName));
    }

    @Test
    void givenAValidExistingName_whenCallExistsByName_shouldReturnTrue() {
        final var aName = "ceo";
        final var aDescription = "Chief Executive Officer";
        final var aRoleType = RoleTypes.EMPLOYEES;

        final var aRole = Role.newRole(aName, aDescription, aRoleType);

        Assertions.assertEquals(0, roleRepository.count());
        roleRepository.save(RoleJpaEntity.toEntity(aRole));
        Assertions.assertEquals(1, roleRepository.count());

        Assertions.assertTrue(roleGateway.existsByName(aName));
    }
}

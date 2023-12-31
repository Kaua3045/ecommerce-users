package com.kaua.ecommerce.users.infrastructure.role.persistence;

import com.kaua.ecommerce.users.infrastructure.roles.persistence.RolePermissionID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RolePermissionIDTest {

    @Test
    void testEqualsSameObject() {
        final var aRolePermissionIdOne = RolePermissionID.from("1", "2");
        Assertions.assertTrue(aRolePermissionIdOne.equals(aRolePermissionIdOne));
        Assertions.assertNotNull(aRolePermissionIdOne.hashCode());
    }

    @Test
    void testEqualsNullAndNotSameClass() {
        final var aRolePermissionIdOne = RolePermissionID.from("1", "2");
        Assertions.assertFalse(aRolePermissionIdOne.equals(null));
        Assertions.assertFalse(aRolePermissionIdOne.equals("NotARolePermissionID"));
    }

    @Test
    void testEqualsBothTrue() {
        final var id1 = RolePermissionID.from("role1", "permission1");
        final var id2 = RolePermissionID.from("role1", "permission1");
        Assertions.assertTrue(id1.equals(id2));
    }

    @Test
    void testEqualsRoleIdTrueButPermissionIdFalse() {
        final var id1 = RolePermissionID.from("role1", "permission1");
        final var id2 = RolePermissionID.from("role1", "permission2");
        Assertions.assertFalse(id1.equals(id2));
    }

    @Test
    void testEqualsPermissionIdTrueButRoleIdFalse() {
        final var id1 = RolePermissionID.from("role1", "permission1");
        final var id2 = RolePermissionID.from("role2", "permission1");
        Assertions.assertFalse(id1.equals(id2));
    }

    @Test
    void testEqualsBothFalse() {
        final var id1 = RolePermissionID.from("role1", "permission1");
        final var id2 = RolePermissionID.from("role2", "permission2");
        Assertions.assertFalse(id1.equals(id2));
    }
}

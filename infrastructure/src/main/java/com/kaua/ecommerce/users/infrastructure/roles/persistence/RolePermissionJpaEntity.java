package com.kaua.ecommerce.users.infrastructure.roles.persistence;

import com.kaua.ecommerce.users.domain.roles.RolePermission;
import jakarta.persistence.*;

@Entity
@Table(name = "roles_permissions")
public class RolePermissionJpaEntity {

    @EmbeddedId
    private RolePermissionID id;

    @ManyToOne
    @MapsId("roleId")
    private RoleJpaEntity role;

    @Column(name = "permission_name", nullable = false)
    private String permissionName;

    public RolePermissionJpaEntity() {}

    private RolePermissionJpaEntity(final RoleJpaEntity role, final RolePermission rolePermission) {
        this.id = RolePermissionID.from(role.getId(), rolePermission.getPermissionID().getValue());
        this.role = role;
        this.permissionName = rolePermission.getPermissionName();
    }

    public static RolePermissionJpaEntity from(final RoleJpaEntity role, final RolePermission rolePermission) {
        return new RolePermissionJpaEntity(role, rolePermission);
    }

    public RolePermissionID getId() {
        return id;
    }

    public RoleJpaEntity getRole() {
        return role;
    }

    public String getPermissionName() {
        return permissionName;
    }
}

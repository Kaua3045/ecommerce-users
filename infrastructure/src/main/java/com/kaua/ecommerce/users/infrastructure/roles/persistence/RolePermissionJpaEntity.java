package com.kaua.ecommerce.users.infrastructure.roles.persistence;

import com.kaua.ecommerce.users.domain.roles.RolePermission;
import jakarta.persistence.*;

import java.util.Objects;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RolePermissionJpaEntity that = (RolePermissionJpaEntity) o;
        return getId().equals(that.getId()) && getRole().equals(that.getRole()) && getPermissionName().equals(that.getPermissionName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRole(), getPermissionName());
    }

    public RolePermissionID getId() {
        return id;
    }

    public void setId(RolePermissionID id) {
        this.id = id;
    }

    public RoleJpaEntity getRole() {
        return role;
    }

    public void setRole(RoleJpaEntity role) {
        this.role = role;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}

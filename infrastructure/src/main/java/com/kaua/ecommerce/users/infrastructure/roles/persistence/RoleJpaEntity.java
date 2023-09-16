package com.kaua.ecommerce.users.infrastructure.roles.persistence;

import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "roles")
public class RoleJpaEntity {

    @Id
    @Column(name = "role_id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "role_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleTypes roleType;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    private RoleJpaEntity() {}

    private RoleJpaEntity(
            final String id,
            final String name,
            final String description,
            final RoleTypes roleType,
            final boolean isDefault,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.roleType = roleType;
        this.isDefault = isDefault;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RoleJpaEntity toEntity(final Role aRole) {
        return new RoleJpaEntity(
                aRole.getId().getValue(),
                aRole.getName(),
                aRole.getDescription(),
                aRole.getRoleType(),
                aRole.isDefault(),
                aRole.getCreatedAt(),
                aRole.getUpdatedAt()
        );
    }

    public Role toDomain() {
        return Role.with(
                getId(),
                getName(),
                getDescription(),
                getRoleType(),
                isDefault(),
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoleTypes getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleTypes roleType) {
        this.roleType = roleType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

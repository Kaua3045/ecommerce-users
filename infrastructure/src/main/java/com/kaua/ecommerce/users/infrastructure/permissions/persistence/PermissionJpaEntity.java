package com.kaua.ecommerce.users.infrastructure.permissions.persistence;

import com.kaua.ecommerce.users.domain.permissions.Permission;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity(name = "Permission")
@Table(name = "permissions")
public class PermissionJpaEntity {

    @Id
    @Column(name = "permission_id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    private PermissionJpaEntity() {}

    private PermissionJpaEntity(
            final String id,
            final String name,
            final String description,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public PermissionJpaEntity(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static PermissionJpaEntity toEntity(final Permission aPermission) {
        return new PermissionJpaEntity(
                aPermission.getId().getValue(),
                aPermission.getName(),
                aPermission.getDescription(),
                aPermission.getCreatedAt(),
                aPermission.getUpdatedAt()
        );
    }

    public Permission toDomain() {
        return Permission.with(
                getId(),
                getName(),
                getDescription(),
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

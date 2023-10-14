package com.kaua.ecommerce.users.infrastructure.roles.persistence;

import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RolePermission;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RedisHash(value = "role", timeToLive = 60 * 60 * 24 * 7) // 7 days
public class RoleCacheEntity {

    @Id
    private String id;

    private String name;
    private String description;
    private RoleTypes type;
    private boolean isDefault;
    private Set<String> permissions;
    private Instant createdAt;
    private Instant updatedAt;

    public RoleCacheEntity() {}

    private RoleCacheEntity(
            final String id,
            final String name,
            final String description,
            final RoleTypes type,
            final boolean isDefault,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.isDefault = isDefault;
        this.permissions = new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RoleCacheEntity toEntity(final Role aRole) {
        final var aEntity = new RoleCacheEntity(
                aRole.getId().getValue(),
                aRole.getName(),
                aRole.getDescription(),
                aRole.getRoleType(),
                aRole.isDefault(),
                aRole.getCreatedAt(),
                aRole.getUpdatedAt()
        );

        aRole.getPermissions().forEach(permission -> aEntity.getPermissions()
                .add(permission.getPermissionName()));

        return aEntity;
    }

    public Role toDomain() {
        return Role.with(
                getId(),
                getName(),
                getDescription(),
                getType(),
                isDefault(),
                getRolePermissions(),
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public RoleTypes getType() {
        return type;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public Set<String> getPermissions() {
        return permissions == null ? new HashSet<>() : permissions;
    }

    public Set<RolePermission> getRolePermissions() {
        return getPermissions().stream()
                .map(it -> RolePermission
                        .newRolePermission(
                                null, it))
                .collect(Collectors.toSet());
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

package com.kaua.ecommerce.users.domain.roles;

import com.kaua.ecommerce.users.domain.AggregateRoot;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Role extends AggregateRoot<RoleID> {

    private String name;
    private String description;
    private RoleTypes roleType;
    private boolean isDefault;
    private Set<RolePermission> permissions;
    private Instant createdAt;
    private Instant updatedAt;

    private Role(
            final RoleID aRoleID,
            final String aName,
            final String aDescription,
            final RoleTypes aRoleType,
            final boolean isDefault,
            final Set<RolePermission> aPermissions,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        super(aRoleID);
        this.name = aName;
        this.description = aDescription;
        this.roleType = aRoleType;
        this.isDefault = isDefault;
        this.permissions = aPermissions;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' must not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedAt, "'updatedAt' must not be null");
    }

    @Override
    public void validate(ValidationHandler handler) {
        new RoleValidator(this, handler).validate();
    }

    public static Role newRole(
            final String aName,
            final String aDescription,
            final RoleTypes aRoleType,
            final boolean isDefault
    ) {
        final var aId = RoleID.unique();
        final var aNow = InstantUtils.now();
        return new Role(
                aId,
                aName,
                aDescription,
                aRoleType,
                isDefault,
                new HashSet<>(),
                aNow,
                aNow
        );
    }

    public static Role with(
            final String aId,
            final String aName,
            final String aDescription,
            final RoleTypes aRoleType,
            final boolean isDefault,
            final Set<RolePermission> aPermissions,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        return new Role(
                RoleID.from(aId),
                aName,
                aDescription,
                aRoleType,
                isDefault,
                new HashSet<>(aPermissions),
                aCreatedAt,
                aUpdatedAt
        );
    }

    public Role update(
            final String aName,
            final String aDescription,
            final RoleTypes aRoleType,
            final boolean isDefault,
            final Set<RolePermission> aPermissions
    ) {
        this.name = aName;
        this.description = aDescription;
        this.roleType = aRoleType;
        this.isDefault = isDefault;
        this.permissions = new HashSet<>(aPermissions != null ? aPermissions : Collections.emptySet());
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public RoleTypes getRoleType() {
        return roleType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public Set<RolePermission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Role addPermissions(final Set<RolePermission> aPermissions) {
        if (aPermissions == null || aPermissions.isEmpty()) {
            return this;
        }
        this.permissions.addAll(aPermissions);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Role removePermission(final RolePermission aPermission) {
        if (aPermission == null) {
            return this;
        }
        this.permissions.remove(aPermission);
        this.updatedAt = InstantUtils.now();
        return this;
    }
}

package com.kaua.ecommerce.users.domain.roles;

import com.kaua.ecommerce.users.domain.AggregateRoot;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Role extends AggregateRoot<RoleID> {

    private String name;
    private String description;
    private RoleTypes roleType;
    private boolean isDefault;
    private List<RolePermission> permissions;
    private Instant createdAt;
    private Instant updatedAt;

    private Role(
            final RoleID aRoleID,
            final String aName,
            final String aDescription,
            final RoleTypes aRoleType,
            final boolean isDefault,
            final List<RolePermission> aPermissions,
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
                new ArrayList<>(),
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
            final List<RolePermission> aPermissions,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        return new Role(
                RoleID.from(aId),
                aName,
                aDescription,
                aRoleType,
                isDefault,
                new ArrayList<>(aPermissions),
                aCreatedAt,
                aUpdatedAt
        );
    }

    public Role update(
            final String aName,
            final String aDescription,
            final RoleTypes aRoleType,
            final boolean isDefault,
            final List<RolePermission> aPermissions
    ) {
        this.name = aName;
        this.description = aDescription;
        this.roleType = aRoleType;
        this.isDefault = isDefault;
        this.permissions = new ArrayList<>(aPermissions != null ? aPermissions : Collections.emptyList());
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

    public List<RolePermission> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Role addPermission(final RolePermission aPermission) {
        if (aPermission == null) {
            return this;
        }
        this.permissions.add(aPermission);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Role addPermissions(final List<RolePermission> aPermissions) {
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

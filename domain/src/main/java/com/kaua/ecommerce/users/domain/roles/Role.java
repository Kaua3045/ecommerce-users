package com.kaua.ecommerce.users.domain.roles;

import com.kaua.ecommerce.users.domain.AggregateRoot;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Role extends AggregateRoot<RoleID> {

    private String name;
    private String description;
    private RoleTypes roleType;
    private boolean isDefault;
    private Instant createdAt;
    private Instant updatedAt;

    private Role(
            final RoleID aRoleID,
            final String aName,
            final String aDescription,
            final RoleTypes aRoleType,
            final boolean isDefault,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        super(aRoleID);
        this.name = aName;
        this.description = aDescription;
        this.roleType = aRoleType;
        this.isDefault = isDefault;
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
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        return new Role(
                RoleID.from(aId),
                aName,
                aDescription,
                aRoleType,
                isDefault,
                aCreatedAt,
                aUpdatedAt
        );
    }

    public Role update(
            final String aName,
            final String aDescription,
            final RoleTypes aRoleType,
            final boolean isDefault
    ) {
        this.name = aName;
        this.description = aDescription;
        this.roleType = aRoleType;
        this.isDefault = isDefault;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

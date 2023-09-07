package com.kaua.ecommerce.users.domain.roles;

import com.kaua.ecommerce.users.domain.AggregateRoot;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;

import java.time.Instant;

public class Role extends AggregateRoot<RoleID> {

    private String name;
    private String description;
    private RoleTypes roleType;
    private Instant createdAt;
    private Instant updatedAt;

    private Role(
            final RoleID aRoleID,
            final String aName,
            final String aDescription,
            final RoleTypes aRoleType,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        super(aRoleID);
        this.name = aName;
        this.description = aDescription;
        this.roleType = aRoleType;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new RoleValidator(this, handler).validate();
    }

    public static Role newRole(
            final String aName,
            final String aDescription,
            final RoleTypes aRoleType
    ) {
        final var aId = RoleID.unique();
        final var aNow = InstantUtils.now();
        return new Role(
                aId,
                aName,
                aDescription,
                aRoleType,
                aNow,
                aNow
        );
    }

    public static Role with(
            final String aId,
            final String aName,
            final String aDescription,
            final RoleTypes aRoleType,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        return new Role(
                RoleID.from(aId),
                aName,
                aDescription,
                aRoleType,
                aCreatedAt,
                aUpdatedAt
        );
    }

    public Role update(
            final String aName,
            final String aDescription,
            final RoleTypes aRoleType
    ) {
        this.name = aName;
        this.description = aDescription;
        this.roleType = aRoleType;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

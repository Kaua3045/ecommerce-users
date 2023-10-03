package com.kaua.ecommerce.users.domain.permissions;

import com.kaua.ecommerce.users.domain.AggregateRoot;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Permission extends AggregateRoot<PermissionID> {

    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    private Permission(
            final PermissionID aPermissionID,
            final String aName,
            final String aDescription,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        super(aPermissionID);
        this.name = aName;
        this.description = aDescription;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' must not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedAt, "'updatedAt' must not be null");
    }

    @Override
    public void validate(ValidationHandler handler) {
        new PermissionValidator(handler, this).validate();
    }

    public static Permission newPermission(final String aName, final String aDescription) {
        final var aId = PermissionID.unique();
        final var aNow = InstantUtils.now();
        return new Permission(aId, aName, aDescription, aNow, aNow);
    }

    public Permission update(final String aDescription) {
        this.description = aDescription;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public static Permission with(
            final String aId,
            final String aName,
            final String aDescription,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        return new Permission(
                PermissionID.from(aId),
                aName,
                aDescription,
                aCreatedAt,
                aUpdatedAt
        );
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

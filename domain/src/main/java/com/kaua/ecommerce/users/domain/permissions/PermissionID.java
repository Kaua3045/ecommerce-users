package com.kaua.ecommerce.users.domain.permissions;

import com.kaua.ecommerce.users.domain.Identifier;
import com.kaua.ecommerce.users.domain.utils.IdUtils;

import java.util.Objects;

public class PermissionID extends Identifier {

    private final String value;

    private PermissionID(final String value) {
        this.value = Objects.requireNonNull(value, "'value' should not be null");
    }

    public static PermissionID unique() {
        return new PermissionID(IdUtils.generate());
    }

    public static PermissionID from(final String aId) {
        return new PermissionID(aId);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PermissionID that = (PermissionID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}

package com.kaua.ecommerce.users.domain.roles;

import com.kaua.ecommerce.users.domain.Identifier;
import com.kaua.ecommerce.users.domain.utils.IdUtils;

import java.util.Objects;

public class RoleID extends Identifier {

    private final String value;

    private RoleID(final String value) {
        this.value = Objects.requireNonNull(value, "'value' should not be null");
    }

    public static RoleID unique() {
        return new RoleID(IdUtils.generate());
    }

    public static RoleID from(final String aId) {
        return new RoleID(aId);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RoleID roleID = (RoleID) o;
        return getValue().equals(roleID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}

package com.kaua.ecommerce.users.domain.accounts.code;

import com.kaua.ecommerce.users.domain.Identifier;
import com.kaua.ecommerce.users.domain.utils.IdUtils;

import java.util.Objects;

public class AccountCodeID extends Identifier {

    private final String value;

    public AccountCodeID(final String value) {
        Objects.requireNonNull(value, "'value' should not be null");
        this.value = value;
    }

    public static AccountCodeID unique() {
        return new AccountCodeID(IdUtils.generate());
    }

    public static AccountCodeID from(final String aId) {
        return new AccountCodeID(aId);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AccountCodeID that = (AccountCodeID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}

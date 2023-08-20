package com.kaua.ecommerce.users.domain.accounts.mail;

import com.kaua.ecommerce.users.domain.Identifier;
import com.kaua.ecommerce.users.domain.utils.IdUtils;

import java.util.Objects;

public class AccountMailID extends Identifier {

    private final String value;

    private AccountMailID(final String value) {
        Objects.requireNonNull(value, "'value' should not be null");
        this.value = value;
    }

    public static AccountMailID unique() {
        return new AccountMailID(IdUtils.generate());
    }

    public static AccountMailID from(final String aId) {
        return new AccountMailID(aId);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AccountMailID that = (AccountMailID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}

package com.kaua.ecommerce.users.domain.accounts;

import com.kaua.ecommerce.users.domain.Identifier;
import com.kaua.ecommerce.users.domain.utils.IdUtils;

import java.util.Objects;

public class AccountID extends Identifier {

    private final String value;

    private AccountID(final String value) {
        Objects.requireNonNull(value, "'value' should not be null");
        this.value = value;
    }

    public static AccountID unique() {
        return new AccountID(IdUtils.generate());
    }

    public static AccountID from(final String aId) {
        return new AccountID(aId);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AccountID accountID = (AccountID) o;
        return getValue().equals(accountID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}

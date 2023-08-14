package com.kaua.ecommerce.users.application.account.create;

import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountID;

public record CreateAccountOutput(String id) {

    public static CreateAccountOutput from(final Account aAccount) {
        return new CreateAccountOutput(aAccount.getId().getValue());
    }

    public static CreateAccountOutput from(final AccountID aId) {
        return new CreateAccountOutput(aId.getValue());
    }
}

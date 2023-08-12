package com.kaua.ecommerce.users.application.account.create;

import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountID;

public record CreateAccountOutput(AccountID id) {

    public static CreateAccountOutput from(final Account aAccount) {
        return new CreateAccountOutput(aAccount.getId());
    }
}

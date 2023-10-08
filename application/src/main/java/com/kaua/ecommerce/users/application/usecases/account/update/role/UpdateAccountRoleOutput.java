package com.kaua.ecommerce.users.application.usecases.account.update.role;

import com.kaua.ecommerce.users.domain.accounts.Account;

public record UpdateAccountRoleOutput(String id) {

    public static UpdateAccountRoleOutput from(final Account aAccount) {
        return new UpdateAccountRoleOutput(aAccount.getId().getValue());
    }
}

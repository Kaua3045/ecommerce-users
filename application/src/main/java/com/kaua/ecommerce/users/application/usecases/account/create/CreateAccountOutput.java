package com.kaua.ecommerce.users.application.usecases.account.create;

import com.kaua.ecommerce.users.domain.accounts.Account;

public record CreateAccountOutput(String id, String email, String password) {

    public static CreateAccountOutput from(final Account aAccount) {
        return new CreateAccountOutput(
                aAccount.getId().getValue(),
                aAccount.getEmail(),
                aAccount.getPassword()
        );
    }

    public static CreateAccountOutput from(final String id, final String email, final String password) {
        return new CreateAccountOutput(id, email, password);
    }
}

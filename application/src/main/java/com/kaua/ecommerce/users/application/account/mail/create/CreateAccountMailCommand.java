package com.kaua.ecommerce.users.application.account.mail.create;

import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;

import java.time.Instant;

public record CreateAccountMailCommand(
        Account account,
        String token,
        AccountMailType type,
        Instant expiresAt
) {

    public static CreateAccountMailCommand with(
            final Account account,
            final String token,
            final AccountMailType type,
            final Instant expiresAt
    ) {
        return new CreateAccountMailCommand(account, token, type, expiresAt);
    }
}

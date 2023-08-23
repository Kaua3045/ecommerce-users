package com.kaua.ecommerce.users.application.account.mail.create;

import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;

import java.time.Instant;

public record CreateAccountMailCommand(
        String accountId,
        String token,
        AccountMailType type,
        Instant expiresAt
) {

    public static CreateAccountMailCommand with(
            final String accountId,
            final String token,
            final AccountMailType type,
            final Instant expiresAt
    ) {
        return new CreateAccountMailCommand(accountId, token, type, expiresAt);
    }
}

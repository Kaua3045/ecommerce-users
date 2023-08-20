package com.kaua.ecommerce.users.application.account.mail.create;

import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;

import java.time.Instant;

public record CreateAccountMailCommand(
        String accountId,
        String token,
        AccountMailType type,
        String subject,
        Instant expirestAt
) {

    public static CreateAccountMailCommand with(
            final String accountId,
            final String token,
            final AccountMailType type,
            final String subject,
            final Instant expirestAt
    ) {
        return new CreateAccountMailCommand(accountId, token, type, subject, expirestAt);
    }
}

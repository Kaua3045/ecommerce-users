package com.kaua.ecommerce.users.application.usecases.account.retrieve.list;

import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountID;

import java.time.Instant;

public record ListAccountsOutput(
        AccountID id,
        String firstName,
        String lastName,
        String email,
        Instant createdAt
) {

    public static ListAccountsOutput from(final Account aAccount) {
        return new ListAccountsOutput(
                aAccount.getId(),
                aAccount.getFirstName(),
                aAccount.getLastName(),
                aAccount.getEmail(),
                aAccount.getCreatedAt());
    }
}

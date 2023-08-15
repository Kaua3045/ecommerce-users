package com.kaua.ecommerce.users.domain.accounts.code;

import com.kaua.ecommerce.users.domain.AggregateRoot;
import com.kaua.ecommerce.users.domain.accounts.AccountID;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class AccountCode extends AggregateRoot<AccountCodeID> {

    private String code;
    private String codeChallenge;
    private AccountID accountID;
    private Instant createdAt;
    private Instant updatedAt;

    private AccountCode(
            final AccountCodeID aAccountCodeID,
            final String aCode,
            final String aCodeChallenge,
            final AccountID aAccountID,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        super(aAccountCodeID);
        this.code = aCode;
        this.codeChallenge = aCodeChallenge;
        this.accountID = aAccountID;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' must not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedAt, "'updatedAt' must not be null");
    }

    public static AccountCode newAccountCode(
            final String aCode,
            final String aCodeChallenge,
            final AccountID aAccountID
    ) {
        final var id = AccountCodeID.unique();
        final var now = Instant.now();
        return new AccountCode(
                id,
                aCode,
                aCodeChallenge,
                aAccountID,
                now,
                now
        );
    }

    public static AccountCode with(
            final String aId,
            final String aCode,
            final String aCodeChallenge,
            final AccountID aAccountID,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        return new AccountCode(
                AccountCodeID.from(aId),
                aCode,
                aCodeChallenge,
                aAccountID,
                aCreatedAt,
                aUpdatedAt
        );
    }

    public String getCode() {
        return code;
    }

    public String getCodeChallenge() {
        return codeChallenge;
    }


    public AccountID getAccountID() {
        return accountID;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }


    public Instant getUpdatedAt() {
        return updatedAt;
    }


    @Override
    public void validate(ValidationHandler handler) {
        new AccountCodeValidator(this, handler).validate();
    }
}

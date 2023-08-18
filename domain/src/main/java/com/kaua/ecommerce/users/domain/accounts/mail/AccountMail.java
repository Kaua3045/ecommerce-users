package com.kaua.ecommerce.users.domain.accounts.mail;

import com.kaua.ecommerce.users.domain.AggregateRoot;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class AccountMail extends AggregateRoot<AccountMailID> {

    private String token;
    private AccountMailType type;
    private Account account;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant updatedAt;

    private AccountMail(
            final AccountMailID aId,
            final String aToken,
            final AccountMailType aType,
            final Account aAccount,
            final Instant aExpiresAt,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        super(aId);
        this.token = aToken;
        this.type = aType;
        this.account = aAccount;
        this.expiresAt = aExpiresAt;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' must not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedAt, "'updatedAt' must not be null");
    }

    @Override
    public void validate(ValidationHandler handler) {
        new AccountMailValidator(this, handler).validate();
    }

    public static AccountMail newAccountMail(
            final String aToken,
            final AccountMailType aType,
            final Account aAccount,
            final Instant aExpiresAt
    ) {
        final var aId = AccountMailID.unique();
        final var now = InstantUtils.now();
        return new AccountMail(
                aId,
                aToken,
                aType,
                aAccount,
                aExpiresAt,
                now,
                now
        );
    }

    public static AccountMail with(
            final String aId,
            final String aToken,
            final AccountMailType aType,
            final Account aAccount,
            final Instant aExpiresAt,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        return new AccountMail(
                AccountMailID.from(aId),
                aToken,
                aType,
                aAccount,
                aExpiresAt,
                aCreatedAt,
                aUpdatedAt
        );
    }

    public String getToken() {
        return token;
    }

    public AccountMailType getType() {
        return type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account aAccount) {
        this.account = aAccount;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

package com.kaua.ecommerce.users.domain.accounts;

import com.kaua.ecommerce.users.domain.AggregateRoot;

import java.time.Instant;

public class Account extends AggregateRoot<AccountID> {

    private String firstName;
    private String lastName;
    private String email;
    private AccountMailStatus mailStatus;
    private String password;
    private String avatarUrl;
    private Instant createdAt;
    private Instant updatedAt;

    private Account(
            final AccountID aAccountID,
            final String aFirstName,
            final String aLastName,
            final String aEmail,
            final AccountMailStatus aMailStatus,
            final String aPassword,
            final String aAvatarUrl,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        super(aAccountID);
        this.firstName = aFirstName;
        this.lastName = aLastName;
        this.email = aEmail;
        this.mailStatus = aMailStatus;
        this.password = aPassword;
        this.avatarUrl = aAvatarUrl;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
    }

    public static Account newAccount(
            final String aFirstName,
            final String aLastName,
            final String aEmail,
            final String aPassword
    ) {
        final var id = AccountID.unique();
        final var now = Instant.now();
        return new Account(
                id,
                aFirstName,
                aLastName,
                aEmail,
                AccountMailStatus.WAITING_CONFIRMATION,
                aPassword,
                null,
                now,
                now
        );
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public AccountMailStatus getMailStatus() {
        return mailStatus;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

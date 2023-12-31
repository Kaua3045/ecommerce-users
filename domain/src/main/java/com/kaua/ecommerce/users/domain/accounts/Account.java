package com.kaua.ecommerce.users.domain.accounts;

import com.kaua.ecommerce.users.domain.AggregateRoot;
import com.kaua.ecommerce.users.domain.event.DomainEvent;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.List;

public class Account extends AggregateRoot<AccountID> {

    private String firstName;
    private String lastName;
    private String email;
    private AccountMailStatus mailStatus;
    private String password;
    private String avatarUrl;
    private Role role;
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
            final Role aRole,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final List<DomainEvent> aDomainEvents
    ) {
        super(aAccountID, aDomainEvents);
        this.firstName = aFirstName;
        this.lastName = aLastName;
        this.email = aEmail;
        this.mailStatus = aMailStatus;
        this.password = aPassword;
        this.avatarUrl = aAvatarUrl;
        this.role = aRole;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
    }

    public static Account newAccount(
            final String aFirstName,
            final String aLastName,
            final String aEmail,
            final String aPassword,
            final Role aRole
    ) {
        final var id = AccountID.unique();
        final var now = InstantUtils.now();
        return new Account(
                id,
                aFirstName,
                aLastName,
                aEmail,
                AccountMailStatus.WAITING_CONFIRMATION,
                aPassword,
                null,
                aRole,
                now,
                now,
                null
        );
    }

    public Account changePassword(final String aPassword) {
        this.password = aPassword;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Account changeAvatarUrl(final String aAvatarUrl) {
        this.avatarUrl = aAvatarUrl;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Account changeRole(final Role aRole) {
        this.role = aRole;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public static Account with(
            final String aId,
            final String aFirstName,
            final String aLastName,
            final String aEmail,
            final AccountMailStatus aMailStatus,
            final String aPassword,
            final String aAvatarUrl,
            final Role aRole,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final List<DomainEvent> aDomainEvents
    ) {
        return new Account(
                AccountID.from(aId),
                aFirstName,
                aLastName,
                aEmail,
                aMailStatus,
                aPassword,
                aAvatarUrl,
                aRole,
                aCreatedAt,
                aUpdatedAt,
                aDomainEvents
        );
    }

    public Account confirm() {
        this.mailStatus = AccountMailStatus.CONFIRMED;
        this.updatedAt = InstantUtils.now();
        return this;
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

    public Role getRole() {
        return role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new AccountValidator(this, handler).validate();
    }
}

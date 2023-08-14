package com.kaua.ecommerce.users.infrastructure.accounts.persistence;

import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountMailStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "accounts")
public class AccountJpaEntity {

    @Id
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "mail_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountMailStatus mailStatus;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    private AccountJpaEntity() {}

    private AccountJpaEntity(
            final String id,
            final String firstName,
            final String lastName,
            final String email,
            final String password,
            final String avatarUrl,
            final AccountMailStatus mailStatus,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.mailStatus = mailStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AccountJpaEntity toEntity(final Account aAccount) {
        return new AccountJpaEntity(
                aAccount.getId().getValue(),
                aAccount.getFirstName(),
                aAccount.getLastName(),
                aAccount.getEmail(),
                aAccount.getPassword(),
                aAccount.getAvatarUrl(),
                aAccount.getMailStatus(),
                aAccount.getCreatedAt(),
                aAccount.getUpdatedAt()
        );
    }

    public Account toDomain() {
        return Account.with(
                getId(),
                getFirstName(),
                getLastName(),
                getEmail(),
                getMailStatus(),
                getPassword(),
                getAvatarUrl(),
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    public String getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public AccountMailStatus getMailStatus() {
        return mailStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

package com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence;

import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "accounts_mails")
public class AccountMailJpaEntity {

    @Id
    private String id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountMailType type;

    @Column(name = "expires_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant expiresAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountJpaEntity account;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    private AccountMailJpaEntity() {}

    public AccountMailJpaEntity(
            final String id,
            final String token,
            final AccountMailType type,
            final Instant expiresAt,
            final AccountJpaEntity account,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = id;
        this.token = token;
        this.type = type;
        this.expiresAt = expiresAt;
        this.account = account;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AccountMailJpaEntity toEntity(final AccountMail aAccountMail) {
        return new AccountMailJpaEntity(
                aAccountMail.getId().getValue(),
                aAccountMail.getToken(),
                aAccountMail.getType(),
                aAccountMail.getExpiresAt(),
                AccountJpaEntity.toEntity(aAccountMail.getAccount()),
                aAccountMail.getCreatedAt(),
                aAccountMail.getUpdatedAt()
        );
    }

    public AccountMail toDomain() {
        return AccountMail.with(
                getId(),
                getToken(),
                getType(),
                getAccount().toDomain(),
                getExpiresAt(),
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AccountMailType getType() {
        return type;
    }

    public void setType(AccountMailType type) {
        this.type = type;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public AccountJpaEntity getAccount() {
        return account;
    }

    public void setAccount(AccountJpaEntity account) {
        this.account = account;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

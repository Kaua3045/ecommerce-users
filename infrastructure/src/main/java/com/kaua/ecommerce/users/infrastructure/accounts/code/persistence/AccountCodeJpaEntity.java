package com.kaua.ecommerce.users.infrastructure.accounts.code.persistence;

import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountID;
import com.kaua.ecommerce.users.domain.accounts.code.AccountCode;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "accounts_code_login")
public class AccountCodeJpaEntity {

    @Id
    private String id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "code_challenge", nullable = false)
    private String codeChallenge;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountJpaEntity account;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    private AccountCodeJpaEntity() {}

    private AccountCodeJpaEntity(
            final String id,
            final String code,
            final String codeChallenge,
            final AccountJpaEntity account,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = id;
        this.code = code;
        this.codeChallenge = codeChallenge;
        this.account = account;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AccountCodeJpaEntity from(final AccountCode aAccountCode, final Account aAccount) {
        return new AccountCodeJpaEntity(
                aAccountCode.getId().getValue(),
                aAccountCode.getCode(),
                aAccountCode.getCodeChallenge(),
                AccountJpaEntity.toEntity(aAccount),
                aAccountCode.getCreatedAt(),
                aAccountCode.getUpdatedAt());
    }

    public AccountCode toDomain() {
        return AccountCode.with(
                getId(),
                getCode(),
                getCodeChallenge(),
                AccountID.from(getAccount().getId()),
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeChallenge() {
        return codeChallenge;
    }

    public void setCodeChallenge(String codeChallenge) {
        this.codeChallenge = codeChallenge;
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

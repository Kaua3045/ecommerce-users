package com.kaua.ecommerce.users.infrastructure.accounts.persistence;

import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountMailStatus;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "accounts")
public class AccountJpaEntity {

    @Id
    @Column(name ="account_id", nullable = false, unique = true)
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

    @JoinColumn(name = "role_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    private RoleJpaEntity roleJpaEntity;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    public AccountJpaEntity() {}

    private AccountJpaEntity(
            final String id,
            final String firstName,
            final String lastName,
            final String email,
            final String password,
            final String avatarUrl,
            final AccountMailStatus mailStatus,
            final RoleJpaEntity roleJpaEntity,
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
        this.roleJpaEntity = roleJpaEntity;
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
                RoleJpaEntity.toEntity(aAccount.getRole()),
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
                getRoleJpaEntity().toDomain(),
                getCreatedAt(),
                getUpdatedAt(),
                null
        );
    }

    public Account toDomainPagination() {
        return Account.with(
                getId(),
                getFirstName(),
                getLastName(),
                getEmail(),
                getMailStatus(),
                getPassword(),
                getAvatarUrl(),
                null,
                getCreatedAt(),
                getUpdatedAt(),
                null
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

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setMailStatus(AccountMailStatus mailStatus) {
        this.mailStatus = mailStatus;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RoleJpaEntity getRoleJpaEntity() {
        return roleJpaEntity;
    }
}

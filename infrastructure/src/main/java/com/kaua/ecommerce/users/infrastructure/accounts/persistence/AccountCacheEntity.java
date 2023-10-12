package com.kaua.ecommerce.users.infrastructure.accounts.persistence;

import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountMailStatus;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleCacheEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@RedisHash(value = "account", timeToLive = 60 * 60 * 24) // 1 day
public class AccountCacheEntity {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private AccountMailStatus mailStatus;
    private String avatarUrl;
    private RoleCacheEntity role;
    private Instant createdAt;
    private Instant updatedAt;

    public AccountCacheEntity() {}

    private AccountCacheEntity(
            final String id,
            final String firstName,
            final String lastName,
            final String email,
            final AccountMailStatus mailStatus,
            final String avatarUrl,
            final RoleCacheEntity role,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mailStatus = mailStatus;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AccountCacheEntity toEntity(final Account aAccount) {
        return new AccountCacheEntity(
                aAccount.getId().getValue(),
                aAccount.getFirstName(),
                aAccount.getLastName(),
                aAccount.getEmail(),
                aAccount.getMailStatus(),
                aAccount.getAvatarUrl(),
                RoleCacheEntity.toEntity(aAccount.getRole()),
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
                null,
                getAvatarUrl(),
                getRole().toDomain(),
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

    public AccountMailStatus getMailStatus() {
        return mailStatus;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public RoleCacheEntity getRole() {
        return role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

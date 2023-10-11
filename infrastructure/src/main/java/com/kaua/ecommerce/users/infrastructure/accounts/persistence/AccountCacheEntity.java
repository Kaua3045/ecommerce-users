package com.kaua.ecommerce.users.infrastructure.accounts.persistence;

import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountMailStatus;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "account", timeToLive = 60 * 60 * 24 * 7) // 7 days
public class AccountCacheEntity {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private AccountMailStatus mailStatus;
    private String avatarUrl;
    private RoleJpaEntity role;

    public AccountCacheEntity() {}

    private AccountCacheEntity(
            final String id,
            final String firstName,
            final String lastName,
            final String email,
            final AccountMailStatus mailStatus,
            final String avatarUrl,
            final RoleJpaEntity role
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mailStatus = mailStatus;
        this.avatarUrl = avatarUrl;
        this.role = role;
    }

    public static AccountCacheEntity toEntity(final Account aAccount) {
        return new AccountCacheEntity(
                aAccount.getId().getValue(),
                aAccount.getFirstName(),
                aAccount.getLastName(),
                aAccount.getEmail(),
                aAccount.getMailStatus(),
                aAccount.getAvatarUrl(),
                RoleJpaEntity.toEntity(aAccount.getRole())
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
                null,
                null,
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

    public RoleJpaEntity getRole() {
        return role;
    }
}

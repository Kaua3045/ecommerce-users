package com.kaua.ecommerce.users.infrastructure.accounts;

import com.kaua.ecommerce.users.application.gateways.CacheGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountCacheEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountCacheRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class AccountCacheGateway implements CacheGateway<Account> {

    private final AccountCacheRepository accountCacheRepository;

    public AccountCacheGateway(final AccountCacheRepository accountCacheRepository) {
        this.accountCacheRepository = Objects.requireNonNull(accountCacheRepository);
    }

    @Override
    public void save(Account value) {
        this.accountCacheRepository.save(AccountCacheEntity.toEntity(value));
    }

    @Override
    public Optional<Account> get(String key) {
        return this.accountCacheRepository.findById(key)
                .map(AccountCacheEntity::toDomain);
    }

    @Override
    public void delete(String key) {
        if (this.accountCacheRepository.existsById(key)) {
            this.accountCacheRepository.deleteById(key);
        }
    }
}

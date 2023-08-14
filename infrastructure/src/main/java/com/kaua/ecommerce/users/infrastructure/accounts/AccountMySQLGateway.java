package com.kaua.ecommerce.users.infrastructure.accounts;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AccountMySQLGateway implements AccountGateway {

    private final AccountJpaRepository accountJpaRepository;

    public AccountMySQLGateway(final AccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = Objects.requireNonNull(accountJpaRepository);
    }

    @Override
    public Account create(Account aAccount) {
        return this.accountJpaRepository
                .save(AccountJpaEntity.toEntity(aAccount))
                .toDomain();
    }

    @Override
    public boolean existsByEmail(String aEmail) {
        return this.accountJpaRepository.existsByEmail(aEmail);
    }
}

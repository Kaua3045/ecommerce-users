package com.kaua.ecommerce.users.infrastructure.accounts.code;

import com.kaua.ecommerce.users.application.gateways.AccountCodeGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.code.AccountCode;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.infrastructure.accounts.code.persistence.AccountCodeJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.code.persistence.AccountCodeJpaRepository;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AccountCodeMySQLGateway implements AccountCodeGateway {

    private final AccountCodeJpaRepository repository;
    private final AccountJpaRepository accountJpaRepository;

    public AccountCodeMySQLGateway(final AccountCodeJpaRepository repository, final AccountJpaRepository accountJpaRepository) {
        this.repository = Objects.requireNonNull(repository);
        this.accountJpaRepository = Objects.requireNonNull(accountJpaRepository);
    }

    @Override
    public AccountCode create(AccountCode aAccountCode) {
        final var aAccountEntity = this.accountJpaRepository.findById(aAccountCode.getAccountID().getValue())
                .orElseThrow(() -> NotFoundException.with(Account.class, aAccountCode.getAccountID().getValue()));

        final var aEntity = AccountCodeJpaEntity.toEntity(aAccountCode, aAccountEntity.toDomain());
        return this.repository.save(aEntity).toDomain();
    }
}

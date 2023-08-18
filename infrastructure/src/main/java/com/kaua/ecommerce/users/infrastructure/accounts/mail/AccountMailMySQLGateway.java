package com.kaua.ecommerce.users.infrastructure.accounts.mail;

import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
public class AccountMailMySQLGateway implements AccountMailGateway {

    private final AccountMailJpaRepository accountMailRepository;

    public AccountMailMySQLGateway(final AccountMailJpaRepository accountMailRepository) {
        this.accountMailRepository = Objects.requireNonNull(accountMailRepository);
    }

    @Override
    @Transactional
    public AccountMail create(AccountMail accountMail) {
        final var aEntity = AccountMailJpaEntity.toEntity(accountMail);
        return this.accountMailRepository.save(aEntity).toDomain();
    }

    @Override
    public List<AccountMail> findAllByAccountId(String accountId) {
        return this.accountMailRepository.findAllByAccountId(accountId)
                .stream()
                .map(AccountMailJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        if (this.accountMailRepository.existsById(id)) {
            this.accountMailRepository.deleteById(id);
        }
    }
}

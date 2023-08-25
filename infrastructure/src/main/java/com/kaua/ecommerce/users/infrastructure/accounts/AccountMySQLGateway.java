package com.kaua.ecommerce.users.infrastructure.accounts;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.services.EventService;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class AccountMySQLGateway implements AccountGateway {

    private final AccountJpaRepository accountJpaRepository;
    private final EventService eventService;

    public AccountMySQLGateway(
            final AccountJpaRepository accountJpaRepository,
            @AccountCreatedEvent final EventService eventService
    ) {
        this.accountJpaRepository = Objects.requireNonNull(accountJpaRepository);
        this.eventService = Objects.requireNonNull(eventService);
    }

    @Override
    public Account create(Account aAccount) {
        final var aResult = this.accountJpaRepository
                .save(AccountJpaEntity.toEntity(aAccount))
                .toDomain();

        aAccount.publishDomainEvent(this.eventService::send);

        return aResult;
    }

    @Override
    public boolean existsByEmail(String aEmail) {
        return this.accountJpaRepository.existsByEmail(aEmail);
    }

    @Override
    public Optional<Account> findById(String aId) {
        return this.accountJpaRepository.findById(aId).map(AccountJpaEntity::toDomain);
    }

    @Override
    public Optional<Account> findByEmail(String aEmail) {
        return this.accountJpaRepository.findByEmail(aEmail).map(AccountJpaEntity::toDomain);
    }

    @Override
    public Account update(Account aAccount) {
        return this.accountJpaRepository
                .save(AccountJpaEntity.toEntity(aAccount))
                .toDomain();
    }

    @Override
    public void deleteById(String aId) {
        if (this.accountJpaRepository.existsById(aId)) {
            this.accountJpaRepository.deleteById(aId);
        }
    }
}

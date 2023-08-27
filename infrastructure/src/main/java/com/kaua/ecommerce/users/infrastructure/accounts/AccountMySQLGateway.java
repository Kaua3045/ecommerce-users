package com.kaua.ecommerce.users.infrastructure.accounts;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountDeletedEvent;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountCreatedEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountDeleteEvent;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.AccountEvents;
import com.kaua.ecommerce.users.infrastructure.configurations.properties.amqp.QueueProperties;
import com.kaua.ecommerce.users.infrastructure.services.EventService;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class AccountMySQLGateway implements AccountGateway {

    private final AccountJpaRepository accountJpaRepository;
    private final EventService eventService;
    private final QueueProperties accountCreatedQueueProperties;
    private final QueueProperties accountDeletedQueueProperties;

    public AccountMySQLGateway(
            final AccountJpaRepository accountJpaRepository,
            @AccountEvents final EventService eventService,
            @AccountCreatedEvent final QueueProperties accountCreatedQueueProperties,
            @AccountDeleteEvent final QueueProperties accountDeletedQueueProperties
    ) {
        this.accountJpaRepository = Objects.requireNonNull(accountJpaRepository);
        this.eventService = Objects.requireNonNull(eventService);
        this.accountCreatedQueueProperties = Objects.requireNonNull(accountCreatedQueueProperties);
        this.accountDeletedQueueProperties = Objects.requireNonNull(accountDeletedQueueProperties);
    }

    @Override
    public Account create(Account aAccount) {
        final var aResult = this.accountJpaRepository
                .save(AccountJpaEntity.toEntity(aAccount))
                .toDomain();

        aAccount.publishDomainEvent(this.eventService::send, this.accountCreatedQueueProperties.getRoutingKey());

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
            this.eventService.send(new AccountDeletedEvent(aId), this.accountDeletedQueueProperties.getRoutingKey());
            this.accountJpaRepository.deleteById(aId);
        }
    }
}

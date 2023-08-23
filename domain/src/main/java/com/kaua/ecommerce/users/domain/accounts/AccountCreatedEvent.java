package com.kaua.ecommerce.users.domain.accounts;

import com.kaua.ecommerce.users.domain.event.DomainEvent;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;

import java.time.Instant;

public record AccountCreatedEvent(
        String id,
        String firstName,
        String lastName,
        String email,
        Instant occurredOn
) implements DomainEvent {

    public AccountCreatedEvent(
            final String id,
            final String firstName,
            final String lastName,
            final String email
    ) {
        this(id, firstName, lastName, email, InstantUtils.now());
    }
}

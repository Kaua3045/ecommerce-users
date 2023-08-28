package com.kaua.ecommerce.users.domain.accounts;

import com.kaua.ecommerce.users.domain.event.DomainEvent;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;

import java.time.Instant;

public record AccountDeletedEvent(String id, Instant occurredOn) implements DomainEvent {

    public AccountDeletedEvent(String id) {
        this(id, InstantUtils.now());
    }
}

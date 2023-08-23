package com.kaua.ecommerce.users.domain;

import com.kaua.ecommerce.users.domain.event.DomainEvent;

import java.util.Collections;
import java.util.List;

// Um aggregate root é uma entidade que é a raiz de um aggregate
// Um aggregate é um conjunto de entidades e value objects que são tratados como uma unidade
public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        this(id, Collections.emptyList());
    }

    protected AggregateRoot(final ID id, final List<DomainEvent> domainEvents) {
        super(id, domainEvents);
    }
}

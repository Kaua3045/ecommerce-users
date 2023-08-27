package com.kaua.ecommerce.users.domain;

import com.kaua.ecommerce.users.domain.event.DomainEvent;
import com.kaua.ecommerce.users.domain.event.DomainEventPublisher;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// Entity definida por um ID
// Identifier é um value object, pois é identificado pelos seus atributos
// Podemos receber qualquer tipo desde que ele extenda um Identifier
// Em uma entity o id é constante, já os atributos podem ser alterados
public abstract class Entity<ID extends Identifier> {

    private final ID id;
    private final List<DomainEvent> domainEvents;

    protected Entity(final ID id, final List<DomainEvent> domainEvents) {
        Objects.requireNonNull(id, "'id' should not be null");
        this.id = id;
        this.domainEvents = new ArrayList<>(domainEvents == null ? Collections.emptyList() : domainEvents);
    }

    public abstract void validate(ValidationHandler handler);

    public ID getId() {
        return id;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void publishDomainEvent(final DomainEventPublisher publisher, final String routingKey) {
        if (publisher == null) {
            return;
        }

        getDomainEvents().forEach(event -> publisher.publish(event, routingKey));

        this.domainEvents.clear();
    }

    public void registerEvent(final DomainEvent event) {
        if (event != null) {
            this.domainEvents.add(event);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

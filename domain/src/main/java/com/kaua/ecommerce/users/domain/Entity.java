package com.kaua.ecommerce.users.domain;

import com.kaua.ecommerce.users.domain.validation.ValidationHandler;

import java.util.Objects;

// Entity definida por um ID
// Identifier é um value object, pois é identificado pelos seus atributos
// Podemos receber qualquer tipo desde que ele extenda um Identifier
// Em uma entity o id é constante, já os atributos podem ser alterados
public abstract class Entity<ID extends Identifier> {

    protected final ID id;

    protected Entity(final ID id) {
        Objects.requireNonNull(id, "'id' should not be null");
        this.id = id;
    }

    public abstract void validate(ValidationHandler handler);

    public ID getId() {
        return id;
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

package com.kaua.ecommerce.users.domain;

// Um aggregate root é uma entidade que é a raiz de um aggregate
// Um aggregate é um conjunto de entidades e value objects que são tratados como uma unidade
public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        super(id);
    }
}

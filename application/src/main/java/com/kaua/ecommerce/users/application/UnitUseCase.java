package com.kaua.ecommerce.users.application;

import reactor.core.publisher.Mono;

public abstract class UnitUseCase<IN> {

    public abstract Mono<Void> execute(IN input);
}

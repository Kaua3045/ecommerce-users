package com.kaua.ecommerce.users.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN input);
}

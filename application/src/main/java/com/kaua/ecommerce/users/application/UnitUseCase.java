package com.kaua.ecommerce.users.application;

public abstract class UnitUseCase<IN> {

    public abstract void execute(IN input);
}

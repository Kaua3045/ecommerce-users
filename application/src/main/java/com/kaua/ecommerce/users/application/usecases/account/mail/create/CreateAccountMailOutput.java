package com.kaua.ecommerce.users.application.usecases.account.mail.create;

public record CreateAccountMailOutput(String id) {

    public static CreateAccountMailOutput from(String id) {
        return new CreateAccountMailOutput(id);
    }
}

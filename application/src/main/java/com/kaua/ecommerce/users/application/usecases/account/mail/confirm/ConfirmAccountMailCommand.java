package com.kaua.ecommerce.users.application.usecases.account.mail.confirm;

public record ConfirmAccountMailCommand(String token) {

    public static ConfirmAccountMailCommand with(String token) {
        return new ConfirmAccountMailCommand(token);
    }
}

package com.kaua.ecommerce.users.application.usecases.account.update.password;

public record RequestResetPasswordCommand(String email) {

    public static RequestResetPasswordCommand with(String email) {
        return new RequestResetPasswordCommand(email);
    }
}

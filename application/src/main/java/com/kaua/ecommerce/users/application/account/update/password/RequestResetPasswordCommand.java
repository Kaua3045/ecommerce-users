package com.kaua.ecommerce.users.application.account.update.password;

public record RequestResetPasswordCommand(String email) {

    public static RequestResetPasswordCommand with(String email) {
        return new RequestResetPasswordCommand(email);
    }
}

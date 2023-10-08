package com.kaua.ecommerce.users.application.usecases.account.update.password.reset;

public record ResetPasswordCommand(String token, String newPassword) {

    public static ResetPasswordCommand with(String token, String newPassword) {
        return new ResetPasswordCommand(token, newPassword);
    }
}

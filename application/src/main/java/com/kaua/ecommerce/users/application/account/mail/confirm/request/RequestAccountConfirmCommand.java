package com.kaua.ecommerce.users.application.account.mail.confirm.request;

public record RequestAccountConfirmCommand(String id) {

    public static RequestAccountConfirmCommand with(String id) {
        return new RequestAccountConfirmCommand(id);
    }
}

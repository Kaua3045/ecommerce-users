package com.kaua.ecommerce.users.application.usecases.account.delete;

public record DeleteAccountCommand(String id) {

    public static DeleteAccountCommand with(final String aId) {
        return new DeleteAccountCommand(aId);
    }
}

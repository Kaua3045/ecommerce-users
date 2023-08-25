package com.kaua.ecommerce.users.application.account.retrieve.get;

public record GetAccountCommand(String id) {

    public static GetAccountCommand with(final String aId) {
        return new GetAccountCommand(aId);
    }
}

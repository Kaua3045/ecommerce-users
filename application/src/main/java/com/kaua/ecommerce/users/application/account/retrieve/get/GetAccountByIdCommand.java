package com.kaua.ecommerce.users.application.account.retrieve.get;

public record GetAccountByIdCommand(String id) {

    public static GetAccountByIdCommand with(final String aId) {
        return new GetAccountByIdCommand(aId);
    }
}

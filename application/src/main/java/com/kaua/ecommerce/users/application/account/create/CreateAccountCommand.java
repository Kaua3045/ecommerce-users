package com.kaua.ecommerce.users.application.account.create;

public record CreateAccountCommand(
        String firstName,
        String lastName,
        String email,
        String password
) {

    public static CreateAccountCommand with(
            final String firstName,
            final String lastName,
            final String email,
            final String password
    ) {
        return new CreateAccountCommand(firstName, lastName, email, password);
    }
}

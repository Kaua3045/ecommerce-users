package com.kaua.ecommerce.users.application.usecases.account.mail.create;

public record CreateMailQueueCommand(
        String token,
        String firstName,
        String email,
        String type
) {

    public static CreateMailQueueCommand with(
            final String token,
            final String firstName,
            final String email,
            final String type
    ) {
        return new CreateMailQueueCommand(token, firstName, email, type);
    }
}

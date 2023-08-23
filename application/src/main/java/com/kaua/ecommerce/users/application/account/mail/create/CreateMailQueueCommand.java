package com.kaua.ecommerce.users.application.account.mail.create;

public record CreateMailQueueCommand(
        String token,
        String subject,
        String firstName,
        String email
) {

    public static CreateMailQueueCommand with(
            final String token,
            final String subject,
            final String firstName,
            final String email
    ) {
        return new CreateMailQueueCommand(token, subject, firstName, email);
    }
}

package com.kaua.ecommerce.users.application.account.code;

public record CreateAccountCodeCommand(
        String code,
        String codeChallenge,
        String accountID
) {

    public static CreateAccountCodeCommand with(
            final String code,
            final String codeChallenge,
            final String accountID
    ) {
        return new CreateAccountCodeCommand(code, codeChallenge, accountID);
    }
}

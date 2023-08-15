package com.kaua.ecommerce.users.application.account.code;

import com.kaua.ecommerce.users.domain.accounts.code.AccountCode;

public record CreateAccountCodeOutput(
        String code,
        String codeChallenge
) {

    public static CreateAccountCodeOutput from(final AccountCode aAccountCode) {
        return new CreateAccountCodeOutput(aAccountCode.getCode(), aAccountCode.getCodeChallenge());
    }
}

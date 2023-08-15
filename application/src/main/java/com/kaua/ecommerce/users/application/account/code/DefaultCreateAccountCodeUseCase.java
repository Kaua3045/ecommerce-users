package com.kaua.ecommerce.users.application.account.code;

import com.kaua.ecommerce.users.application.gateways.AccountCodeGateway;
import com.kaua.ecommerce.users.domain.accounts.AccountID;
import com.kaua.ecommerce.users.domain.accounts.code.AccountCode;
import com.kaua.ecommerce.users.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateAccountCodeUseCase extends CreateAccountCodeUseCase {

    private final AccountCodeGateway accountCodeGateway;

    public DefaultCreateAccountCodeUseCase(final AccountCodeGateway accountCodeGateway) {
        this.accountCodeGateway = Objects.requireNonNull(accountCodeGateway);
    }

    @Override
    public CreateAccountCodeOutput execute(CreateAccountCodeCommand aCommand) {
        final var aAccountCode = AccountCode.newAccountCode(
                aCommand.code(),
                aCommand.codeChallenge(),
                AccountID.from(aCommand.accountID())
        );
        aAccountCode.validate(new ThrowsValidationHandler());

        return CreateAccountCodeOutput.from(accountCodeGateway.create(aAccountCode));
    }
}

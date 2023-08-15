package com.kaua.ecommerce.users.domain.accounts.code;

import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;
import com.kaua.ecommerce.users.domain.validation.Validator;

public class AccountCodeValidator extends Validator {

    private final AccountCode accountCode;
    private final int CODE_MAXIMUM_LENGTH = 36;
    private final int CODE_CHALLENGE_MAXIMUM_LENGTH = 100;

    public AccountCodeValidator(final AccountCode accountCode, final ValidationHandler aHandler) {
        super(aHandler);
        this.accountCode = accountCode;
    }

    @Override
    public void validate() {
        checkCodeConstraints();
        checkCodeChallengeConstraints();
    }

    private void checkCodeConstraints() {
        if (this.accountCode.getCode() == null || this.accountCode.getCode().isBlank()) {
            this.validationHandler().append(new Error("'code' should not be null or blank"));
            return;
        }

        if (this.accountCode.getCode() != null &&
                this.accountCode.getCode().trim().length() > CODE_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error("'code' must be less than " + CODE_MAXIMUM_LENGTH + " characters"));
        }
    }

    private void checkCodeChallengeConstraints() {
        if (this.accountCode.getCodeChallenge() == null || this.accountCode.getCodeChallenge().isBlank()) {
            this.validationHandler().append(new Error("'codeChallenge' should not be null or blank"));
            return;
        }

        if (this.accountCode.getCodeChallenge() != null &&
                this.accountCode.getCodeChallenge().trim().length() > CODE_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error("'codeChallenge' must be less than " + CODE_CHALLENGE_MAXIMUM_LENGTH + " characters"));
        }
    }
}

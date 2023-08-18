package com.kaua.ecommerce.users.domain.accounts.mail;

import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;
import com.kaua.ecommerce.users.domain.validation.Validator;

public class AccountMailValidator extends Validator {

    private AccountMail accountMail;
    private final int TOKEN_MAXIMUM_LENGTH = 36;

    public AccountMailValidator(final AccountMail aAccountMail, final ValidationHandler aHandler) {
        super(aHandler);
        this.accountMail = aAccountMail;
    }

    @Override
    public void validate() {
        this.checkTokenConstraints();
        this.checkTypeConstraints();
        this.checkAccountIdConstraints();
        this.checkExpiresAtConstraints();
    }

    private void checkTokenConstraints() {
        if (this.accountMail.getToken() == null || this.accountMail.getToken().isBlank()) {
            this.validationHandler().append(new Error("'token' should not be null or blank"));
            return;
        }

        if (this.accountMail.getToken() != null &&
                this.accountMail.getToken().trim().length() > TOKEN_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error("'token' should not be greater than " + TOKEN_MAXIMUM_LENGTH));
            return;
        }
    }

    private void checkTypeConstraints() {
        if (this.accountMail.getType() == null) {
            this.validationHandler().append(new Error("'type' should not be null"));
            return;
        }
    }

    private void checkAccountIdConstraints() {
        if (this.accountMail.getAccountId() == null) {
            this.validationHandler().append(new Error("'accountId' should not be null"));
            return;
        }
    }

    private void checkExpiresAtConstraints() {
        if (this.accountMail.getExpiresAt() == null) {
            this.validationHandler().append(new Error("'expiresAt' should not be null"));
            return;
        }

        if (this.accountMail.getExpiresAt() != null &&
                this.accountMail.getExpiresAt().isBefore(InstantUtils.now())) {
            this.validationHandler().append(new Error("'expiresAt' should not be before now"));
            return;
        }
    }
}

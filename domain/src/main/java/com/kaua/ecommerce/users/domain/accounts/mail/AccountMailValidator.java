package com.kaua.ecommerce.users.domain.accounts.mail;

import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;
import com.kaua.ecommerce.users.domain.validation.Validator;

import static com.kaua.ecommerce.users.domain.utils.CommonErrorsMessages.commonNullOrBlankErrorMessage;

public class AccountMailValidator extends Validator {

    private final AccountMail accountMail;
    private static final int TOKEN_MAXIMUM_LENGTH = 36;

    public AccountMailValidator(final AccountMail aAccountMail, final ValidationHandler aHandler) {
        super(aHandler);
        this.accountMail = aAccountMail;
    }

    @Override
    public void validate() {
        this.checkTokenConstraints();
        this.checkTypeConstraints();
        this.checkAccountConstraints();
        this.checkExpiresAtConstraints();
    }

    private void checkTokenConstraints() {
        if (this.accountMail.getToken() == null || this.accountMail.getToken().isBlank()) {
            this.validationHandler().append(new Error(commonNullOrBlankErrorMessage("token")));
            return;
        }

        if (this.accountMail.getToken().trim().length() > TOKEN_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error("'token' should not be greater than " + TOKEN_MAXIMUM_LENGTH));
        }
    }

    private void checkTypeConstraints() {
        if (this.accountMail.getType() == null) {
            this.validationHandler().append(new Error("'type' should not be null"));
        }
    }

    private void checkAccountConstraints() {
        if (this.accountMail.getAccount() == null) {
            this.validationHandler().append(new Error("'account' should not be null"));
        }
    }

    private void checkExpiresAtConstraints() {
        if (this.accountMail.getExpiresAt() == null) {
            this.validationHandler().append(new Error("'expiresAt' should not be null"));
            return;
        }

        if (this.accountMail.getExpiresAt().isBefore(InstantUtils.now())) {
            this.validationHandler().append(new Error("'expiresAt' should not be before now"));
        }
    }
}

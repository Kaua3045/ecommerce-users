package com.kaua.ecommerce.users.domain.accounts;

import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;
import com.kaua.ecommerce.users.domain.validation.Validator;

public class AccountValidator extends Validator {

    private final Account account;
    private final int PASSWORD_MINIMUM_LENGTH = 8;

    public AccountValidator(final Account aAccount, final ValidationHandler aHandler) {
        super(aHandler);
        this.account = aAccount;
    }

    @Override
    public void validate() {
        if (account.getFirstName() == null || account.getFirstName().isBlank()) {
            this.validationHandler().append(new Error("'firstName' should not be null or blank"));
        }

        if (account.getLastName() == null || account.getLastName().isBlank()) {
            this.validationHandler().append(new Error("'lastName' should not be null or blank"));
        }

        if (account.getEmail() == null || account.getEmail().isBlank()) {
            this.validationHandler().append(new Error("'email' should not be null or blank"));
        }

        if (account.getPassword() == null || account.getPassword().isBlank()) {
            this.validationHandler().append(new Error("'password' should not be null or blank"));
        }

        if (account.getPassword() != null && account.getPassword().length() < PASSWORD_MINIMUM_LENGTH) {
            this.validationHandler().append(new Error("'password' should be at least " + PASSWORD_MINIMUM_LENGTH + " characters"));
        }

        if (account.getPassword() != null && !account.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z]).*$")) {
            this.validationHandler().append(new Error("'password' should contain at least one uppercase letter, one lowercase letter and one number"));
        }
    }
}

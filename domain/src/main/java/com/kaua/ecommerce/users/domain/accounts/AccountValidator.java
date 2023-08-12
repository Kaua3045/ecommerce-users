package com.kaua.ecommerce.users.domain.accounts;

import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;
import com.kaua.ecommerce.users.domain.validation.Validator;

public class AccountValidator extends Validator {

    private final Account account;
    private final int PASSWORD_MINIMUM_LENGTH = 8;
    private final int PASSWORD_MAXIMUM_LENGTH = 255;
    private final int NAME_MINIMUM_LENGTH = 3;
    private final int NAME_MAXIMUM_LENGTH = 255;

    public AccountValidator(final Account aAccount, final ValidationHandler aHandler) {
        super(aHandler);
        this.account = aAccount;
    }

    @Override
    public void validate() {
        if (account.getFirstName() == null || account.getFirstName().isBlank()) {
            this.validationHandler().append(new Error("'firstName' should not be null or blank"));
            return;
        }

        if (account.getFirstName().trim().length() < NAME_MINIMUM_LENGTH ||
                account.getFirstName().trim().length() > NAME_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error("'firstName' must be between " + NAME_MINIMUM_LENGTH + " and " + NAME_MAXIMUM_LENGTH + " characters"));
            return;
        }

        if (account.getLastName() == null || account.getLastName().isBlank()) {
            this.validationHandler().append(new Error("'lastName' should not be null or blank"));
            return;
        }

        if (account.getLastName().trim().length() < NAME_MINIMUM_LENGTH ||
                account.getLastName().trim().length() > NAME_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error("'lastName' must be between " + NAME_MINIMUM_LENGTH + " and " + NAME_MAXIMUM_LENGTH + " characters"));
            return;
        }

        if (account.getEmail() == null || account.getEmail().isBlank()) {
            this.validationHandler().append(new Error("'email' should not be null or blank"));
            return;
        }

        if (account.getPassword() == null || account.getPassword().isBlank()) {
            this.validationHandler().append(new Error("'password' should not be null or blank"));
            return;
        }

        if (account.getPassword().trim().length() < PASSWORD_MINIMUM_LENGTH ||
                account.getPassword().trim().length() > PASSWORD_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error("'password' must be between " + PASSWORD_MINIMUM_LENGTH + " and " + PASSWORD_MAXIMUM_LENGTH + " characters"));
            return;
        }

        if (!account.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z]).*$")) {
            this.validationHandler().append(new Error("'password' should contain at least one uppercase letter, one lowercase letter and one number"));
            return;
        }
    }
}

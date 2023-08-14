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
        checkFirstNameConstraints();
        checkLastNameConstraints();
        checkEmailConstraints();
        checkPasswordConstraints();
    }

    private void checkFirstNameConstraints() {
        if (this.account.getFirstName() == null || this.account.getFirstName().isBlank()) {
            this.validationHandler().append(new Error("'firstName' should not be null or blank"));
            return;
        }

        if (this.account.getFirstName().trim().length() < NAME_MINIMUM_LENGTH ||
                this.account.getFirstName().trim().length() > NAME_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error("'firstName' must be between " + NAME_MINIMUM_LENGTH + " and " + NAME_MAXIMUM_LENGTH + " characters"));
            return;
        }
    }

    private void checkLastNameConstraints() {
        if (this.account.getLastName() == null || this.account.getLastName().isBlank()) {
            this.validationHandler().append(new Error("'lastName' should not be null or blank"));
            return;
        }

        if (this.account.getLastName().trim().length() < NAME_MINIMUM_LENGTH ||
                this.account.getLastName().trim().length() > NAME_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error("'lastName' must be between " + NAME_MINIMUM_LENGTH + " and " + NAME_MAXIMUM_LENGTH + " characters"));
            return;
        }
    }

    private void checkEmailConstraints() {
        if (this.account.getEmail() == null || this.account.getEmail().isBlank()) {
            this.validationHandler().append(new Error("'email' should not be null or blank"));
            return;
        }
    }

    private void checkPasswordConstraints() {
        if (this.account.getPassword() == null || this.account.getPassword().isBlank()) {
            this.validationHandler().append(new Error("'password' should not be null or blank"));
            return;
        }

        if (this.account.getPassword().trim().length() < PASSWORD_MINIMUM_LENGTH ||
                this.account.getPassword().trim().length() > PASSWORD_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error("'password' must be between " + PASSWORD_MINIMUM_LENGTH + " and " + PASSWORD_MAXIMUM_LENGTH + " characters"));
            return;
        }

        if (!this.account.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z]).*$")) {
            this.validationHandler().append(new Error("'password' should contain at least one uppercase letter, one lowercase letter and one number"));
            return;
        }
    }
}

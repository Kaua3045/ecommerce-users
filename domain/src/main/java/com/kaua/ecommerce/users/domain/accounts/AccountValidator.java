package com.kaua.ecommerce.users.domain.accounts;

import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;
import com.kaua.ecommerce.users.domain.validation.Validator;

import static com.kaua.ecommerce.users.domain.utils.CommonErrorsMessages.commonLengthErrorMessage;
import static com.kaua.ecommerce.users.domain.utils.CommonErrorsMessages.commonNullOrBlankErrorMessage;

public class AccountValidator extends Validator {

    private final Account account;
    private static final int PASSWORD_MINIMUM_LENGTH = 8;
    private static final int PASSWORD_MAXIMUM_LENGTH = 255;
    private static final int NAME_MINIMUM_LENGTH = 3;
    private static final int NAME_MAXIMUM_LENGTH = 255;

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
            this.validationHandler().append(new Error(commonNullOrBlankErrorMessage("firstName")));
            return;
        }

        if (this.account.getFirstName().trim().length() < NAME_MINIMUM_LENGTH ||
                this.account.getFirstName().trim().length() > NAME_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error(commonLengthErrorMessage("firstName", NAME_MINIMUM_LENGTH, NAME_MAXIMUM_LENGTH)));
        }
    }

    private void checkLastNameConstraints() {
        if (this.account.getLastName() == null || this.account.getLastName().isBlank()) {
            this.validationHandler().append(new Error(commonNullOrBlankErrorMessage("lastName")));
            return;
        }

        if (this.account.getLastName().trim().length() < NAME_MINIMUM_LENGTH ||
                this.account.getLastName().trim().length() > NAME_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error(commonLengthErrorMessage("lastName", NAME_MINIMUM_LENGTH, NAME_MAXIMUM_LENGTH)));
        }
    }

    private void checkEmailConstraints() {
        if (this.account.getEmail() == null || this.account.getEmail().isBlank()) {
            this.validationHandler().append(new Error(commonNullOrBlankErrorMessage("email")));
        }
    }

    private void checkPasswordConstraints() {
        if (this.account.getPassword() == null || this.account.getPassword().isBlank()) {
            this.validationHandler().append(new Error(commonNullOrBlankErrorMessage("password")));
            return;
        }

        if (this.account.getPassword().trim().length() < PASSWORD_MINIMUM_LENGTH ||
                this.account.getPassword().trim().length() > PASSWORD_MAXIMUM_LENGTH) {
            this.validationHandler().append(new Error(commonLengthErrorMessage("password", PASSWORD_MINIMUM_LENGTH, PASSWORD_MAXIMUM_LENGTH)));
            return;
        }

        if (!this.account.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z]).*$")) {
            this.validationHandler().append(new Error("'password' should contain at least one uppercase letter, one lowercase letter and one number"));
        }
    }
}

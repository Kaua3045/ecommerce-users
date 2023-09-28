package com.kaua.ecommerce.users.domain.permissions;

import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;
import com.kaua.ecommerce.users.domain.validation.Validator;

import static com.kaua.ecommerce.users.domain.utils.CommonErrorsMessages.commonLengthErrorMessage;
import static com.kaua.ecommerce.users.domain.utils.CommonErrorsMessages.commonNullOrBlankErrorMessage;

public class PermissionValidator extends Validator {

    private final Permission permission;
    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 50;
    private static final int DESCRIPTION_MAX_LENGTH = 255;

    public PermissionValidator(final ValidationHandler aHandler, final Permission permission) {
        super(aHandler);
        this.permission = permission;
    }

    @Override
    public void validate() {
        checkConstraintsName();
        checkConstraintsDescription();
    }

    private void checkConstraintsName() {
        if (this.permission.getName().isBlank() || this.permission.getName() == null) {
            this.validationHandler().append(new Error(commonNullOrBlankErrorMessage("name")));
            return;
        }

        if (this.permission.getName().trim().length() < NAME_MIN_LENGTH ||
                this.permission.getName().trim().length() > NAME_MAX_LENGTH) {
            this.validationHandler().append(new Error(commonLengthErrorMessage("name", NAME_MIN_LENGTH, NAME_MAX_LENGTH)));
            return;
        }
    }

    private void checkConstraintsDescription() {
        if (this.permission.getDescription() != null &&
                this.permission.getDescription().trim().length() > DESCRIPTION_MAX_LENGTH) {
            this.validationHandler().append(new Error(commonLengthErrorMessage("description", 0, DESCRIPTION_MAX_LENGTH)));
            return;
        }
    }
}

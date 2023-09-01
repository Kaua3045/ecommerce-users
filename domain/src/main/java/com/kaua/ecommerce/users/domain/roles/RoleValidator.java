package com.kaua.ecommerce.users.domain.roles;

import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.ValidationHandler;
import com.kaua.ecommerce.users.domain.validation.Validator;

import static com.kaua.ecommerce.users.domain.utils.CommonErrorsMessages.commonLengthErrorMessage;
import static com.kaua.ecommerce.users.domain.utils.CommonErrorsMessages.commonNullOrBlankErrorMessage;

public class RoleValidator extends Validator {

    private final Role role;
    private final int MINIMUM_NAME_LENGTH = 3;
    private final int MAXIMUM_NAME_LENGTH = 50;
    private final int MAXIMUM_DESCRIPTION_LENGTH = 255;

    protected RoleValidator(final Role aRole, final ValidationHandler aHandler) {
        super(aHandler);
        this.role = aRole;
    }

    @Override
    public void validate() {
        this.checkNameConstraints();
        this.checkDescriptionConstraints();
        this.checkRoleTypeConstraints();
    }

    private void checkNameConstraints() {
        if (this.role.getName() == null || this.role.getName().isBlank()) {
            this.validationHandler().append(new Error(commonNullOrBlankErrorMessage("name")));
            return;
        }

        if (this.role.getName().trim().length() < MINIMUM_NAME_LENGTH ||
                this.role.getName().trim().length() > MAXIMUM_NAME_LENGTH) {
            this.validationHandler().append(new Error(commonLengthErrorMessage("name", MINIMUM_NAME_LENGTH, MAXIMUM_NAME_LENGTH)));
            return;
        }
    }

    private void checkDescriptionConstraints() {
        if (this.role.getDescription() != null && this.role.getDescription().trim().length() > MAXIMUM_DESCRIPTION_LENGTH) {
            this.validationHandler().append(new Error(commonLengthErrorMessage("description", 0, MAXIMUM_DESCRIPTION_LENGTH)));
            return;
        }
    }

    private void checkRoleTypeConstraints() {
        if (this.role.getRoleType() == null) {
            this.validationHandler().append(new Error("'roleType' must not be null"));
            return;
        }
    }
}

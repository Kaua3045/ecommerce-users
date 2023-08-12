package com.kaua.ecommerce.users.domain.accounts;

import com.kaua.ecommerce.users.domain.validation.ValidationHandler;
import com.kaua.ecommerce.users.domain.validation.Validator;

public class AccountValidator extends Validator {

    private final Account account;

    public AccountValidator(final Account aAccount, final ValidationHandler aHandler) {
        super(aHandler);
        this.account = aAccount;
    }

    @Override
    public void validate() {

    }
}

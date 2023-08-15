package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.accounts.code.AccountCode;

public interface AccountCodeGateway {

    AccountCode create(final AccountCode aAccountCode);
}

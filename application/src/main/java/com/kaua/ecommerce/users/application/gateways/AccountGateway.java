package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.accounts.Account;

public interface AccountGateway {

    Account create(Account aAccount);

    boolean existsByEmail(String aEmail);
}

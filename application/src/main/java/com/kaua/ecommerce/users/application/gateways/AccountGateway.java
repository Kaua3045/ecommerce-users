package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.accounts.Account;

import java.util.Optional;

public interface AccountGateway {

    Account create(Account aAccount);

    boolean existsByEmail(String aEmail);

    Optional<Account> findById(String aId);

    Optional<Account> findByEmail(String aEmail);

    Account update(Account aAccount);
}

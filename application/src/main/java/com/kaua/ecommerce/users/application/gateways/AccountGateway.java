package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;

import java.util.Optional;

public interface AccountGateway {

    Account create(Account aAccount);

    boolean existsByEmail(String aEmail);

    Optional<Account> findById(String aId);

    Optional<Account> findByEmail(String aEmail);

    Pagination<Account> findAll(SearchQuery aQuery);

    Account update(Account aAccount);

    void deleteById(String aId);
}

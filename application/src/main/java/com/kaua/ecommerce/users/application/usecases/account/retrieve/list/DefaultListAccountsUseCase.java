package com.kaua.ecommerce.users.application.usecases.account.retrieve.list;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListAccountsUseCase extends ListAccountsUseCase {

    private final AccountGateway accountGateway;

    public DefaultListAccountsUseCase(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Pagination<ListAccountsOutput> execute(SearchQuery aCommand) {
        return this.accountGateway.findAll(aCommand)
                .map(ListAccountsOutput::from);
    }
}

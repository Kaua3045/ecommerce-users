package com.kaua.ecommerce.users.application.usecases.role.retrieve.list;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListRolesUseCase extends ListRolesUseCase {

    private final RoleGateway roleGateway;

    public DefaultListRolesUseCase(final RoleGateway roleGateway) {
        this.roleGateway = Objects.requireNonNull(roleGateway);
    }

    @Override
    public Pagination<ListRolesOutput> execute(SearchQuery aQuery) {
        return this.roleGateway.findAll(aQuery)
                .map(ListRolesOutput::from);
    }
}

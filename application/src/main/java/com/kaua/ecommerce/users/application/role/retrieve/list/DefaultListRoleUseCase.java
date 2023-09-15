package com.kaua.ecommerce.users.application.role.retrieve.list;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.roles.RoleSearchQuery;

import java.util.Objects;

public class DefaultListRoleUseCase extends ListRoleUseCase {

    private final RoleGateway roleGateway;

    public DefaultListRoleUseCase(final RoleGateway roleGateway) {
        this.roleGateway = Objects.requireNonNull(roleGateway);
    }

    @Override
    public Pagination<ListRoleOutput> execute(RoleSearchQuery aQuery) {
        return this.roleGateway.findAll(aQuery)
                .map(ListRoleOutput::from);
    }
}

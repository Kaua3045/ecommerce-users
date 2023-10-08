package com.kaua.ecommerce.users.application.usecases.permission.retrieve.list;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListPermissionsUseCase extends ListPermissionsUseCase {

    private final PermissionGateway permissionGateway;

    public DefaultListPermissionsUseCase(final PermissionGateway permissionGateway) {
        this.permissionGateway = Objects.requireNonNull(permissionGateway);
    }

    @Override
    public Pagination<ListPermissionsOutput> execute(SearchQuery aQuery) {
        return this.permissionGateway.findAll(aQuery)
                .map(ListPermissionsOutput::from);
    }
}

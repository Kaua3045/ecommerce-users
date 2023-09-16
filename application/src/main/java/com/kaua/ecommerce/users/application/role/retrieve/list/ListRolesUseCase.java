package com.kaua.ecommerce.users.application.role.retrieve.list;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.roles.RoleSearchQuery;

public abstract class ListRolesUseCase extends UseCase<RoleSearchQuery, Pagination<ListRolesOutput>> {
}

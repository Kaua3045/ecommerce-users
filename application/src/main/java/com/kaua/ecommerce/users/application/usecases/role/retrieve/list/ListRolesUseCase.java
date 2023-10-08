package com.kaua.ecommerce.users.application.usecases.role.retrieve.list;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;

public abstract class ListRolesUseCase extends UseCase<SearchQuery, Pagination<ListRolesOutput>> {
}

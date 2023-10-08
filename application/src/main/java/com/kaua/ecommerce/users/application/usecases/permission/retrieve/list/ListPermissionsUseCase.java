package com.kaua.ecommerce.users.application.usecases.permission.retrieve.list;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;

public abstract class ListPermissionsUseCase extends UseCase<SearchQuery, Pagination<ListPermissionsOutput>> {
}

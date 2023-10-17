package com.kaua.ecommerce.users.application.usecases.account.retrieve.list;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;

public abstract class ListAccountsUseCase extends UseCase<SearchQuery, Pagination<ListAccountsOutput>> {
}

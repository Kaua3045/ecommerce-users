package com.kaua.ecommerce.users.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
        int currentPage,
        int perPage,
        int totalPages,
        long totalItems,
        List<T> items
) {

    public <R> Pagination<R> map(final Function<T, R> mapper) {
        final List<R> aNewList = this.items.stream()
                .map(mapper)
                .toList();

        return new Pagination<>(
                currentPage(),
                perPage(),
                totalPages(),
                totalItems(),
                aNewList
        );
    }
}

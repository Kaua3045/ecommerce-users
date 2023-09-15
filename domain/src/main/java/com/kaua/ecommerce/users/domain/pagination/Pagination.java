package com.kaua.ecommerce.users.domain.pagination;

import java.util.List;

public record Pagination<T>(
        int currentPage,
        int perPage,
        int totalPage,
        long total,
        List<T> items
) {
}

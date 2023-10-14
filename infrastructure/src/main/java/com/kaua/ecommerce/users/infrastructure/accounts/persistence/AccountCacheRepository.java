package com.kaua.ecommerce.users.infrastructure.accounts.persistence;

import org.springframework.data.repository.CrudRepository;

public interface AccountCacheRepository extends CrudRepository<AccountCacheEntity, String> {
}

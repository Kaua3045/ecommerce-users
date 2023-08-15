package com.kaua.ecommerce.users.infrastructure.accounts.code.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountCodeJpaRepository extends JpaRepository<AccountCodeJpaEntity, String> {
}

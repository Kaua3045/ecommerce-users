package com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountMailJpaRepository extends JpaRepository<AccountMailJpaEntity, String> {

    List<AccountMailJpaEntity> findAllByAccountId(String accountId);
}

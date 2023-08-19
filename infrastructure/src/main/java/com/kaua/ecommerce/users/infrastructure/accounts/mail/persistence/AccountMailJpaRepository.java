package com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountMailJpaRepository extends JpaRepository<AccountMailJpaEntity, String> {

    List<AccountMailJpaEntity> findAllByAccountId(String accountId);

    Optional<AccountMailJpaEntity> findByToken(String token);
}

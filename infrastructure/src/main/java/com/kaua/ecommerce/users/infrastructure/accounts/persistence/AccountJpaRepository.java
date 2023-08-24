package com.kaua.ecommerce.users.infrastructure.accounts.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountJpaEntity, String> {

    boolean existsByEmail(String email);

    Optional<AccountJpaEntity> findByEmail(String email);
}

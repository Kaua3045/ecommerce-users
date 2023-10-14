package com.kaua.ecommerce.users.infrastructure.accounts.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface AccountJpaRepository extends JpaRepository<AccountJpaEntity, String> {

    boolean existsByEmail(String email);

    Optional<AccountJpaEntity> findByEmail(String email);

    @Query(value = "SELECT * FROM accounts WHERE role_id = :roleId", nativeQuery = true)
    Set<AccountJpaEntity> findAllWhereRoleId(@Param("roleId") String roleId);
}

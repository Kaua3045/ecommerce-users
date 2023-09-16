package com.kaua.ecommerce.users.infrastructure.roles.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, String> {

    boolean existsByName(String name);

    Page<RoleJpaEntity> findAll(Specification<RoleJpaEntity> whereClause, Pageable page);

    @Query(value = "SELECT * FROM roles r WHERE r.is_default = true", nativeQuery = true)
    Optional<RoleJpaEntity> findIsDefaultTrue();
}

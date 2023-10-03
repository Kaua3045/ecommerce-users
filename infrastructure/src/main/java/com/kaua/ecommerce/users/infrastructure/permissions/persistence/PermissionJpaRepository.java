package com.kaua.ecommerce.users.infrastructure.permissions.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionJpaRepository extends JpaRepository<PermissionJpaEntity, String> {

    boolean existsByName(String name);

    Page<PermissionJpaEntity> findAll(Specification<PermissionJpaEntity> whereClause, Pageable page);
}

package com.kaua.ecommerce.users.infrastructure.permissions.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PermissionJpaRepository extends JpaRepository<PermissionJpaEntity, String> {

    boolean existsByName(String name);

    Page<PermissionJpaEntity> findAll(Specification<PermissionJpaEntity> whereClause, Pageable page);

    @Query(value = "SELECT NEW com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity(" +
            "p.id, p.name) " +
            "FROM Permission p " +
            "WHERE p.id IN :ids")
    Set<PermissionJpaEntity> findAllByIds(@Param("ids") Set<String> ids);
}

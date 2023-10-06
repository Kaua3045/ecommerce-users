package com.kaua.ecommerce.users.infrastructure.permissions.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionJpaRepository extends JpaRepository<PermissionJpaEntity, String> {

    boolean existsByName(String name);

    Page<PermissionJpaEntity> findAll(Specification<PermissionJpaEntity> whereClause, Pageable page);

    @Query(value = "select p.permission_id, p.name from permissions p where p.permission_id in :ids")
    List<PermissionJpaEntity> findAllByIds(@Param("ids") List<String> ids);
}

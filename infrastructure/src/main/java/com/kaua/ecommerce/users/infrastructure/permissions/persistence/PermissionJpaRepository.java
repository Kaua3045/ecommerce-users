package com.kaua.ecommerce.users.infrastructure.permissions.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionJpaRepository extends JpaRepository<PermissionJpaEntity, String> {

    boolean existsByName(String name);
}

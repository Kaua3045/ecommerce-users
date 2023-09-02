package com.kaua.ecommerce.users.infrastructure.roles.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, String> {

    boolean existsByName(String name);
}

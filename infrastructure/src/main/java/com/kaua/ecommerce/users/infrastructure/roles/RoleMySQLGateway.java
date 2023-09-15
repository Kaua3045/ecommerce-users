package com.kaua.ecommerce.users.infrastructure.roles;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleSearchQuery;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import com.kaua.ecommerce.users.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class RoleMySQLGateway implements RoleGateway {

    private final RoleJpaRepository roleRepository;

    public RoleMySQLGateway(final RoleJpaRepository roleRepository) {
        this.roleRepository = Objects.requireNonNull(roleRepository);
    }

    @Override
    public Role create(Role aRole) {
        return this.roleRepository.save(RoleJpaEntity.toEntity(aRole)).toDomain();
    }

    @Override
    public boolean existsByName(String aName) {
        return this.roleRepository.existsByName(aName);
    }

    @Override
    public Optional<Role> findById(String aId) {
        return this.roleRepository.findById(aId).map(RoleJpaEntity::toDomain);
    }

    @Override
    public Pagination<Role> findAll(RoleSearchQuery aQuery) {
        final var aPage = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var aSpecification = Optional.ofNullable(aQuery.terms())
                .filter(term -> !term.isBlank())
                .map(term -> SpecificationUtils.<RoleJpaEntity>like("name", term)
                        .or(SpecificationUtils.<RoleJpaEntity>like("description", term)
                                .or(SpecificationUtils.like("roleType", term)))).orElse(null);

        final var aPageResult = this.roleRepository.findAll(Specification.where(aSpecification), aPage);

        return new Pagination<>(
                aPageResult.getNumber(),
                aPageResult.getSize(),
                aPageResult.getTotalPages(),
                aPageResult.getTotalElements(),
                aPageResult.map(RoleJpaEntity::toDomain).toList()
        );
    }

    @Override
    public Role update(Role aRole) {
        return this.roleRepository.save(RoleJpaEntity.toEntity(aRole)).toDomain();
    }

    @Override
    public void deleteById(String aId) {
        if (this.roleRepository.existsById(aId)) {
            this.roleRepository.deleteById(aId);
        }
    }
}

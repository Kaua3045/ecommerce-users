package com.kaua.ecommerce.users.infrastructure.permissions;

import com.kaua.ecommerce.users.application.gateways.PermissionGateway;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.domain.permissions.Permission;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaEntity;
import com.kaua.ecommerce.users.infrastructure.permissions.persistence.PermissionJpaRepository;
import com.kaua.ecommerce.users.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class PermissionMySQLGateway implements PermissionGateway {

    private final PermissionJpaRepository permissionRepository;

    public PermissionMySQLGateway(final PermissionJpaRepository permissionRepository) {
        this.permissionRepository = Objects.requireNonNull(permissionRepository);
    }

    @Override
    public Permission create(Permission aPermission) {
        return this.permissionRepository.save(PermissionJpaEntity.toEntity(aPermission)).toDomain();
    }

    @Override
    public boolean existsByName(String aName) {
        return this.permissionRepository.existsByName(aName);
    }

    @Override
    public Optional<Permission> findById(String aId) {
        return this.permissionRepository.findById(aId).map(PermissionJpaEntity::toDomain);
    }

    @Override
    public Pagination<Permission> findAll(SearchQuery aQuery) {
        final var aPage = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var aSpecification = Optional.ofNullable(aQuery.terms())
                .filter(term -> !term.isBlank())
                .map(term -> SpecificationUtils.<PermissionJpaEntity>like("name", term)
                        .or(SpecificationUtils.like("description", term))).orElse(null);

        final var aPageResult = this.permissionRepository.findAll(aSpecification, aPage);

        return new Pagination<>(
                aPageResult.getNumber(),
                aPageResult.getSize(),
                aPageResult.getTotalPages(),
                aPageResult.getTotalElements(),
                aPageResult.map(PermissionJpaEntity::toDomain).toList()
        );
    }

    @Override
    public Permission update(Permission aPermission) {
        return this.permissionRepository.save(PermissionJpaEntity.toEntity(aPermission)).toDomain();
    }

    @Override
    public void deleteById(String aId) {
        if (this.permissionRepository.existsById(aId)) {
            this.permissionRepository.deleteById(aId);
        }
    }

    @Override
    public List<Permission> findAllByIds(List<String> permissions) {
        return this.permissionRepository.findAllByIds(permissions).stream()
                .map(PermissionJpaEntity::toDomain)
                .toList();
    }
}

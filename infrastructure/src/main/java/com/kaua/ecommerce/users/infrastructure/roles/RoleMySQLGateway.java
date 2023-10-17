package com.kaua.ecommerce.users.infrastructure.roles;

import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountCacheRepository;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaEntity;
import com.kaua.ecommerce.users.infrastructure.roles.persistence.RoleJpaRepository;
import com.kaua.ecommerce.users.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
public class RoleMySQLGateway implements RoleGateway {

    private final RoleJpaRepository roleRepository;
    private final AccountCacheRepository accountCacheRepository;
    private final AccountJpaRepository accountJpaRepository;

    public RoleMySQLGateway(
            final RoleJpaRepository roleRepository,
            final AccountCacheRepository accountCacheRepository,
            final AccountJpaRepository accountJpaRepository
    ) {
        this.roleRepository = Objects.requireNonNull(roleRepository);
        this.accountCacheRepository = Objects.requireNonNull(accountCacheRepository);
        this.accountJpaRepository = Objects.requireNonNull(accountJpaRepository);
    }

    @Override
    public Role create(Role aRole) {
        return this.roleRepository.save(RoleJpaEntity.toEntity(aRole)).toDomain();
    }

    @Override
    public boolean existsByName(String aName) {
        return this.roleRepository.existsByName(aName);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Role> findById(String aId) {
        return this.roleRepository.findById(aId).map(RoleJpaEntity::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Role> findDefaultRole() {
        return this.roleRepository.findIsDefaultTrue().map(RoleJpaEntity::toDomain);
    }

    @Override
    public Pagination<Role> findAll(SearchQuery aQuery) {
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
                aPageResult.map(RoleJpaEntity::toDomainPagination).toList()
        );
    }

    @Override
    public Role update(Role aRole) {
        this.invalidateAccountCacheAfterRoleChange(aRole.getId().getValue());
        return this.roleRepository.save(RoleJpaEntity.toEntity(aRole)).toDomain();
    }

    @Override
    public void deleteById(String aId) {
        if (this.roleRepository.existsById(aId)) {
            this.invalidateAccountCacheAfterRoleChange(aId);
            this.setDefaultRoleAfterRoleDeleted(aId);
            this.roleRepository.deleteById(aId);
        }
    }

    private void invalidateAccountCacheAfterRoleChange(final String aId) {
        this.accountCacheRepository.findAll()
                .forEach(account -> {
                    if (account.getRole().getId().equals(aId)) {
                        this.accountCacheRepository.deleteById(account.getId());
                    }
                });
    }

    private void setDefaultRoleAfterRoleDeleted(final String aId) {
        this.accountJpaRepository.findAllWhereRoleId(aId).forEach(account -> {
            final var aRole = this.roleRepository.findIsDefaultTrue()
                    .orElseThrow(NotFoundException.with(Role.class, "default"))
                    .toDomain();
            final var accountUpdated = account.toDomain().changeRole(aRole);
            this.accountJpaRepository.save(AccountJpaEntity.toEntity(accountUpdated));
        });
    }
}

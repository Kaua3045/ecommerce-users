package com.kaua.ecommerce.users.application.usecases.account.retrieve.list;

import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.pagination.Pagination;
import com.kaua.ecommerce.users.domain.pagination.SearchQuery;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.roles.RoleTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListAccountsUseCaseTest {

    @InjectMocks
    private DefaultListAccountsUseCase useCase;

    @Mock
    private AccountGateway accountGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(accountGateway);
    }

    @Test
    void givenAValidQuery_whenCallListAccounts_thenShouldReturnAccounts() {
        final var aRole = Role.newRole("user", null, RoleTypes.COMMON, true);
        final var accounts = List.of(Account.newAccount(
                "teste",
                "testes",
                "teste.testes@test.com",
                "12345678Ab*",
                aRole),
                Account.newAccount(
                        "fulano",
                        "fulaninho",
                        "fulaninho.fulano@fulaninho.com",
                        "12345678Ab*",
                        aRole)
        );

        final var page = 0;
        final var perPage = 10;
        final var totalPages = 1;
        final var terms = "";
        final var sort = "createdAt";
        final var direction = "asc";

        final var aQuery = new SearchQuery(page, perPage, terms, sort, direction);
        final var pagination = new Pagination<>(page, perPage, totalPages, accounts.size(), accounts);

        final var itemsCount = 2;
        final var resultItems = pagination.map(ListAccountsOutput::from);

        Mockito.when(accountGateway.findAll(aQuery)).thenReturn(pagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(itemsCount, actualResult.totalItems());
        Assertions.assertEquals(resultItems, actualResult);
        Assertions.assertEquals(page, actualResult.currentPage());
        Assertions.assertEquals(perPage, actualResult.perPage());
        Assertions.assertEquals(totalPages, actualResult.totalPages());
        Assertions.assertEquals(accounts.size(), actualResult.items().size());
    }

    @Test
    void givenAValidQuery_whenHasNoResult_thenShouldReturnEmptyAccounts() {
        final var accounts = List.<Account>of();

        final var page = 0;
        final var perPage = 10;
        final var totalPages = 1;
        final var terms = "";
        final var sort = "createdAt";
        final var direction = "asc";

        final var aQuery = new SearchQuery(page, perPage, terms, sort, direction);
        final var pagination = new Pagination<>(page, perPage, totalPages, accounts.size(), accounts);

        final var itemsCount = 0;
        final var resultItems = pagination.map(ListAccountsOutput::from);

        Mockito.when(accountGateway.findAll(aQuery)).thenReturn(pagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(itemsCount, actualResult.totalItems());
        Assertions.assertEquals(resultItems, actualResult);
        Assertions.assertEquals(page, actualResult.currentPage());
        Assertions.assertEquals(perPage, actualResult.perPage());
        Assertions.assertEquals(totalPages, actualResult.totalPages());
        Assertions.assertEquals(accounts.size(), actualResult.items().size());
    }
}

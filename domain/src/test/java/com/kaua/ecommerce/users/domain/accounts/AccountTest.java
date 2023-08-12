package com.kaua.ecommerce.users.domain.accounts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    public void givenAValidValues_whenCallsNewAccount_thenAnAccountShouldBeCreated() {
        // given
        final var aFirstName = "Kaua";
        final var aLastName = "Pereira";
        final var aEmail = "teste@teste.com";
        final var aPassword = "123456Ab";

        // when
        final var aAccount = Account.newAccount(
                aFirstName,
                aLastName,
                aEmail,
                aPassword
        );

        // then
        Assertions.assertNotNull(aAccount.getId());
        Assertions.assertEquals(aFirstName, aAccount.getFirstName());
        Assertions.assertEquals(aLastName, aAccount.getLastName());
        Assertions.assertEquals(aEmail, aAccount.getEmail());
        Assertions.assertEquals(aPassword, aAccount.getPassword());
        Assertions.assertEquals(AccountMailStatus.WAITING_CONFIRMATION, aAccount.getMailStatus());
        Assertions.assertNull(aAccount.getAvatarUrl());
        Assertions.assertNotNull(aAccount.getCreatedAt());
        Assertions.assertNotNull(aAccount.getUpdatedAt());
    }
}

package com.kaua.ecommerce.users.infrastructure.account.persistence;

import com.kaua.ecommerce.users.MySQLGatewayTest;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import org.hibernate.PropertyValueException;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

@MySQLGatewayTest
public class AccountRepositoryTest {

    @Autowired
    private AccountJpaRepository accountRepository;

    @Test
    public void givenAnInvalidNullFirstName_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "firstName";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity.firstName";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aEntity = AccountJpaEntity.toEntity(aAccount);
        aEntity.setFirstName(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullLastName_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "lastName";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity.lastName";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aEntity = AccountJpaEntity.toEntity(aAccount);
        aEntity.setLastName(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullEmail_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "email";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity.email";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aEntity = AccountJpaEntity.toEntity(aAccount);
        aEntity.setEmail(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullPassword_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "password";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity.password";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aEntity = AccountJpaEntity.toEntity(aAccount);
        aEntity.setPassword(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullMailStatus_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "mailStatus";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity.mailStatus";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aEntity = AccountJpaEntity.toEntity(aAccount);
        aEntity.setMailStatus(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "createdAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity.createdAt";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aEntity = AccountJpaEntity.toEntity(aAccount);
        aEntity.setCreatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "updatedAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity.updatedAt";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aEntity = AccountJpaEntity.toEntity(aAccount);
        aEntity.setUpdatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullId_whenCallSave_shouldReturnAnException() {
        final var expectedErrorMessage = "ids for this class must be manually assigned before calling save(): com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aEntity = AccountJpaEntity.toEntity(aAccount);
        aEntity.setId(null);

        final var actualException = Assertions.assertThrows(JpaSystemException.class,
                () -> accountRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(IdentifierGenerationException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAValidAvatarUrl_whenCallSave_shouldDoesNotThrowException() {
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aEntity = AccountJpaEntity.toEntity(aAccount);
        aEntity.setAvatarUrl("http://localhost/avatar.png");

        final var actualOutput = Assertions.assertDoesNotThrow(() -> accountRepository.save(aEntity));

        Assertions.assertEquals(aEntity.getAvatarUrl(), actualOutput.getAvatarUrl());
    }
}

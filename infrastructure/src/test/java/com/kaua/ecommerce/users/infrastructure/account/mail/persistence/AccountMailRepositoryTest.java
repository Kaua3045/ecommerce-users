package com.kaua.ecommerce.users.infrastructure.account.mail.persistence;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.MySQLGatewayTest;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaRepository;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import org.hibernate.PropertyValueException;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

import java.time.temporal.ChronoUnit;

@IntegrationTest
public class AccountMailRepositoryTest {

    @Autowired
    private AccountMailJpaRepository accountMailRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Test
    void givenAnInvalidNullToken_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "token";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaEntity.token";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountMail = AccountMail.newAccountMail(
                RandomStringUtils.generateValue(36),
                AccountMailType.ACCOUNT_CONFIRMATION,
                aAccount,
                InstantUtils.now().plus(1, ChronoUnit.HOURS)
        );

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var aEntity = AccountMailJpaEntity.toEntity(aAccountMail);
        aEntity.setToken(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountMailRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullType_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "type";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaEntity.type";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountMail = AccountMail.newAccountMail(
                RandomStringUtils.generateValue(36),
                AccountMailType.ACCOUNT_CONFIRMATION,
                aAccount,
                InstantUtils.now().plus(1, ChronoUnit.HOURS)
        );

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var aEntity = AccountMailJpaEntity.toEntity(aAccountMail);
        aEntity.setType(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountMailRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullExpiresAt_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "expiresAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaEntity.expiresAt";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountMail = AccountMail.newAccountMail(
                RandomStringUtils.generateValue(36),
                AccountMailType.ACCOUNT_CONFIRMATION,
                aAccount,
                InstantUtils.now().plus(1, ChronoUnit.HOURS)
        );

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var aEntity = AccountMailJpaEntity.toEntity(aAccountMail);
        aEntity.setExpiresAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountMailRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullAccount_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "account";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaEntity.account";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountMail = AccountMail.newAccountMail(
                RandomStringUtils.generateValue(36),
                AccountMailType.ACCOUNT_CONFIRMATION,
                aAccount,
                InstantUtils.now().plus(1, ChronoUnit.HOURS)
        );

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var aEntity = AccountMailJpaEntity.toEntity(aAccountMail);
        aEntity.setAccount(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountMailRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullCreatedAt_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "createdAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaEntity.createdAt";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountMail = AccountMail.newAccountMail(
                RandomStringUtils.generateValue(36),
                AccountMailType.ACCOUNT_CONFIRMATION,
                aAccount,
                InstantUtils.now().plus(1, ChronoUnit.HOURS)
        );

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var aEntity = AccountMailJpaEntity.toEntity(aAccountMail);
        aEntity.setCreatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountMailRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullUpdatedAt_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "updatedAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaEntity.updatedAt";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountMail = AccountMail.newAccountMail(
                RandomStringUtils.generateValue(36),
                AccountMailType.ACCOUNT_CONFIRMATION,
                aAccount,
                InstantUtils.now().plus(1, ChronoUnit.HOURS)
        );

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var aEntity = AccountMailJpaEntity.toEntity(aAccountMail);
        aEntity.setUpdatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountMailRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullId_whenCallSave_shouldReturnAnException() {
        final var expectedErrorMessage = "ids for this class must be manually assigned before calling save(): com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaEntity";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountMail = AccountMail.newAccountMail(
                RandomStringUtils.generateValue(36),
                AccountMailType.ACCOUNT_CONFIRMATION,
                aAccount,
                InstantUtils.now().plus(1, ChronoUnit.HOURS)
        );

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var aEntity = AccountMailJpaEntity.toEntity(aAccountMail);
        aEntity.setId(null);

        final var actualException = Assertions.assertThrows(JpaSystemException.class,
                () -> accountMailRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(IdentifierGenerationException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }
}

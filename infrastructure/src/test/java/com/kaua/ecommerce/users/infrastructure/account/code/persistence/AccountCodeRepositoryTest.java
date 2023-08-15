package com.kaua.ecommerce.users.infrastructure.account.code.persistence;

import com.kaua.ecommerce.users.MySQLGatewayTest;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.code.AccountCode;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.infrastructure.accounts.code.persistence.AccountCodeJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.code.persistence.AccountCodeJpaRepository;
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
public class AccountCodeRepositoryTest {

    @Autowired
    private AccountCodeJpaRepository accountCodeRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Test
    public void givenAnInvalidNullCode_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "code";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.code.persistence.AccountCodeJpaEntity.code";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountCode = AccountCode.newAccountCode(
                RandomStringUtils.generateValue(36),
                RandomStringUtils.generateValue(50),
                aAccount.getId());

        final var aEntity = AccountCodeJpaEntity.toEntity(aAccountCode, aAccount);
        aEntity.setCode(null);

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountCodeRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullCodeChallenge_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "codeChallenge";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.code.persistence.AccountCodeJpaEntity.codeChallenge";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountCode = AccountCode.newAccountCode(
                RandomStringUtils.generateValue(36),
                RandomStringUtils.generateValue(50),
                aAccount.getId());

        final var aEntity = AccountCodeJpaEntity.toEntity(aAccountCode, aAccount);
        aEntity.setCodeChallenge(null);

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountCodeRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullAccount_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "account";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.code.persistence.AccountCodeJpaEntity.account";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountCode = AccountCode.newAccountCode(
                RandomStringUtils.generateValue(36),
                RandomStringUtils.generateValue(50),
                aAccount.getId());

        final var aEntity = AccountCodeJpaEntity.toEntity(aAccountCode, aAccount);
        aEntity.setAccount(null);

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountCodeRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "createdAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.code.persistence.AccountCodeJpaEntity.createdAt";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountCode = AccountCode.newAccountCode(
                RandomStringUtils.generateValue(36),
                RandomStringUtils.generateValue(50),
                aAccount.getId());

        final var aEntity = AccountCodeJpaEntity.toEntity(aAccountCode, aAccount);
        aEntity.setCreatedAt(null);

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountCodeRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "updatedAt";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.ecommerce.users.infrastructure.accounts.code.persistence.AccountCodeJpaEntity.updatedAt";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountCode = AccountCode.newAccountCode(
                RandomStringUtils.generateValue(36),
                RandomStringUtils.generateValue(50),
                aAccount.getId());

        final var aEntity = AccountCodeJpaEntity.toEntity(aAccountCode, aAccount);
        aEntity.setUpdatedAt(null);

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> accountCodeRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullId_whenCallSave_shouldReturnAnException() {
        final var expectedErrorMessage = "ids for this class must be manually assigned before calling save(): com.kaua.ecommerce.users.infrastructure.accounts.code.persistence.AccountCodeJpaEntity";

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab");

        final var aAccountCode = AccountCode.newAccountCode(
                RandomStringUtils.generateValue(36),
                RandomStringUtils.generateValue(50),
                aAccount.getId());

        final var aEntity = AccountCodeJpaEntity.toEntity(aAccountCode, aAccount);
        aEntity.setId(null);

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        final var actualException = Assertions.assertThrows(JpaSystemException.class,
                () -> accountCodeRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(IdentifierGenerationException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }
}

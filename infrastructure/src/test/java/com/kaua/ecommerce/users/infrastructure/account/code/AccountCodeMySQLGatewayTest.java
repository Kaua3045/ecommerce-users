package com.kaua.ecommerce.users.infrastructure.account.code;

import com.kaua.ecommerce.users.MySQLGatewayTest;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountID;
import com.kaua.ecommerce.users.domain.accounts.code.AccountCode;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.infrastructure.accounts.code.AccountCodeMySQLGateway;
import com.kaua.ecommerce.users.infrastructure.accounts.code.persistence.AccountCodeJpaRepository;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class AccountCodeMySQLGatewayTest {

    @Autowired
    private AccountCodeMySQLGateway accountCodeGateway;

    @Autowired
    private AccountCodeJpaRepository accountCodeRepository;

    @Autowired
    private AccountJpaRepository accountRepository;

    @Test
    public void givenAValidAccountCodeAndValidAccount_whenCallCreate_shouldReturnANewAccountCode() {
        Assertions.assertEquals(0, accountRepository.count());

        final var aAccount = accountRepository.save(AccountJpaEntity
                .toEntity(Account.newAccount(
                                "Fulano",
                                "Silva",
                                "teste@teste.com",
                                "1234567Ab"))).toDomain();

        Assertions.assertEquals(1, accountRepository.count());

        final var aCode = RandomStringUtils.generateValue(36);
        final var aCodeChallenge = RandomStringUtils.generateValue(100);
        final var aAccountId = aAccount.getId();

        final var aAccountCode = AccountCode.newAccountCode(
                aCode,
                aCodeChallenge,
                aAccountId
        );

        Assertions.assertEquals(0, accountCodeRepository.count());

        final var actualAccountCode = accountCodeGateway.create(aAccountCode);

        Assertions.assertEquals(1, accountCodeRepository.count());

        Assertions.assertEquals(aAccountCode.getId(), actualAccountCode.getId());
        Assertions.assertEquals(aCode, actualAccountCode.getCode());
        Assertions.assertEquals(aCodeChallenge, actualAccountCode.getCodeChallenge());
        Assertions.assertEquals(aAccountId, actualAccountCode.getAccountID());
        Assertions.assertEquals(aAccountCode.getCreatedAt(), actualAccountCode.getCreatedAt());
        Assertions.assertEquals(aAccountCode.getUpdatedAt(), actualAccountCode.getUpdatedAt());

        final var actualEntity = accountCodeRepository.findById(actualAccountCode.getId().getValue()).get();

        Assertions.assertEquals(aAccountCode.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aCode, actualEntity.getCode());
        Assertions.assertEquals(aCodeChallenge, actualEntity.getCodeChallenge());
        Assertions.assertEquals(aAccountId.getValue(), actualEntity.getAccount().getId());
        Assertions.assertEquals(aAccountCode.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aAccountCode.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    public void givenAValidAccountCodeAndInvalidAccount_whenCallCreate_shouldReturnAnError() {
        final var aCode = RandomStringUtils.generateValue(36);
        final var aCodeChallenge = RandomStringUtils.generateValue(100);
        final var aAccountId = "123";
        final var expectedErrorMessage = "Account with id 123 was not found";

        final var aAccountCode = AccountCode.newAccountCode(
                aCode,
                aCodeChallenge,
                AccountID.from(aAccountId)
        );

        Assertions.assertEquals(0, accountCodeRepository.count());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () ->
                accountCodeGateway.create(aAccountCode));

        Assertions.assertEquals(0, accountCodeRepository.count());

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}

package com.kaua.ecommerce.users.infrastructure.account.mail;

import com.kaua.ecommerce.users.MySQLGatewayTest;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.domain.utils.RandomStringUtils;
import com.kaua.ecommerce.users.infrastructure.accounts.mail.AccountMailMySQLGateway;
import com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.mail.persistence.AccountMailJpaRepository;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaEntity;
import com.kaua.ecommerce.users.infrastructure.accounts.persistence.AccountJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;
import java.util.List;

@MySQLGatewayTest
public class AccountMailMySQLGatewayTest {

    @Autowired
    private AccountMailMySQLGateway accountMailGateway;

    @Autowired
    private AccountMailJpaRepository accountMailRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @BeforeEach
    private void setup() {
        accountMailRepository.deleteAll();
        accountJpaRepository.deleteAll();
    }

    @Test
    public void givenAValidAccountMail_whenCallCreate_shouldReturnANewAccountMail() {
        final var aToken = RandomStringUtils.generateValue(36);
        final var aType = AccountMailType.ACCOUNT_CONFIRMATION;
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab"
        );
        final var aExpiresAt = InstantUtils.now().plus(1, ChronoUnit.HOURS);

        final var aAccountMail = AccountMail.newAccountMail(
                aToken,
                aType,
                aAccount,
                aExpiresAt
        );

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        Assertions.assertEquals(0, accountMailRepository.count());

        final var actualAccountMail = accountMailGateway.create(aAccountMail);

        Assertions.assertEquals(1, accountMailRepository.count());

        Assertions.assertEquals(aAccountMail.getId(), actualAccountMail.getId());
        Assertions.assertEquals(aToken, actualAccountMail.getToken());
        Assertions.assertEquals(aType, actualAccountMail.getType());
        Assertions.assertEquals(aAccount, actualAccountMail.getAccount());
        Assertions.assertEquals(aExpiresAt, actualAccountMail.getExpiresAt());
        Assertions.assertEquals(aAccountMail.getCreatedAt(), actualAccountMail.getCreatedAt());
        Assertions.assertEquals(aAccountMail.getUpdatedAt(), actualAccountMail.getUpdatedAt());

        final var actualEntity = accountMailRepository.findById(actualAccountMail.getId().getValue()).get();

        Assertions.assertEquals(aAccountMail.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(aToken, actualEntity.getToken());
        Assertions.assertEquals(aType, actualEntity.getType());
        Assertions.assertEquals(aAccount, actualEntity.getAccount().toDomain());
        Assertions.assertEquals(aExpiresAt, actualEntity.getExpiresAt());
        Assertions.assertEquals(aAccountMail.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aAccountMail.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    public void givenAValidPrePersistedAccountMails_whenCallFindAllByAccountId_shouldReturnAListOfAccountMail() {
        final var expectedMailsCount = 2;

        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab"
        );

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        accountMailRepository.saveAll(List.of(
                AccountMailJpaEntity.toEntity(AccountMail.newAccountMail(
                        RandomStringUtils.generateValue(36),
                        AccountMailType.ACCOUNT_CONFIRMATION,
                        aAccount,
                        InstantUtils.now().plus(1, ChronoUnit.HOURS)
                )),
                AccountMailJpaEntity.toEntity(AccountMail.newAccountMail(
                        RandomStringUtils.generateValue(36),
                        AccountMailType.PASSWORD_RESET,
                        aAccount,
                        InstantUtils.now().plus(20, ChronoUnit.MINUTES)
                ))
        ));


        Assertions.assertEquals(2, accountMailRepository.count());

        final var actualAccountMails = accountMailGateway.findAllByAccountId(aAccount.getId().getValue());

        Assertions.assertEquals(expectedMailsCount, actualAccountMails.size());
    }

    @Test
    public void givenAValidPrePersistedAccountMail_whenCallFindAllByAccountId_shouldDoesNotThrowException() {
        final var aAccount = Account.newAccount(
                "Fulano",
                "Silva",
                "teste@teste.com",
                "1234567Ab"
        );

        final var aAccountMail = AccountMailJpaEntity.toEntity(AccountMail.newAccountMail(
                RandomStringUtils.generateValue(36),
                AccountMailType.ACCOUNT_CONFIRMATION,
                aAccount,
                InstantUtils.now().plus(1, ChronoUnit.HOURS)
        ));

        accountJpaRepository.save(AccountJpaEntity.toEntity(aAccount));

        accountMailRepository.save(aAccountMail);


        Assertions.assertEquals(1, accountMailRepository.count());

        Assertions.assertDoesNotThrow(() -> accountMailGateway.deleteById(aAccountMail.getId()));

        Assertions.assertEquals(0, accountMailRepository.count());
    }
}

package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;

import java.util.List;
import java.util.Optional;

public interface AccountMailGateway {

    AccountMail create(AccountMail accountMail);

    List<AccountMail> findAllByAccountId(String accountId);

    Optional<AccountMail> findByToken(String token);

    void deleteById(String id);
}

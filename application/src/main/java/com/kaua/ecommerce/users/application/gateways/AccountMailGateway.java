package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;

import java.util.List;

public interface AccountMailGateway {

    AccountMail create(AccountMail accountMail);

    List<AccountMail> findAllByAccountId(String accountId);

    void deleteById(String id);
}

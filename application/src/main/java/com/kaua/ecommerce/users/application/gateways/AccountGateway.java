package com.kaua.ecommerce.users.application.gateways;

import com.kaua.ecommerce.users.domain.accounts.Account;
import reactor.core.publisher.Mono;

public interface AccountGateway {

    Mono<Account> create(Account aAccount);

    Mono<Boolean> existsByEmail(String aEmail);
}

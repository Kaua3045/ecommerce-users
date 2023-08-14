package com.kaua.ecommerce.users.infrastructure.configurations;

import com.kaua.ecommerce.users.application.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.account.create.DefaultCreateAccountUseCase;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountUseCaseConfig {

    private final AccountGateway accountGateway;

    public AccountUseCaseConfig(final AccountGateway accountGateway) {
        this.accountGateway = accountGateway;
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase() {
        return new DefaultCreateAccountUseCase(accountGateway);
    }
}

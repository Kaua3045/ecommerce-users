package com.kaua.ecommerce.users.infrastructure.configurations.usecases;

import com.kaua.ecommerce.users.application.account.code.CreateAccountCodeUseCase;
import com.kaua.ecommerce.users.application.account.code.DefaultCreateAccountCodeUseCase;
import com.kaua.ecommerce.users.application.gateways.AccountCodeGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountCodeUseCaseConfig {

    private final AccountCodeGateway accountCodeGateway;

    public AccountCodeUseCaseConfig(final AccountCodeGateway accountCodeGateway) {
        this.accountCodeGateway = accountCodeGateway;
    }

    @Bean
    public CreateAccountCodeUseCase createAccountCodeUseCase() {
        return new DefaultCreateAccountCodeUseCase(accountCodeGateway);
    }
}

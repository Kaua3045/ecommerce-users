package com.kaua.ecommerce.users.infrastructure.configurations.usecases;

import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.account.mail.create.DefaultCreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class AccountMailUseCaseConfig {

    private final AccountMailGateway accountMailGateway;

    public AccountMailUseCaseConfig(final AccountMailGateway accountMailGateway) {
        this.accountMailGateway = Objects.requireNonNull(accountMailGateway);
    }

    @Bean
    public CreateAccountMailUseCase createAccountMailUseCase() {
        return new DefaultCreateAccountMailUseCase(accountMailGateway);
    }
}
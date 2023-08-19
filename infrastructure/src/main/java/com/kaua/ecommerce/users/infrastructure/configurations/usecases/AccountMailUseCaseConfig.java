package com.kaua.ecommerce.users.infrastructure.configurations.usecases;

import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.account.mail.create.DefaultCreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.application.gateways.QueueGateway;
import com.kaua.ecommerce.users.infrastructure.configurations.annotations.EmailQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class AccountMailUseCaseConfig {

    private final AccountMailGateway accountMailGateway;
    private final AccountGateway accountGateway;
    private final QueueGateway queueGateway;

    public AccountMailUseCaseConfig(
            final AccountMailGateway accountMailGateway,
            final AccountGateway accountGateway,
            @EmailQueue final QueueGateway queueGateway
    ) {
        this.accountMailGateway = Objects.requireNonNull(accountMailGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.queueGateway = Objects.requireNonNull(queueGateway);
    }

    @Bean
    public CreateAccountMailUseCase createAccountMailUseCase() {
        return new DefaultCreateAccountMailUseCase(accountMailGateway, accountGateway, queueGateway);
    }
}

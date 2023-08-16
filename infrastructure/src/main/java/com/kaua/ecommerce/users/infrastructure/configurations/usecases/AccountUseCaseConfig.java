package com.kaua.ecommerce.users.infrastructure.configurations.usecases;

import com.kaua.ecommerce.users.application.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.account.create.DefaultCreateAccountUseCase;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.EncrypterGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountUseCaseConfig {

    private final AccountGateway accountGateway;
    private final EncrypterGateway encrypterGateway;

    public AccountUseCaseConfig(final AccountGateway accountGateway, final EncrypterGateway encrypterGateway) {
        this.accountGateway = accountGateway;
        this.encrypterGateway = encrypterGateway;
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase() {
        return new DefaultCreateAccountUseCase(accountGateway, encrypterGateway);
    }
}

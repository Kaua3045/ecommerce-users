package com.kaua.ecommerce.users.infrastructure.configurations.usecases;

import com.kaua.ecommerce.users.application.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.account.create.DefaultCreateAccountUseCase;
import com.kaua.ecommerce.users.application.account.delete.DefaultDeleteAccountUseCase;
import com.kaua.ecommerce.users.application.account.delete.DeleteAccountUseCase;
import com.kaua.ecommerce.users.application.account.retrieve.get.DefaultGetAccountByIdUseCase;
import com.kaua.ecommerce.users.application.account.retrieve.get.GetAccountByIdUseCase;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.application.gateways.EncrypterGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountUseCaseConfig {

    private final AccountGateway accountGateway;
    private final EncrypterGateway encrypterGateway;
    private final AccountMailGateway accountMailGateway;

    public AccountUseCaseConfig(
            final AccountGateway accountGateway,
            final EncrypterGateway encrypterGateway,
            final AccountMailGateway accountMailGateway
    ) {
        this.accountGateway = accountGateway;
        this.encrypterGateway = encrypterGateway;
        this.accountMailGateway = accountMailGateway;
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase() {
        return new DefaultCreateAccountUseCase(accountGateway, encrypterGateway);
    }

    @Bean
    public GetAccountByIdUseCase getAccountByIdUseCase() {
        return new DefaultGetAccountByIdUseCase(accountGateway);
    }

    @Bean
    public DeleteAccountUseCase deleteAccountUseCase() {
        return new DefaultDeleteAccountUseCase(accountGateway, accountMailGateway);
    }
}

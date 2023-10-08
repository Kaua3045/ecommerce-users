package com.kaua.ecommerce.users.infrastructure.configurations.usecases;

import com.kaua.ecommerce.users.application.usecases.account.mail.confirm.ConfirmAccountMailUseCase;
import com.kaua.ecommerce.users.application.usecases.account.mail.confirm.DefaultConfirmAccountMailUseCase;
import com.kaua.ecommerce.users.application.usecases.account.mail.confirm.request.DefaultRequestAccountConfirmUseCase;
import com.kaua.ecommerce.users.application.usecases.account.mail.confirm.request.RequestAccountConfirmUseCase;
import com.kaua.ecommerce.users.application.usecases.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.usecases.account.mail.create.DefaultCreateAccountMailUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.password.DefaultRequestResetPasswordUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.password.RequestResetPasswordUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.password.reset.DefaultResetPasswordUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.password.reset.ResetPasswordUseCase;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.application.gateways.EncrypterGateway;
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
    private final EncrypterGateway encrypterGateway;

    public AccountMailUseCaseConfig(
            final AccountMailGateway accountMailGateway,
            final AccountGateway accountGateway,
            @EmailQueue final QueueGateway queueGateway,
            final EncrypterGateway encrypterGateway
    ) {
        this.accountMailGateway = Objects.requireNonNull(accountMailGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.queueGateway = Objects.requireNonNull(queueGateway);
        this.encrypterGateway = Objects.requireNonNull(encrypterGateway);
    }

    @Bean
    public CreateAccountMailUseCase createAccountMailUseCase() {
        return new DefaultCreateAccountMailUseCase(accountMailGateway, queueGateway);
    }

    @Bean
    public RequestAccountConfirmUseCase requestAccountConfirmUseCase() {
        return new DefaultRequestAccountConfirmUseCase(accountGateway, createAccountMailUseCase());
    }

    @Bean
    public ConfirmAccountMailUseCase confirmAccountMailUseCase() {
        return new DefaultConfirmAccountMailUseCase(accountMailGateway, accountGateway);
    }

    @Bean
    public RequestResetPasswordUseCase requestResetPasswordUseCase() {
        return new DefaultRequestResetPasswordUseCase(accountGateway, createAccountMailUseCase());
    }

    @Bean
    public ResetPasswordUseCase resetPasswordUseCase() {
        return new DefaultResetPasswordUseCase(accountGateway, encrypterGateway, accountMailGateway);
    }
}

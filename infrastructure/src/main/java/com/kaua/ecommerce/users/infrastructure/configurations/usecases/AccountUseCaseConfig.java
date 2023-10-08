package com.kaua.ecommerce.users.infrastructure.configurations.usecases;

import com.kaua.ecommerce.users.application.usecases.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.usecases.account.create.DefaultCreateAccountUseCase;
import com.kaua.ecommerce.users.application.usecases.account.delete.DefaultDeleteAccountUseCase;
import com.kaua.ecommerce.users.application.usecases.account.delete.DeleteAccountUseCase;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.get.DefaultGetAccountByIdUseCase;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.get.GetAccountByIdUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.avatar.DefaultUpdateAvatarUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.avatar.UpdateAvatarUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.role.DefaultUpdateAccountRoleUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.role.UpdateAccountRoleUseCase;
import com.kaua.ecommerce.users.application.gateways.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountUseCaseConfig {

    private final AccountGateway accountGateway;
    private final EncrypterGateway encrypterGateway;
    private final AccountMailGateway accountMailGateway;
    private final AvatarGateway avatarGateway;
    private final RoleGateway roleGateway;

    public AccountUseCaseConfig(
            final AccountGateway accountGateway,
            final EncrypterGateway encrypterGateway,
            final AccountMailGateway accountMailGateway,
            final AvatarGateway avatarGateway,
            final RoleGateway roleGateway
    ) {
        this.accountGateway = accountGateway;
        this.encrypterGateway = encrypterGateway;
        this.accountMailGateway = accountMailGateway;
        this.avatarGateway = avatarGateway;
        this.roleGateway = roleGateway;
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase() {
        return new DefaultCreateAccountUseCase(accountGateway, encrypterGateway, roleGateway);
    }

    @Bean
    public GetAccountByIdUseCase getAccountByIdUseCase() {
        return new DefaultGetAccountByIdUseCase(accountGateway);
    }

    @Bean
    public UpdateAvatarUseCase updateAvatarUseCase() {
        return new DefaultUpdateAvatarUseCase(avatarGateway, accountGateway);
    }

    @Bean
    public UpdateAccountRoleUseCase updateAccountRoleUseCase() {
        return new DefaultUpdateAccountRoleUseCase(accountGateway, roleGateway);
    }

    @Bean
    public DeleteAccountUseCase deleteAccountUseCase() {
        return new DefaultDeleteAccountUseCase(accountGateway, accountMailGateway, avatarGateway);
    }
}

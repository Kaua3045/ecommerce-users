package com.kaua.ecommerce.users.infrastructure.configurations.usecases;

import com.kaua.ecommerce.users.application.gateways.*;
import com.kaua.ecommerce.users.application.usecases.account.create.CreateAccountUseCase;
import com.kaua.ecommerce.users.application.usecases.account.create.DefaultCreateAccountUseCase;
import com.kaua.ecommerce.users.application.usecases.account.delete.DefaultDeleteAccountUseCase;
import com.kaua.ecommerce.users.application.usecases.account.delete.DeleteAccountUseCase;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.get.DefaultGetAccountByIdUseCase;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.get.GetAccountByIdUseCase;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.list.DefaultListAccountsUseCase;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.list.ListAccountsUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.avatar.DefaultUpdateAvatarUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.avatar.UpdateAvatarUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.role.DefaultUpdateAccountRoleUseCase;
import com.kaua.ecommerce.users.application.usecases.account.update.role.UpdateAccountRoleUseCase;
import com.kaua.ecommerce.users.domain.accounts.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountUseCaseConfig {

    private final AccountGateway accountGateway;
    private final CacheGateway<Account> accountCacheGateway;
    private final EncrypterGateway encrypterGateway;
    private final AccountMailGateway accountMailGateway;
    private final AvatarGateway avatarGateway;
    private final RoleGateway roleGateway;

    public AccountUseCaseConfig(
            final AccountGateway accountGateway,
            final CacheGateway<Account> accountCacheGateway,
            final EncrypterGateway encrypterGateway,
            final AccountMailGateway accountMailGateway,
            final AvatarGateway avatarGateway,
            final RoleGateway roleGateway
    ) {
        this.accountGateway = accountGateway;
        this.accountCacheGateway = accountCacheGateway;
        this.encrypterGateway = encrypterGateway;
        this.accountMailGateway = accountMailGateway;
        this.avatarGateway = avatarGateway;
        this.roleGateway = roleGateway;
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase() {
        return new DefaultCreateAccountUseCase(accountGateway, accountCacheGateway, encrypterGateway, roleGateway);
    }

    @Bean
    public GetAccountByIdUseCase getAccountByIdUseCase() {
        return new DefaultGetAccountByIdUseCase(accountGateway, accountCacheGateway);
    }

    @Bean
    public ListAccountsUseCase listAccountsUseCase() {
        return new DefaultListAccountsUseCase(accountGateway);
    }

    @Bean
    public UpdateAvatarUseCase updateAvatarUseCase() {
        return new DefaultUpdateAvatarUseCase(avatarGateway, accountGateway, accountCacheGateway);
    }

    @Bean
    public UpdateAccountRoleUseCase updateAccountRoleUseCase() {
        return new DefaultUpdateAccountRoleUseCase(accountGateway, accountCacheGateway, roleGateway);
    }

    @Bean
    public DeleteAccountUseCase deleteAccountUseCase() {
        return new DefaultDeleteAccountUseCase(accountGateway, accountMailGateway, avatarGateway);
    }
}

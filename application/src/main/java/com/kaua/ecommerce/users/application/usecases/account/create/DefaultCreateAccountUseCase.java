package com.kaua.ecommerce.users.application.usecases.account.create;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.EncrypterGateway;
import com.kaua.ecommerce.users.application.gateways.RoleGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.AccountCreatedEvent;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.roles.Role;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Objects;

public class DefaultCreateAccountUseCase extends CreateAccountUseCase {

    private final AccountGateway accountGateway;
    private final EncrypterGateway encrypterGateway;
    private final RoleGateway roleGateway;

    public DefaultCreateAccountUseCase(
            final AccountGateway accountGateway,
            final EncrypterGateway encrypterGateway,
            final RoleGateway roleGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.encrypterGateway = Objects.requireNonNull(encrypterGateway);
        this.roleGateway = Objects.requireNonNull(roleGateway);
    }

    @Override
    public Either<NotificationHandler, CreateAccountOutput> execute(final CreateAccountCommand aCommand) {
        final var notification = NotificationHandler.create();

        if (this.accountGateway.existsByEmail(aCommand.email())) {
            return Either.left(notification.append(new Error("'email' already exists")));
        }

        final var aRoleDefault = this.roleGateway.findDefaultRole()
                .orElseThrow(NotFoundException.with(Role.class, "default"));

        final var aAccount = Account.newAccount(
                aCommand.firstName(),
                aCommand.lastName(),
                aCommand.email(),
                aCommand.password(),
                aRoleDefault
        );
        aAccount.validate(notification);

        return notification.hasError()
                ? Either.left(notification)
                : Either.right(CreateAccountOutput.from(accountWithPasswordEncodded(aAccount)));
    }

    private Account accountWithPasswordEncodded(final Account aAccount) {
        aAccount.registerEvent(new AccountCreatedEvent(
                aAccount.getId().getValue(),
                aAccount.getFirstName(),
                aAccount.getLastName(),
                aAccount.getEmail()
        ));

        this.accountGateway.create(
                Account.with(
                        aAccount.getId().getValue(),
                        aAccount.getFirstName(),
                        aAccount.getLastName(),
                        aAccount.getEmail(),
                        aAccount.getMailStatus(),
                        this.encrypterGateway.encrypt(aAccount.getPassword()),
                        aAccount.getAvatarUrl(),
                        aAccount.getRole(),
                        aAccount.getCreatedAt(),
                        aAccount.getUpdatedAt(),
                        aAccount.getDomainEvents())
        );

        return aAccount;
    }
}

package com.kaua.ecommerce.users.application.account.mail.create;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountGateway;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.application.gateways.QueueGateway;
import com.kaua.ecommerce.users.domain.accounts.Account;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.domain.exceptions.NotFoundException;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Objects;

public class DefaultCreateAccountMailUseCase extends CreateAccountMailUseCase {

    private final AccountMailGateway accountMailGateway;
    private final AccountGateway accountGateway;
    private final QueueGateway queueGateway;

    public DefaultCreateAccountMailUseCase(
            final AccountMailGateway accountMailGateway,
            final AccountGateway accountGateway,
            final QueueGateway queueGateway
    ) {
        this.accountMailGateway = Objects.requireNonNull(accountMailGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.queueGateway = Objects.requireNonNull(queueGateway);
    }

    @Override
    public Either<NotificationHandler, CreateAccountMailOutput> execute(CreateAccountMailCommand aCommand) {
        if (aCommand.accountId() == null) {
            return Either.left(NotificationHandler.create(new Error("'accountId' should not be null")));
        }

        final var aAccount = this.accountGateway.findById(aCommand.accountId())
                .orElseThrow(() -> NotFoundException.with(Account.class, aCommand.accountId()));

        final var aAccountMails = this.accountMailGateway.findAllByAccountId(aAccount.getId().getValue());

        aAccountMails.stream()
                .filter(mail -> mail.getType() == aCommand.type())
                .forEach(mail -> this.accountMailGateway.deleteById(mail.getId().getValue()));

        final var aAccountMail = AccountMail.newAccountMail(
                aCommand.token(),
                aCommand.type(),
                aAccount,
                aCommand.expiresAt()
        );
        final var notification = NotificationHandler.create();
        aAccountMail.validate(notification);

        return notification.hasError()
                ? Either.left(notification)
                : Either.right(this.createAccountMail(aAccountMail, aCommand));
    }

    private CreateAccountMailOutput createAccountMail(AccountMail aAccountMail, CreateAccountMailCommand aCommand) {
        this.queueGateway.send(CreateMailQueueCommand.with(
                aAccountMail.getToken(),
                aAccountMail.getAccount().getFirstName(),
                aAccountMail.getAccount().getEmail(),
                aCommand.type().name()
        ));
        this.accountMailGateway.create(aAccountMail);

        return CreateAccountMailOutput.from(aAccountMail.getId().getValue());
    }
}

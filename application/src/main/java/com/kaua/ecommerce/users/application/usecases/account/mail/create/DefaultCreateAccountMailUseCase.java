package com.kaua.ecommerce.users.application.usecases.account.mail.create;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.application.gateways.QueueGateway;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Objects;

public class DefaultCreateAccountMailUseCase extends CreateAccountMailUseCase {

    private final AccountMailGateway accountMailGateway;
    private final QueueGateway queueGateway;

    public DefaultCreateAccountMailUseCase(
            final AccountMailGateway accountMailGateway,
            final QueueGateway queueGateway
    ) {
        this.accountMailGateway = Objects.requireNonNull(accountMailGateway);
        this.queueGateway = Objects.requireNonNull(queueGateway);
    }

    @Override
    public Either<NotificationHandler, CreateAccountMailOutput> execute(CreateAccountMailCommand aCommand) {
        if (aCommand.account() == null) {
            return Either.left(NotificationHandler.create(new Error("'account' should not be null")));
        }

        final var aAccountMails = this.accountMailGateway.findAllByAccountId(aCommand.account().getId().getValue());

        aAccountMails.stream()
                .filter(mail -> mail.getType() == aCommand.type())
                .forEach(mail -> this.accountMailGateway.deleteById(mail.getId().getValue()));

        final var aAccountMail = AccountMail.newAccountMail(
                aCommand.token(),
                aCommand.type(),
                aCommand.account(),
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

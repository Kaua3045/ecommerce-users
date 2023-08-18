package com.kaua.ecommerce.users.application.account.mail.create;

import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.application.gateways.AccountMailGateway;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMail;
import com.kaua.ecommerce.users.domain.validation.Error;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

import java.util.Objects;

public class DefaultCreateAccountMailUseCase extends CreateAccountMailUseCase {

    private final AccountMailGateway accountMailGateway;

    public DefaultCreateAccountMailUseCase(final AccountMailGateway accountMailGateway) {
        this.accountMailGateway = Objects.requireNonNull(accountMailGateway);
    }

    @Override
    public Either<NotificationHandler, AccountMail> execute(CreateAccountMailCommand aCommand) {
        if (aCommand.account() == null) {
            return Either.left(NotificationHandler.create(new Error("'account' should not be null")));
        }

        final var aAccountId = aCommand.account().getId().getValue();
        final var aAccountMails = this.accountMailGateway.findAllByAccountId(aAccountId);

        aAccountMails.stream()
                .filter(mail -> mail.getType() == aCommand.type())
                .forEach(mail -> this.accountMailGateway.deleteById(mail.getId().getValue()));

        final var aAccountMail = AccountMail.newAccountMail(
                aCommand.token(),
                aCommand.type(),
                aCommand.account(),
                aCommand.expirestAt()
        );
        final var notification = NotificationHandler.create();
        aAccountMail.validate(notification);

        // TODO: Send message to mail queue
        // FILA, SUBJECT, EMAIL e TOKEN
        // this.messageGateway.send("mail-queue", aCommand.subject(), aCommand.email(), aCommand.token());

        return notification.hasError()
                ? Either.left(notification)
                : Either.right(this.accountMailGateway.create(aAccountMail));
    }
}

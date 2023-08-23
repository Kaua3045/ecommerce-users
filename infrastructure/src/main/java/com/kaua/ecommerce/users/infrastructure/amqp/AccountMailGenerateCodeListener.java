package com.kaua.ecommerce.users.infrastructure.amqp;

import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailCommand;
import com.kaua.ecommerce.users.application.account.mail.create.CreateAccountMailUseCase;
import com.kaua.ecommerce.users.domain.accounts.AccountCreatedEvent;
import com.kaua.ecommerce.users.domain.accounts.mail.AccountMailType;
import com.kaua.ecommerce.users.domain.utils.IdUtils;
import com.kaua.ecommerce.users.domain.utils.InstantUtils;
import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
public class AccountMailGenerateCodeListener {

    public static final String ACCOUNT_MAIL_GENERATE_CODE_LISTENER = "AccountMailGenerateCodeListener";
    private static final String ACCOUNT_MAIL_GENERATE_CODE_QUEUE = "${amqp.queues.account-created-generate-confirmation-code-mail.queue}";

    private final CreateAccountMailUseCase createAccountMailUseCase;

    public AccountMailGenerateCodeListener(final CreateAccountMailUseCase createAccountMailUseCase) {
        this.createAccountMailUseCase = Objects.requireNonNull(createAccountMailUseCase);
    }

    @RabbitListener(id = ACCOUNT_MAIL_GENERATE_CODE_LISTENER, queues = ACCOUNT_MAIL_GENERATE_CODE_QUEUE)
    public void onGenerateAccountMailCode(@Payload final String message) {
        final var aResult = Json.readValue(message, AccountCreatedEvent.class);

        final var aCommand = CreateAccountMailCommand.with(
                aResult.id(),
                IdUtils.generate().replace("-", ""),
                AccountMailType.ACCOUNT_CONFIRMATION,
                "Confirmar sua conta",
                InstantUtils.now().plus(1, ChronoUnit.HOURS)
        );

        this.createAccountMailUseCase.execute(aCommand);
    }
}

package com.kaua.ecommerce.users.application.account.mail.create;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

public abstract class CreateAccountMailUseCase extends
        UseCase<CreateAccountMailCommand, Either<NotificationHandler, CreateAccountMailOutput>> {
}

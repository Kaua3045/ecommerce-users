package com.kaua.ecommerce.users.application.usecases.account.mail.confirm;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

public abstract class ConfirmAccountMailUseCase extends
        UseCase<ConfirmAccountMailCommand, Either<NotificationHandler, Boolean>> {
}

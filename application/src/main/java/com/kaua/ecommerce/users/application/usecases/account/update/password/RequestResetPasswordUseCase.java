package com.kaua.ecommerce.users.application.usecases.account.update.password;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.application.usecases.account.mail.create.CreateAccountMailOutput;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

public abstract class RequestResetPasswordUseCase
        extends UseCase<RequestResetPasswordCommand, Either<NotificationHandler, CreateAccountMailOutput>> {
}

package com.kaua.ecommerce.users.application.usecases.account.mail.confirm.request;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.application.usecases.account.mail.create.CreateAccountMailOutput;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

public abstract class RequestAccountConfirmUseCase
        extends UseCase<RequestAccountConfirmCommand, Either<NotificationHandler, CreateAccountMailOutput>> {
}

package com.kaua.ecommerce.users.application.usecases.account.create;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

public abstract class CreateAccountUseCase
        extends UseCase<CreateAccountCommand, Either<NotificationHandler, CreateAccountOutput>> {
}

package com.kaua.ecommerce.users.application.usecases.role.create;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

public abstract class CreateRoleUseCase extends
        UseCase<CreateRoleCommand, Either<NotificationHandler, CreateRoleOutput>> {
}

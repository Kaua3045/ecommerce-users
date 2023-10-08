package com.kaua.ecommerce.users.application.usecases.permission.update;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

public abstract class UpdatePermissionUseCase extends
        UseCase<UpdatePermissionCommand, Either<NotificationHandler, UpdatePermissionOutput>> {
}

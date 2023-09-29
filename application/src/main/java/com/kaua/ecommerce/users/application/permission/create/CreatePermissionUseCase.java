package com.kaua.ecommerce.users.application.permission.create;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

public abstract class CreatePermissionUseCase extends
        UseCase<CreatePermissionCommand, Either<NotificationHandler, CreatePermissionOutput>> {
}

package com.kaua.ecommerce.users.application.usecases.role.update;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

public abstract class UpdateRoleUseCase extends
        UseCase<UpdateRoleCommand, Either<NotificationHandler, UpdateRoleOutput>> {
}

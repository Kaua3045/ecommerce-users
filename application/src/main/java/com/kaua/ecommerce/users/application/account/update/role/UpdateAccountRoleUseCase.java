package com.kaua.ecommerce.users.application.account.update.role;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

public abstract class UpdateAccountRoleUseCase extends
        UseCase<UpdateAccountRoleCommand, Either<NotificationHandler, UpdateAccountRoleOutput>> {
}

package com.kaua.ecommerce.users.application.account.update.password.reset;

import com.kaua.ecommerce.users.application.UseCase;
import com.kaua.ecommerce.users.application.either.Either;
import com.kaua.ecommerce.users.domain.validation.handler.NotificationHandler;

public abstract class ResetPasswordUseCase extends UseCase<ResetPasswordCommand, Either<NotificationHandler, Boolean>> {
}

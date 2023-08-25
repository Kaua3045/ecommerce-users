package com.kaua.ecommerce.users.infrastructure.accounts.presenters;

import com.kaua.ecommerce.users.application.account.retrieve.get.GetAccountByIdOutput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.GetAccountPresenter;

public final class AccountApiPresenter {

    private AccountApiPresenter() {}

    public static GetAccountPresenter present(final GetAccountByIdOutput aOutput) {
        return new GetAccountPresenter(
                aOutput.id(),
                aOutput.firstName(),
                aOutput.lastName(),
                aOutput.email(),
                aOutput.avatarUrl(),
                aOutput.mailStatus(),
                aOutput.createdAt(),
                aOutput.updatedAt()
        );
    }
}

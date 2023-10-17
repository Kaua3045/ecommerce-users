package com.kaua.ecommerce.users.infrastructure.accounts.presenters;

import com.kaua.ecommerce.users.application.usecases.account.retrieve.get.GetAccountByIdOutput;
import com.kaua.ecommerce.users.application.usecases.account.retrieve.list.ListAccountsOutput;
import com.kaua.ecommerce.users.infrastructure.accounts.models.GetAccountPresenter;
import com.kaua.ecommerce.users.infrastructure.accounts.models.ListAccountsPresenter;

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
                aOutput.roleId(),
                aOutput.createdAt(),
                aOutput.updatedAt()
        );
    }

    public static ListAccountsPresenter present(final ListAccountsOutput aOutput) {
        return new ListAccountsPresenter(
                aOutput.id().getValue(),
                aOutput.firstName(),
                aOutput.lastName(),
                aOutput.email(),
                aOutput.createdAt().toString()
        );
    }
}

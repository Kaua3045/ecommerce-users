package com.kaua.ecommerce.users.application.usecases.account.update.avatar;

import com.kaua.ecommerce.users.domain.accounts.Account;

public record UpdateAvatarOutput(String id) {

    public static UpdateAvatarOutput from(final Account aAccount) {
        return new UpdateAvatarOutput(aAccount.getId().getValue());
    }
}

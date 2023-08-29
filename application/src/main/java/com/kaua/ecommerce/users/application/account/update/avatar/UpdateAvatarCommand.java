package com.kaua.ecommerce.users.application.account.update.avatar;

import com.kaua.ecommerce.users.domain.utils.Resource;

public record UpdateAvatarCommand(String accountId, Resource resource) {

    public static UpdateAvatarCommand with(final String aAccountId, final Resource aResource) {
        return new UpdateAvatarCommand(aAccountId, aResource);
    }
}

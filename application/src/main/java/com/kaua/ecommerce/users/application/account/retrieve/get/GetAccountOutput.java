package com.kaua.ecommerce.users.application.account.retrieve.get;

import com.kaua.ecommerce.users.domain.accounts.Account;

public record GetAccountOutput(
        String id,
        String firstName,
        String lastName,
        String email,
        String avatarUrl,
        String mailStatus,
        String createdAt,
        String updatedAt
) {

    public static GetAccountOutput from(final Account aAccount) {
        return new GetAccountOutput(
                aAccount.getId().getValue(),
                aAccount.getFirstName(),
                aAccount.getLastName(),
                aAccount.getEmail(),
                aAccount.getAvatarUrl(),
                aAccount.getMailStatus().name(),
                aAccount.getCreatedAt().toString(),
                aAccount.getUpdatedAt().toString()
        );
    }
}

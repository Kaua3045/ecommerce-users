package com.kaua.ecommerce.users.infrastructure.accounts.presenters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaua.ecommerce.users.application.account.retrieve.get.GetAccountOutput;

public record GetAccountPresenter(
        @JsonProperty("id") String id,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("email") String email,
        @JsonProperty("avatar_url") String avatarUrl,
        @JsonProperty("mail_status") String mailStatus,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt
) {

    public static GetAccountPresenter from(final GetAccountOutput aOutput) {
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

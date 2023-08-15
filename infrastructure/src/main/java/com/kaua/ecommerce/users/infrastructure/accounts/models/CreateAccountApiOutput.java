package com.kaua.ecommerce.users.infrastructure.accounts.models;

public record CreateAccountApiOutput(
        String id,
        String code,
        String codeChallenge
) {
}

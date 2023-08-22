package com.kaua.ecommerce.users.infrastructure.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.ecommerce.users.infrastructure.exceptions.SecretManagerException;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

public final class SecretsManagerService {

    private SecretsManagerService() {}

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getValue(final String secretName) {
        try(final var secretManagerClient = SecretsManagerClient.create()) {
            final var aRequest = secretManagerClient.getSecretValue(builder -> builder
                    .secretId(secretName)
                    .build()
            );

            final var jsonNode = objectMapper.readTree(aRequest.secretString());
            return jsonNode.get(secretName).asText();
        } catch (SecretsManagerException e) {
            throw new SecretManagerException(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

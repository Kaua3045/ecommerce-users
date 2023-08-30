package com.kaua.ecommerce.users.infrastructure.account;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.application.gateways.AvatarGateway;
import com.kaua.ecommerce.users.domain.utils.Resource;
import com.kaua.ecommerce.users.infrastructure.accounts.AccountAvatarS3Gateway;
import com.kaua.ecommerce.users.infrastructure.services.StorageService;
import com.kaua.ecommerce.users.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;

@IntegrationTest
public class AccountAvatarS3GatewayTest {

    @Autowired
    private AvatarGateway avatarGateway;

    @Autowired
    private StorageService storageService;

    @BeforeEach
    void cleanUp() {
        storageService().clear();
    }

    @Test
    void testInjection() {
        Assertions.assertNotNull(avatarGateway);
        Assertions.assertInstanceOf(AccountAvatarS3Gateway.class, avatarGateway);

        Assertions.assertNotNull(storageService);
        Assertions.assertInstanceOf(InMemoryStorageService.class, storageService);
    }

    @Test
    void givenAValidIdAndResource_whenCallSaveAvatar_shouldReturnAvatarUrl() {
        final var aId = "123";
        final var aResource = Resource.with(
                InputStream.nullInputStream(),
                "image/jpg",
                "teste"
        );
        final var expectedUrl = "http://localhost:8080/files/123-teste.jpg";

        final var actualAvatar = this.avatarGateway.save(aId, aResource);

        Assertions.assertNotNull(actualAvatar);
        Assertions.assertEquals(expectedUrl, actualAvatar);
    }

    @Test
    void givenAValidId_whenCallDeleteAvatar_shouldDoesNotThrow() {
        final var aId = "123";
        final var aResource = Resource.with(
                InputStream.nullInputStream(),
                "image/jpg",
                "teste"
        );
        this.avatarGateway.save(aId, aResource);

        Assertions.assertDoesNotThrow(() -> this.avatarGateway.delete(aId));
    }

    private InMemoryStorageService storageService() {
        return (InMemoryStorageService) this.storageService;
    }
}

package com.kaua.ecommerce.users.infrastructure.accounts;

import com.kaua.ecommerce.users.application.gateways.AvatarGateway;
import com.kaua.ecommerce.users.domain.utils.Resource;
import com.kaua.ecommerce.users.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AccountAvatarS3Gateway implements AvatarGateway {

    private final StorageService storageService;

    public AccountAvatarS3Gateway(final StorageService storageService) {
        this.storageService = Objects.requireNonNull(storageService);
    }

    @Override
    public String save(String aAccountId, Resource aResource) {
        return this.storageService.uploadFile(aAccountId, aResource);
    }

    @Override
    public void delete(String accountId) {
        this.storageService.deleteFileByPrefix(accountId);
    }
}

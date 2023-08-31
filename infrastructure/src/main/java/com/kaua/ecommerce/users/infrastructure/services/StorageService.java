package com.kaua.ecommerce.users.infrastructure.services;

import com.kaua.ecommerce.users.domain.utils.Resource;

import java.util.Optional;

public interface StorageService {

    String uploadFile(String key, Resource resource);

    void deleteFile(String key);

    Optional<String> getFileUrl(String key);

    void deleteFileByPrefix(String prefix);
}

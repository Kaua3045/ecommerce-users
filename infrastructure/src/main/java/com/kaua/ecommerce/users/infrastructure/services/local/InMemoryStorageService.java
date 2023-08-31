package com.kaua.ecommerce.users.infrastructure.services.local;

import com.kaua.ecommerce.users.domain.utils.Resource;
import com.kaua.ecommerce.users.infrastructure.services.StorageService;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorageService implements StorageService {

    private final Map<String, String> storage;

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }

    public void clear() {
        this.storage.clear();
    }

    @Override
    public String uploadFile(String key, Resource resource) {
        final var KEY_WITH_FILENAME = key + "-" + resource.fileName();
        final var url = "http://localhost:8080/files/" +
                KEY_WITH_FILENAME +
                "." +
                resource.contentType().substring(6);
        this.storage.put(KEY_WITH_FILENAME, url);
        return url;
    }

    @Override
    public void deleteFile(String key) {
        this.storage.keySet().removeIf(key::equals);
    }

    @Override
    public Optional<String> getFileUrl(String key) {
        return Optional.ofNullable(this.storage.get(key));
    }

    @Override
    public void deleteFileByPrefix(String prefix) {
        this.storage.keySet().removeIf(key -> key.startsWith(prefix));
    }
}

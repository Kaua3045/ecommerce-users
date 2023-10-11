package com.kaua.ecommerce.users.application.gateways;

import java.util.Optional;

public interface CacheGateway<T> {

    void save(T value);
    Optional<T> get(String key);
    void delete(String key);
}

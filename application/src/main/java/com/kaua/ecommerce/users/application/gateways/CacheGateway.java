package com.kaua.ecommerce.users.application.gateways;

public interface CacheGateway<T> {

    void save(String key, T value);
    T get(String key);
    void delete(String key);
}

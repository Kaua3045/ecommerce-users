package com.kaua.ecommerce.users.config;

import redis.embedded.RedisServer;

import java.util.random.RandomGenerator;

public abstract class CacheTestConfiguration {

    private static RedisServer redisServer;

    public void init() {
//        final var port = RandomGenerator.getDefault().nextInt(1000, 9999);
        redisServer = new RedisServer(7070);
        redisServer.start();
    }

    public void stop() {
        redisServer.stop();
    }
}

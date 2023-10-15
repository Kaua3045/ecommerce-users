package com.kaua.ecommerce.users.config;

import redis.embedded.RedisServer;

import java.util.concurrent.ThreadLocalRandom;

public abstract class CacheTestConfiguration {

    private static RedisServer redisServer;

    public void init() {
        redisServer = new RedisServer(ThreadLocalRandom.current().nextInt(1000, 9999));
        redisServer.start();
    }

    public void stop() {
        redisServer.stop();
    }
}

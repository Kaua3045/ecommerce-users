package com.kaua.ecommerce.users.infrastructure.configurations.properties.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class CacheProperties implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(CacheProperties.class);

    private String host;
    private int port;

    public CacheProperties() {}

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug(toString());
    }

    @Override
    public String toString() {
        return "CacheProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

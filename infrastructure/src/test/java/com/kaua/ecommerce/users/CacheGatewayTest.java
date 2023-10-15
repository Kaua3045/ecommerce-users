package com.kaua.ecommerce.users;

import com.kaua.ecommerce.users.config.AmqpTestConfiguration;
import com.kaua.ecommerce.users.config.CacheCleanUpExtension;
import com.kaua.ecommerce.users.config.JpaCleanUpExtension;
import com.kaua.ecommerce.users.infrastructure.Main;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-cache-integration")
@SpringBootTest(classes = { Main.class, AmqpTestConfiguration.class })
@ExtendWith({ CacheCleanUpExtension.class, JpaCleanUpExtension.class })
@AutoConfigureDataRedis
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public @interface CacheGatewayTest {
}

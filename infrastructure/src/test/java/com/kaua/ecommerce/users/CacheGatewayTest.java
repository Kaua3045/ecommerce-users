package com.kaua.ecommerce.users;

import com.kaua.ecommerce.users.config.AmqpTestConfiguration;
import com.kaua.ecommerce.users.config.CacheCleanUpExtension;
import com.kaua.ecommerce.users.config.JpaCleanUpExtension;
import com.kaua.ecommerce.users.infrastructure.Main;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-cache-integration")
@SpringBootTest(classes = { Main.class, AmqpTestConfiguration.class })
@ExtendWith({ CacheCleanUpExtension.class, JpaCleanUpExtension.class })
@Transactional
public @interface CacheGatewayTest {
}

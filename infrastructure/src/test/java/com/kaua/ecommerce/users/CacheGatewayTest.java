package com.kaua.ecommerce.users;

import com.kaua.ecommerce.users.infrastructure.Main;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@SpringBootTest(classes = { Main.class, AmqpTestConfiguration.class, CacheTestConfiguration.class })
@ExtendWith(CleanUpExtension.class)
public @interface CacheGatewayTest {
}

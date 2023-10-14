package com.kaua.ecommerce.users;

import com.kaua.ecommerce.users.config.JpaCleanUpExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@DataJpaTest
@ComponentScan(
        basePackages = "com.kaua.ecommerce.users",
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*MySQLGateway")}
)
@ExtendWith(JpaCleanUpExtension.class)
public @interface MySQLGatewayTest {
}

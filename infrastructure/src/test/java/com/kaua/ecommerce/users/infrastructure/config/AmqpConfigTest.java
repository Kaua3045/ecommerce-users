package com.kaua.ecommerce.users.infrastructure.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test-integration")
public class AmqpConfigTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void testInjections() {
        Assertions.assertNotNull(rabbitTemplate);
    }
}

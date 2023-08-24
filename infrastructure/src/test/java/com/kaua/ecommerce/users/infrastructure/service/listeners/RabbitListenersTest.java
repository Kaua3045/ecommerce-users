package com.kaua.ecommerce.users.infrastructure.service.listeners;

import com.kaua.ecommerce.users.infrastructure.service.RabbitEventServiceTest;
import com.kaua.ecommerce.users.infrastructure.service.RabbitQueueServiceTest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RabbitListenersTest {

    @RabbitListener(id = RabbitEventServiceTest.LISTENER, queues = "${amqp.queues.account-created.routing-key}")
    public void onAccountCreated(@Payload final String message) {
        System.out.println(message);
    }

    @RabbitListener(id = RabbitQueueServiceTest.LISTENER, queues = "${amqp.queues.email-queue.routing-key}")
    public void onEmailConfirmationSend(@Payload final String message) {
        System.out.println(message);
    }
}

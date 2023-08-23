package com.kaua.ecommerce.users.infrastructure.service;

import com.kaua.ecommerce.users.infrastructure.services.impl.SnsEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

public class SnsEventServiceTest {

    @Test
    public void givenAValidEvent() {
        final var event = new DummyEvent("test");
        final var snsClientMock = Mockito.mock(SnsClient.class);
        final var accountTopicArn = "arn:mock:topic";

        Mockito.when(snsClientMock.publish(Mockito.any(PublishRequest.class)))
                .thenReturn(PublishResponse.builder()
                        .messageId("123")
                        .sequenceNumber("123")
                        .build());

        final var snsEventService = new SnsEventService("filterType", accountTopicArn, snsClientMock);

        Assertions.assertDoesNotThrow(() -> snsEventService.send(event));
    }

    private record DummyEvent(String name) {
    }
}

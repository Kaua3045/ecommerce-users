package com.kaua.ecommerce.users.infrastructure.service;

import com.kaua.ecommerce.users.IntegrationTest;
import com.kaua.ecommerce.users.infrastructure.services.impl.SnsEventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@IntegrationTest
@ActiveProfiles("test-service-integration")
public class SnsEventServiceTest {

    @Test
    public void givenAValidEvent() {
        final var event = new DummyEvent("test");
        SnsClient snsClientMock = Mockito.mock(SnsClient.class);

        Mockito.when(snsClientMock.publish(PublishRequest.builder().topicArn(null).build()))
                .thenReturn(PublishResponse.builder().build());

        final var snsEventService = new SnsEventService("filterType");

        Assertions.assertDoesNotThrow(() -> snsEventService.send(event));
    }

    private record DummyEvent(String name) {
    }
}

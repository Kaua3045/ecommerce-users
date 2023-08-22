package com.kaua.ecommerce.users.infrastructure.services.impl;

import com.kaua.ecommerce.users.infrastructure.configurations.json.Json;
import com.kaua.ecommerce.users.infrastructure.exceptions.SendEventException;
import com.kaua.ecommerce.users.infrastructure.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.SnsException;

import java.util.HashMap;

public class SnsEventService implements EventService {

    private static final Logger log = LoggerFactory.getLogger(SnsEventService.class);
    private static final String TOPIC_ARN = "arn:aws:sns:sa-east-1:727910200489:account.fifo";
    private final String filterType;

    public SnsEventService(final String aFilterType) {
        this.filterType = aFilterType;
    }

    @Override
    public void send(Object event) {
        try(final var snsClient = SnsClient.create()) {
            final var aAttirbutes = new HashMap<String, MessageAttributeValue>();
            aAttirbutes.put("type", MessageAttributeValue.builder()
                    .dataType("String")
                    .stringValue(filterType)
                    .build());

            final var aResponse = snsClient.publish(builder -> builder
                    .messageAttributes(aAttirbutes)
                    .messageGroupId("account")
                    .message(Json.writeValueAsString(event))
                    .topicArn(TOPIC_ARN)
            );

            if (!aResponse.sdkHttpResponse().isSuccessful()) {
                log.error("Error sending event to SNS: {}", aResponse.sdkHttpResponse().statusText());
            }
        } catch (SnsException e) {
            throw new SendEventException(e.getMessage());
        }
    }
}

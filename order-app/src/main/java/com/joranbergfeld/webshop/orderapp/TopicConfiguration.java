package com.joranbergfeld.webshop.orderapp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "order-app.events")
@ConstructorBinding
public record TopicConfiguration(String orderSubmittedTopic, String paymentUpdatedTopic, String stockUpdatedTopic,
                                 String itemCreatedTopic, String itemUpdatedTopic, String itemDeletedTopic) {
}

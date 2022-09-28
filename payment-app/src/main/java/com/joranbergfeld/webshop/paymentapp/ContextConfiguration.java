package com.joranbergfeld.webshop.paymentapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class ContextConfiguration {

    @Value("${payment-app.events.payment-updated-topic}")
    private String paymentUpdatedTopicName;

    @Bean
    @DependsOn("paymentUpdatedTopic")
    public OrderUpdateConsumer orderUpdateConsumer(PaymentRepository repository, KafkaTemplate<String, Object> template) {
        return new OrderUpdateConsumer(repository, template);
    }
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    StringJsonMessageConverter stringJsonMessageConverter() {
        return new StringJsonMessageConverter();
    }

    @Bean
    NewTopic paymentUpdatedTopic() {
        return new NewTopic(paymentUpdatedTopicName, 1, (short) 1);
    }
}

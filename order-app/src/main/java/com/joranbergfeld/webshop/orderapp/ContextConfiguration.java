package com.joranbergfeld.webshop.orderapp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.joranbergfeld.webshop.orderapp.order.OrderRepository;
import com.joranbergfeld.webshop.orderapp.order.OrderStateManager;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class ContextConfiguration {

    private final TopicConfiguration topicConfiguration;

    public ContextConfiguration(TopicConfiguration topicConfiguration) {
        this.topicConfiguration = topicConfiguration;
    }

    @Bean
    NewTopic orderSubmittedTopic() {
        return new NewTopic(topicConfiguration.orderSubmittedTopic(), 1, (short) 1);
    }

    @Bean
    NewTopic itemCreatedTopic() {
        return new NewTopic(topicConfiguration.itemCreatedTopic(), 1, (short) 1);
    }

    @Bean
    NewTopic itemUpdatedTopic() {
        return new NewTopic(topicConfiguration.itemUpdatedTopic(), 1, (short) 1);
    }

    @Bean
    NewTopic itemDeletedTopic() {
        return new NewTopic(topicConfiguration.itemDeletedTopic(), 1, (short) 1);
    }

    @Bean
    OrderStateManager orderStateManager(OrderRepository repository) {
        return new OrderStateManager(repository);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    StringJsonMessageConverter stringJsonMessageConverter() {
        return new StringJsonMessageConverter();
    }
}

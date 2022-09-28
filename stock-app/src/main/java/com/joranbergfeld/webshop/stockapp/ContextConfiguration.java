package com.joranbergfeld.webshop.stockapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joranbergfeld.webshop.stockapp.item.ItemManager;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class ContextConfiguration {

    @Value("${stock-app.events.stock-updated-topic}")
    private String stockUpdatedTopicName;

    @Bean
    public ItemManager itemManager(StockRepository stockRepository) {
        return new ItemManager(stockRepository);
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
    @DependsOn("stockUpdatedTopic")
    public OrderUpdateConsumer orderUpdateConsumer(KafkaTemplate<String, Object> template, StockRepository repository) {
        return new OrderUpdateConsumer(template, repository);
    }

    @Bean
    NewTopic stockUpdatedTopic() {
        return new NewTopic(stockUpdatedTopicName, 1, (short) 1);
    }
}

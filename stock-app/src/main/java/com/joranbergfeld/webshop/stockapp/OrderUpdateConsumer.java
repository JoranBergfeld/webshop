package com.joranbergfeld.webshop.stockapp;

import com.joranbergfeld.webshop.stockapp.event.OrderSubmittedEvent;
import com.joranbergfeld.webshop.stockapp.event.StockUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

public class OrderUpdateConsumer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final StockRepository repository;
    private final Logger log = LoggerFactory.getLogger(OrderUpdateConsumer.class);

    @Value("${stock-app.events.stock-updated-topic}")
    private String stockUpdatedTopicName;

    public OrderUpdateConsumer(KafkaTemplate<String, Object> kafkaTemplate, StockRepository repository) {
        this.kafkaTemplate = kafkaTemplate;
        this.repository = repository;
    }

    @KafkaListener(topics = "${stock-app.events.order-submitted-topic}")
    public void orderSubmittedEventFired(OrderSubmittedEvent event) {
        int amountFromOrder = event.amount();
        final String itemId = event.itemId();
        final String orderId = event.orderId();

        StockEntity item = repository.findByItemId(itemId).orElseThrow(RuntimeException::new);
        int currentAmount = getCurrentAmount(item);
        if (currentAmount - amountFromOrder < 0) {
            publishStockUpdateFailed(itemId, orderId, currentAmount, amountFromOrder);
        } else {
            updateStock(amountFromOrder, item);
            publishStockUpdateSuccessful(itemId, orderId);
        }

    }

    private void updateStock(int amountFromOrder, StockEntity item) {
        item.setAmount(item.getAmount() - amountFromOrder);
        repository.save(item);
    }

    private int getCurrentAmount(StockEntity item) {
        return item.getAmount();
    }

    private void publishStockUpdateSuccessful(final String itemId, final String orderId) {
        StockUpdatedEvent event = new StockUpdatedEvent(orderId, itemId, true);
        kafkaTemplate.send(stockUpdatedTopicName, event);
    }
    private void publishStockUpdateFailed(final String itemId, final String orderId, int currentAmount, int amountFromOrder) {
        log.warn("Failed to update item with ID {} for order with ID {}. Current amount in stock is {}, and we could not fullfill an order with requested product of {}.", itemId, orderId, currentAmount, amountFromOrder);
        StockUpdatedEvent event = new StockUpdatedEvent(orderId, itemId, false);
        kafkaTemplate.send(stockUpdatedTopicName, event);
    }
}

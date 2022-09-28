package com.joranbergfeld.webshop.orderapp.order;

import com.joranbergfeld.webshop.orderapp.order.event.PaymentUpdatedEvent;
import com.joranbergfeld.webshop.orderapp.order.event.StockUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Optional;

public class OrderStateManager {

    private final OrderRepository repository;
    private final Logger log = LoggerFactory.getLogger(OrderStateManager.class);

    public OrderStateManager(OrderRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${order-app.events.payment-updated-topic}")
    void listenForPaymentUpdates(PaymentUpdatedEvent event) {
        final String orderId = event.orderId();
        log.debug("Received payment update about order {}. It indicates processing has successfully processed: {}", orderId, event.successful());
        OrderEntity entity = getOrderEntity(orderId);
        if (entity == null) return;

        if (event.successful()) {
            entity.setPaymentState(PaymentState.PAID);
            if (entity.getStockState() == StockState.FULFILLED) {
                entity.setState(OrderState.COMPLETED);
            }
        } else {
            entity.setPaymentState(PaymentState.FAILED);
            entity.setState(OrderState.FAILED);
        }
        repository.save(entity);
    }

    @KafkaListener(topics = "${order-app.events.stock-updated-topic}")
    void listenForStockUpdates(StockUpdatedEvent event) {
        final String orderId = event.orderId();
        log.debug("Received stock update about order {}. It indicates processing has successfully processed: {}", orderId, event.successful());
        OrderEntity entity = getOrderEntity(orderId);
        if (entity == null) return;

        if (event.successful()) {
            entity.setStockState(StockState.FULFILLED);
            if (entity.getPaymentState() == PaymentState.PAID) {
                entity.setState(OrderState.COMPLETED);
            }
        } else {
            entity.setStockState(StockState.FAILED);
            entity.setState(OrderState.FAILED);
        }
        repository.save(entity);

    }

    private OrderEntity getOrderEntity(String orderId) {
        Optional<OrderEntity> orderEntityById = repository.findById(orderId);
        if (orderEntityById.isEmpty()) {
            log.warn("Received event for order with id {}, but was unable to find it.", orderId);
            return null;
        }
        return orderEntityById.get();
    }
}

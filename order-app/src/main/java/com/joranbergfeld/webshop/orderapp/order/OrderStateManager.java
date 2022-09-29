package com.joranbergfeld.webshop.orderapp.order;

import com.joranbergfeld.webshop.orderapp.eventstore.EventKeyEntity;
import com.joranbergfeld.webshop.orderapp.eventstore.EventKeyRepository;
import com.joranbergfeld.webshop.orderapp.order.event.PaymentUpdatedEvent;
import com.joranbergfeld.webshop.orderapp.order.event.StockUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Optional;

public class OrderStateManager {

    private final OrderRepository repository;
    private final Logger log = LoggerFactory.getLogger(OrderStateManager.class);
    private final EventKeyRepository eventKeyRepository;

    public OrderStateManager(OrderRepository repository, EventKeyRepository eventKeyRepository) {
        this.repository = repository;
        this.eventKeyRepository = eventKeyRepository;
    }

    @KafkaListener(topics = "${order-app.events.payment-updated-topic}")
    void listenForPaymentUpdates(@Payload PaymentUpdatedEvent event, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        // check for key duplication
        if (eventKeyRepository.findByEventKey(key).isPresent()) {
            log.warn("Duplicate received! Not processing.");
            return;
        } else {
            log.info("Duplicate check finalized. Did not find the key before, proceeding..");
            eventKeyRepository.save(new EventKeyEntity(key));
        }

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

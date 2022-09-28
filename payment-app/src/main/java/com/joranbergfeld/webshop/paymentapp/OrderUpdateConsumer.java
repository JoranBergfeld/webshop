package com.joranbergfeld.webshop.paymentapp;

import com.joranbergfeld.webshop.paymentapp.event.OrderSubmittedEvent;
import com.joranbergfeld.webshop.paymentapp.event.PaymentUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Random;

public class OrderUpdateConsumer {

    private final Logger log = LoggerFactory.getLogger(OrderUpdateConsumer.class);
    private final PaymentRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${payment-app.events.payment-updated-topic}")
    private String topicName;

    public OrderUpdateConsumer(PaymentRepository repository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${payment-app.events.order-submitted-topic}")
    public void orderSubmittedEventFired(OrderSubmittedEvent event) throws InterruptedException {
        final String orderId = event.orderId();
        int sleepMultiplier = generateRandomNumberAndSleep();
        persistPayment(orderId);
        publishPaymentUpdatedEvent(orderId, sleepMultiplier);
    }

    private void persistPayment(String orderId) {
        PaymentEntity record = generateRecord(orderId);
        repository.save(record);
    }

    private PaymentEntity generateRecord(String orderId) {
        PaymentEntity record = new PaymentEntity();
        record.setOrderId(orderId);
        return record;
    }

    private int generateRandomNumberAndSleep() throws InterruptedException {
        int sleepMultiplier = new Random().nextInt(10) + 1;
        Thread.sleep(1000 * sleepMultiplier);
        return sleepMultiplier;
    }

    private void publishPaymentUpdatedEvent(final String orderId, int sleepMultiplier) {
        boolean successful = sleepMultiplier < 9;
        PaymentUpdatedEvent event = new PaymentUpdatedEvent(orderId, successful);
        kafkaTemplate.send(topicName, event);
    }
}

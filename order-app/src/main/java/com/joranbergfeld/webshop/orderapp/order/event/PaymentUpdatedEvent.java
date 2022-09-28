package com.joranbergfeld.webshop.orderapp.order.event;

public record PaymentUpdatedEvent(String orderId, boolean successful) {
}

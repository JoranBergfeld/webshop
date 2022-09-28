package com.joranbergfeld.webshop.paymentapp.event;

public record PaymentUpdatedEvent(String orderId, boolean successful) {
}

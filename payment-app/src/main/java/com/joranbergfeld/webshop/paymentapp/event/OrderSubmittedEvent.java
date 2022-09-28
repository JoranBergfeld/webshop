package com.joranbergfeld.webshop.paymentapp.event;

public record OrderSubmittedEvent(String orderId, String itemId, int amount) {
}

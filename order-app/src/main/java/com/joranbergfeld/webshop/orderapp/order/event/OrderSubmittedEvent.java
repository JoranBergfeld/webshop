package com.joranbergfeld.webshop.orderapp.order.event;

public record OrderSubmittedEvent(String orderId, String itemId, int amount) {
}

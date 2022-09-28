package com.joranbergfeld.webshop.stockapp.event;

public record OrderSubmittedEvent(String orderId, String itemId, int amount) {
}

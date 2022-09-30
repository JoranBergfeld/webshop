package com.joranbergfeld.webshop.stockapp.event;

public record OrderFailedEvent(String orderId, String itemId, int amount) {
}

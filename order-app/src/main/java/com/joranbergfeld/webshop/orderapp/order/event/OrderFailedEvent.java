package com.joranbergfeld.webshop.orderapp.order.event;

public record OrderFailedEvent(String orderId, String itemId, int amount) {
}

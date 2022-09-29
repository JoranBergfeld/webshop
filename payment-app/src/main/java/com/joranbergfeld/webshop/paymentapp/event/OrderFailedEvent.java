package com.joranbergfeld.webshop.paymentapp.event;

public record OrderFailedEvent(String orderId, String itemId, int amount) {
}

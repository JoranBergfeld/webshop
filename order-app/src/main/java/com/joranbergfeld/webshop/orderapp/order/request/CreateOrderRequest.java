package com.joranbergfeld.webshop.orderapp.order.request;

public record CreateOrderRequest(String by, String itemId, int amount) {
}

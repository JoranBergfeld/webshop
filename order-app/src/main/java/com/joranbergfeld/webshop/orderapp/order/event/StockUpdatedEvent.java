package com.joranbergfeld.webshop.orderapp.order.event;

public record StockUpdatedEvent(String orderId, String itemId, boolean successful) {

}

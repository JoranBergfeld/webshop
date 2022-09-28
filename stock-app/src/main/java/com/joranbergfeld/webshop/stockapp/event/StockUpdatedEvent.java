package com.joranbergfeld.webshop.stockapp.event;

public record StockUpdatedEvent(String orderId, String itemId, boolean successful) {

}

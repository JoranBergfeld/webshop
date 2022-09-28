package com.joranbergfeld.webshop.stockapp.item.event;

public record ItemCreatedEvent(String itemId, int initialAmount) {
}

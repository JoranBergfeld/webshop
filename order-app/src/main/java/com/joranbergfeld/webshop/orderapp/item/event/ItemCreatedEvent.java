package com.joranbergfeld.webshop.orderapp.item.event;

public record ItemCreatedEvent(String itemId, int initialAmount) {
}

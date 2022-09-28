package com.joranbergfeld.webshop.orderapp.item.event;

public record ItemUpdatedEvent(String itemId, int updatedAmount) {
}

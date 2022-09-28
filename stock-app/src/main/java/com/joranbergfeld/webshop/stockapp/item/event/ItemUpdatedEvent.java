package com.joranbergfeld.webshop.stockapp.item.event;

public record ItemUpdatedEvent(String itemId, int updatedAmount) {
}

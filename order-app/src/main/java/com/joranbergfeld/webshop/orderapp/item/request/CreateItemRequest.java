package com.joranbergfeld.webshop.orderapp.item.request;

public record CreateItemRequest(String name, String url, int amount) {
}

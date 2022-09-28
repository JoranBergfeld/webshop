package com.joranbergfeld.webshop.orderapp.order;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity(name = "order_entity")
public class OrderEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String orderId;
    private String orderedBy;
    private String itemId;
    private int amount;
    @Enumerated(EnumType.ORDINAL)
    private OrderState state;
    @Enumerated(EnumType.ORDINAL)
    private PaymentState paymentState;
    @Enumerated(EnumType.ORDINAL)
    private StockState stockState;

    public OrderEntity() {
    }

    public OrderEntity(String orderedBy, String itemId, int amount, OrderState state, PaymentState paymentState, StockState stockState) {
        this(null, orderedBy, itemId, amount, state, paymentState, stockState);
    }

    public OrderEntity(String orderId, String orderedBy, String itemId, int amount, OrderState state, PaymentState paymentState, StockState stockState) {
        this.orderId = orderId;
        this.orderedBy = orderedBy;
        this.itemId = itemId;
        this.amount = amount;
        this.state = state;
        this.paymentState = paymentState;
        this.stockState = stockState;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String id) {
        this.orderId = id;
    }

    public String getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(String by) {
        this.orderedBy = by;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public PaymentState getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    public StockState getStockState() {
        return stockState;
    }

    public void setStockState(StockState stockState) {
        this.stockState = stockState;
    }
}

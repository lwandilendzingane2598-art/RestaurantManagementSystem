package com.restaurant.models;

public class OrderItem {
    private int id;
    private int orderId;
    private int itemId;
    private String itemName;
    private int quantity;
    private double unitPrice;

    public OrderItem() {}

    public OrderItem(int id, int orderId, int itemId, String itemName, int quantity, double unitPrice) {
        this.id = id;
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
}

package com.restaurant.models;

import java.sql.Timestamp;

public class OrderHistoryDTO {
    private int orderId;
    private Timestamp orderDate;
    private String customerName;
    private String tableNumber;
    private double totalAmount;
    private String status;

    public OrderHistoryDTO() {}

    public OrderHistoryDTO(int orderId, Timestamp orderDate, String customerName, String tableNumber, double totalAmount, String status) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.tableNumber = tableNumber;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

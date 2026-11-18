package com.restaurant.models;

import java.sql.Timestamp;

public class Order {
    private int id;
    private Timestamp orderDate;
    private int employeeId;
    private int tableId;
    private int customerId;
    private double totalAmount;
    private String status; // pending, paid, cancelled

    public Order() {}

    public Order(int id, Timestamp orderDate, int employeeId, int tableId, int customerId, double totalAmount, String status) {
        this.id = id;
        this.orderDate = orderDate;
        this.employeeId = employeeId;
        this.tableId = tableId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

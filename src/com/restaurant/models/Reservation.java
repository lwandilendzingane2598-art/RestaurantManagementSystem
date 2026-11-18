package com.restaurant.models;

import java.sql.Date;
import java.sql.Time;

public class Reservation {
    private int id;
    private int customerId;
    private String customerName;  // for display
    private int tableId;
    private String tableNumber;   // for display
    private Date reservationDate;
    private Time reservationTime;
    private int guests;
    private String status; // pending, confirmed, cancelled

    public Reservation() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
    public Date getReservationDate() { return reservationDate; }
    public void setReservationDate(Date reservationDate) { this.reservationDate = reservationDate; }
    public Time getReservationTime() { return reservationTime; }
    public void setReservationTime(Time reservationTime) { this.reservationTime = reservationTime; }
    public int getGuests() { return guests; }
    public void setGuests(int guests) { this.guests = guests; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

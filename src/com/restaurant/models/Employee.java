package com.restaurant.models;

public class Employee {
    private int id;
    private String fullName;
    private String email;
    private String passwordHash;
    private String role;
    private double salary;
    private String hireDate;

    public Employee() {}
    
    public Employee(int id, String fullName, String email, String role){
         this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }
    
    public Employee(int id, String fullName, String email, String passwordHash, String role, double salary, String hireDate) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getHireDate() { return hireDate; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }
}


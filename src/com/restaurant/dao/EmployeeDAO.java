package com.restaurant.dao;

import com.restaurant.models.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Use your actual table name – adjust if needed
    private static final String TABLE_NAME = "employees_902012598_902012661_902011766";

    // Create employee (INSERT)
    public void addEmployee(Employee emp) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (full_name, email, password_hash, role, salary, hire_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getFullName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getPasswordHash());
            ps.setString(4, emp.getRole());
            ps.setDouble(5, emp.getSalary());
            ps.setString(6, emp.getHireDate());
            ps.executeUpdate();
        }
    }

    // Read all employees
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT emp_id, full_name, email, role, salary, hire_date FROM " + TABLE_NAME;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Employee emp = new Employee(); // using no-arg constructor
                emp.setId(rs.getInt("emp_id"));
                emp.setFullName(rs.getString("full_name"));
                emp.setEmail(rs.getString("email"));
                emp.setRole(rs.getString("role"));
                emp.setSalary(rs.getDouble("salary"));
                emp.setHireDate(rs.getString("hire_date"));
                // passwordHash is not loaded here for security
                list.add(emp);
            }
        }
        return list;
    }

    // Read single employee by ID (optional for updates)
    public Employee getEmployeeById(int id) throws SQLException {
        String sql = "SELECT emp_id, full_name, email, role, salary, hire_date FROM " + TABLE_NAME + " WHERE emp_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("emp_id"));
                    emp.setFullName(rs.getString("full_name"));
                    emp.setEmail(rs.getString("email"));
                    emp.setRole(rs.getString("role"));
                    emp.setSalary(rs.getDouble("salary"));
                    emp.setHireDate(rs.getString("hire_date"));
                    return emp;
                }
            }
        }
        return null;
    }

    // Update employee (excluding password by default)
    public void updateEmployee(Employee emp) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET full_name=?, email=?, role=?, salary=?, hire_date=? WHERE emp_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getFullName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getRole());
            ps.setDouble(4, emp.getSalary());
            ps.setString(5, emp.getHireDate());
            ps.setInt(6, emp.getId());
            ps.executeUpdate();
        }
    }

    // Update password separately
    public void updatePassword(int empId, String newPasswordHash) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET password_hash=? WHERE emp_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPasswordHash);
            ps.setInt(2, empId);
            ps.executeUpdate();
        }
    }

    // Delete employee
    public void deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE emp_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Authenticate login (returns Employee with only id, fullName, email, role)
    public Employee login(String email, String passwordPlain) throws SQLException {
        // Assuming password is stored as plain text (but you should hash it)
        String sql = "SELECT emp_id, full_name, email, role FROM " + TABLE_NAME + " WHERE email=? AND password_hash=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, passwordPlain); // change to hash comparison later
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Use the 4-parameter constructor we added to Employee
                    return new Employee(
                        rs.getInt("emp_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("role")
                    );
                }
            }
        }
        return null;
    }
}

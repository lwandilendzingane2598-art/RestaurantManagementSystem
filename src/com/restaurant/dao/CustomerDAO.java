package com.restaurant.dao;

import com.restaurant.models.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final String TABLE = "customers_902012598_902012661_902011766";

    public boolean add(Customer cust) {
        String sql = "INSERT INTO " + TABLE + " (cust_name, phone, email, loyalty_points) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cust.getName());
            ps.setString(2, cust.getPhone());
            ps.setString(3, cust.getEmail());
            ps.setInt(4, cust.getLoyaltyPoints());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Customer cust) {
        String sql = "UPDATE " + TABLE + " SET cust_name=?, phone=?, email=?, loyalty_points=? WHERE cust_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cust.getName());
            ps.setString(2, cust.getPhone());
            ps.setString(3, cust.getEmail());
            ps.setInt(4, cust.getLoyaltyPoints());
            ps.setInt(5, cust.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE cust_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT cust_id, cust_name, phone, email, loyalty_points FROM " + TABLE;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer cust = new Customer();
                cust.setId(rs.getInt("cust_id"));
                cust.setName(rs.getString("cust_name"));
                cust.setPhone(rs.getString("phone"));
                cust.setEmail(rs.getString("email"));
                cust.setLoyaltyPoints(rs.getInt("loyalty_points"));
                list.add(cust);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Customer findById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE cust_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Customer cust = new Customer();
                cust.setId(rs.getInt("cust_id"));
                cust.setName(rs.getString("cust_name"));
                cust.setPhone(rs.getString("phone"));
                cust.setEmail(rs.getString("email"));
                cust.setLoyaltyPoints(rs.getInt("loyalty_points"));
                return cust;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

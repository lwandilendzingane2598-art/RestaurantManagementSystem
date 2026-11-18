package com.restaurant.dao;

import com.restaurant.models.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private static final String TABLE = "suppliers_902012598_902012661_902011766";

    public boolean add(Supplier supplier) {
        String sql = "INSERT INTO " + TABLE + " (company_name, contact_person, phone, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, supplier.getCompanyName());
            ps.setString(2, supplier.getContactPerson());
            ps.setString(3, supplier.getPhone());
            ps.setString(4, supplier.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Supplier supplier) {
        String sql = "UPDATE " + TABLE + " SET company_name=?, contact_person=?, phone=?, email=? WHERE supplier_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, supplier.getCompanyName());
            ps.setString(2, supplier.getContactPerson());
            ps.setString(3, supplier.getPhone());
            ps.setString(4, supplier.getEmail());
            ps.setInt(5, supplier.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE supplier_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Supplier> findAll() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT supplier_id, company_name, contact_person, phone, email FROM " + TABLE;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Supplier s = new Supplier();
                s.setId(rs.getInt("supplier_id"));
                s.setCompanyName(rs.getString("company_name"));
                s.setContactPerson(rs.getString("contact_person"));
                s.setPhone(rs.getString("phone"));
                s.setEmail(rs.getString("email"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Supplier findById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE supplier_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Supplier s = new Supplier();
                s.setId(rs.getInt("supplier_id"));
                s.setCompanyName(rs.getString("company_name"));
                s.setContactPerson(rs.getString("contact_person"));
                s.setPhone(rs.getString("phone"));
                s.setEmail(rs.getString("email"));
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

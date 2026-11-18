package com.restaurant.dao;

import com.restaurant.models.DiningTable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {
    private static final String TABLE = "tables_902012598_902012661_902011766";

    public boolean add(DiningTable table) {
        String sql = "INSERT INTO " + TABLE + " (table_number, capacity, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, table.getTableNumber());
            ps.setInt(2, table.getCapacity());
            ps.setString(3, table.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(DiningTable table) {
        String sql = "UPDATE " + TABLE + " SET table_number=?, capacity=?, status=? WHERE table_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, table.getTableNumber());
            ps.setInt(2, table.getCapacity());
            ps.setString(3, table.getStatus());
            ps.setInt(4, table.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE table_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DiningTable> findAll() {
        List<DiningTable> list = new ArrayList<>();
        String sql = "SELECT table_id, table_number, capacity, status FROM " + TABLE;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DiningTable table = new DiningTable();
                table.setId(rs.getInt("table_id"));
                table.setTableNumber(rs.getString("table_number"));
                table.setCapacity(rs.getInt("capacity"));
                table.setStatus(rs.getString("status"));
                list.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public DiningTable findById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE table_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DiningTable table = new DiningTable();
                table.setId(rs.getInt("table_id"));
                table.setTableNumber(rs.getString("table_number"));
                table.setCapacity(rs.getInt("capacity"));
                table.setStatus(rs.getString("status"));
                return table;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

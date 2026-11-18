package com.restaurant.dao;

import com.restaurant.models.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {
    private static final String TABLE = "menu_items_902012598_902012661_902011766";
    private static final String CATEGORY_TABLE = "categories_902012598_902012661_902011766";

    public boolean add(MenuItem item) {
        String sql = "INSERT INTO " + TABLE + " (item_name, description, price, stock_quantity, cat_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            ps.setDouble(3, item.getPrice());
            ps.setInt(4, item.getStockQuantity());
            ps.setInt(5, item.getCategoryId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(MenuItem item) {
        String sql = "UPDATE " + TABLE + " SET item_name=?, description=?, price=?, stock_quantity=?, cat_id=? WHERE item_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getDescription());
            ps.setDouble(3, item.getPrice());
            ps.setInt(4, item.getStockQuantity());
            ps.setInt(5, item.getCategoryId());
            ps.setInt(6, item.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE item_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<MenuItem> findAll() {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT m.item_id, m.item_name, m.description, m.price, m.stock_quantity, m.cat_id, c.cat_name " +
                     "FROM " + TABLE + " m LEFT JOIN " + CATEGORY_TABLE + " c ON m.cat_id = c.cat_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MenuItem item = new MenuItem();
                item.setId(rs.getInt("item_id"));
                item.setName(rs.getString("item_name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getDouble("price"));
                item.setStockQuantity(rs.getInt("stock_quantity"));
                item.setCategoryId(rs.getInt("cat_id"));
                item.setCategoryName(rs.getString("cat_name"));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public MenuItem findById(int id) {
        String sql = "SELECT * FROM " + TABLE + "WHERE item_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                MenuItem item = new MenuItem();
                item.setId(rs.getInt("item_id"));
                item.setName(rs.getString("item_name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getDouble("price"));
                item.setStockQuantity(rs.getInt("stock_quantity"));
                item.setCategoryId(rs.getInt("cat_id"));
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

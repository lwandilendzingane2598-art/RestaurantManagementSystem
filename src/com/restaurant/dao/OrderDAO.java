package com.restaurant.dao;

import com.restaurant.models.Order;
import com.restaurant.models.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private static final String ORDER_TABLE = "orders_902012598_902012661_902011766";
    private static final String ORDER_ITEM_TABLE = "order_items_902012598_902012661_902011766";
    private static final String MENU_TABLE = "menu_items_902012598_902012661_902011766";
    private static final String CUSTOMER_TABLE = "customers_902012598_902012661_902011766";
    private static final String TABLE_TABLE = "tables_902012598_902012661_902011766";

    // Create a new order and return generated ID
    public int createOrder(Order order) {
        String sql = "INSERT INTO " + ORDER_TABLE + " (emp_id, table_id, cust_id, total_amount, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getEmployeeId());
            ps.setInt(2, order.getTableId());
            ps.setInt(3, order.getCustomerId());
            ps.setDouble(4, order.getTotalAmount());
            ps.setString(5, order.getStatus());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    // Add an order item (line item)
    public boolean addOrderItem(OrderItem item) {
        String sql = "INSERT INTO " + ORDER_ITEM_TABLE + " (order_id, item_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getItemId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getUnitPrice());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // Get all menu items (for POS selection)
    public List<OrderItem> getMenuItems() {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT item_id, item_name, price FROM " + MENU_TABLE + " WHERE stock_quantity > 0";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setItemId(rs.getInt("item_id"));
                item.setItemName(rs.getString("item_name"));
                item.setUnitPrice(rs.getDouble("price"));
                list.add(item);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Get available tables (status = ‘available’)
    public List<Integer> getAvailableTables() {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT table_id FROM " + TABLE_TABLE + " WHERE status = 'available'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(rs.getInt("table_id"));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Get customers (ID + name)
    public List<String> getCustomers() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT cust_id, cust_name FROM " + CUSTOMER_TABLE;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getInt("cust_id") + " – " + rs.getString("cust_name"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}

package com.restaurant.dao;

import com.restaurant.models.OrderHistoryDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryDAO {
    private static final String ORDER_TABLE = "orders_902012598_902012661_902011766";
    private static final String CUSTOMER_TABLE = "customers_902012598_902012661_902011766";
    private static final String TABLE_TABLE = "tables_902012598_902012661_902011766";
    private static final String ORDER_ITEM_TABLE = "order_items_902012598_902012661_902011766";
    private static final String MENU_TABLE = "menu_items_902012598_902012661_902011766";

    // Get all orders with customer and table names
    public List<OrderHistoryDTO> getAllOrders() {
        List<OrderHistoryDTO> list = new ArrayList<>();
        String sql = "SELECT o.order_id, o.order_date, c.cust_name, t.table_number, o.total_amount, o.status " +
                     "FROM " + ORDER_TABLE + " o " +
                     "LEFT JOIN " + CUSTOMER_TABLE + " c ON o.cust_id = c.cust_id " +
                     "LEFT JOIN " + TABLE_TABLE + " t ON o.table_id = t.table_id " +
                     "ORDER BY o.order_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                OrderHistoryDTO dto = new OrderHistoryDTO();
                dto.setOrderId(rs.getInt("order_id"));
                dto.setOrderDate(rs.getTimestamp("order_date"));
                dto.setCustomerName(rs.getString("cust_name"));
                dto.setTableNumber(rs.getString("table_number"));
                dto.setTotalAmount(rs.getDouble("total_amount"));
                dto.setStatus(rs.getString("status"));
                list.add(dto);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Get order items for a specific order
    public List<Object[]> getOrderItems(int orderId) {
        List<Object[]> items = new ArrayList<>();
        String sql = "SELECT mi.item_name, oi.quantity, oi.unit_price " +
                     "FROM " + ORDER_ITEM_TABLE + " oi " +
                     "JOIN " + MENU_TABLE + " mi ON oi.item_id = mi.item_id " +
                     "WHERE oi.order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new Object[]{
                    rs.getString("item_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return items;
    }
}

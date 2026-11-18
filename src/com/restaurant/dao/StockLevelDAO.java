package com.restaurant.dao;

import com.restaurant.models.StockItem;
import com.restaurant.frames.StockLevelFrame;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockLevelDAO {
    private static final String MENU_TABLE = "menu_items_902012598_902012661_902011766";
    private static final String CATEGORY_TABLE = "categories_902012598_902012661_902011766";

    // Get low stock items (stock < 10)
    public List<StockItem> getLowStockItems() {
        List<StockItem> list = new ArrayList<>();
        String sql = "SELECT m.item_id, m.item_name, m.stock_quantity, c.cat_name " +
                     "FROM " + MENU_TABLE + " m " +
                     "LEFT JOIN " + CATEGORY_TABLE + " c ON m.cat_id = c.cat_id " +
                     "WHERE m.stock_quantity < 10 ORDER BY m.stock_quantity ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StockItem item = new StockItem();
                item.setId(rs.getInt("item_id"));
                item.setName(rs.getString("item_name"));
                item.setQuantity(rs.getInt("stock_quantity"));
                item.setCategory(rs.getString("cat_name"));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Restock an item
    public boolean restockItem(int itemId, int additionalQuantity) {
        String sql = "UPDATE " + MENU_TABLE + " SET stock_quantity = stock_quantity + ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, additionalQuantity);
            ps.setInt(2, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

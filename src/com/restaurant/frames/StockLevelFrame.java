package com.restaurant.frames;

import com.restaurant.dao.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.sql.*;
import com.restaurant.dao.StockLevelDAO;
import com.restaurant.models.StockItem;
import java.util.List;
public class StockLevelFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtRestock;
    private JButton btnRestock, btnRefresh;

    public StockLevelFrame() {
        setTitle("Stock Level Management");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(248, 245, 240));
        setIconImage(loadLogo());
        initUI();
        loadData();
    }

    private Image loadLogo() {
        try {
            URL url = getClass().getResource("/images/logo.png");
            if (url != null) return new ImageIcon(url).getImage();
        } catch (Exception e) {}
        return null;
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Table: show items with stock < 10 (low stock)
        model = new DefaultTableModel(new String[]{"ID", "Item Name", "Current Stock", "Category"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(25, 55, 85));
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        // Bottom panel: restock
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottom.setBackground(new Color(248, 245, 240));
        bottom.add(new JLabel("Restock Quantity:"));
        txtRestock = new JTextField(10);
        bottom.add(txtRestock);
        btnRestock = createStyledButton("➕ Restock Selected");
        btnRefresh = createStyledButton("🔄 Refresh");
        bottom.add(btnRestock);
        bottom.add(btnRefresh);
        add(bottom, BorderLayout.SOUTH);

        btnRestock.addActionListener(e -> restockItem());
        btnRefresh.addActionListener(e -> loadData());

        table.getSelectionModel().addListSelectionListener(e -> {
            // nothing special, just enable restock button
        });
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(25, 55, 85));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadData() {
       model.setRowCount(0);
       StockLevelDAO dao = new StockLevelDAO();
     List<StockItem> items =  dao.getLowStockItems();
       System.out.println("Number of low stock items: " + items.size());
       for (StockItem item :items){
           model.addRow(new Object[]{
               item.getId(),
               item.getName(),
               item.getQuantity(),
               item.getCategory()
           });
       }
 
         
    }

    private void restockItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to restock", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String restockText = txtRestock.getText().trim();
        if (restockText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter restock quantity", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int qty;
        try {
            qty = Integer.parseInt(restockText);
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity");
            return;
        }
        int itemId = (int) model.getValueAt(row, 0);
        String sql = "UPDATE menu_items_902012598_902012661_902011766 SET stock_quantity = stock_quantity + ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, itemId);
            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Restocked successfully");
                loadData();
                txtRestock.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Restock failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


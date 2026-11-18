package com.restaurant.frames;

import com.restaurant.dao.MenuItemDAO;
import com.restaurant.dao.CategoryDAO;
import com.restaurant.models.MenuItem;
import com.restaurant.models.Category;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class MenuItemFrame extends JFrame {
    private MenuItemDAO dao = new MenuItemDAO();
    private CategoryDAO categoryDao = new CategoryDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtName, txtPrice, txtStock;
    private JTextArea txtDescription;
    private JComboBox<String> cmbCategory;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public MenuItemFrame() {
        setTitle("Manage Menu Items");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(248, 245, 240));
        setIconImage(loadLogo());
        initUI();
        loadData();
        loadCategories();
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

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Price", "Stock"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(25, 55, 85));
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        // Form panel (bottom)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(248, 245, 240));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        txtName = new JTextField(20);
        gbc.gridx = 1; gbc.gridwidth = 3;
        formPanel.add(txtName, gbc);

        // Row 1: Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        txtDescription = new JTextArea(3, 20);
        txtDescription.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        gbc.gridx = 1; gbc.gridwidth = 3;
        formPanel.add(descScroll, gbc);

        // Row 2: Price
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Price:"), gbc);
        txtPrice = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(txtPrice, gbc);

        // Row 2b: Stock
        gbc.gridx = 2;
        formPanel.add(new JLabel("Stock:"), gbc);
        txtStock = new JTextField(10);
        gbc.gridx = 3;
        formPanel.add(txtStock, gbc);

        // Row 3: Category
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Category:"), gbc);
        cmbCategory = new JComboBox<>();
        cmbCategory.setPreferredSize(new Dimension(150, 25));
        gbc.gridx = 1;
        formPanel.add(cmbCategory, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(248, 245, 240));
        btnAdd = createStyledButton("➕ Add");
        btnUpdate = createStyledButton("✏️ Update");
        btnDelete = createStyledButton("🗑️ Delete");
        btnRefresh = createStyledButton("🔄 Refresh");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);

        // Button actions
        btnAdd.addActionListener(e -> addMenuItem());
        btnUpdate.addActionListener(e -> updateMenuItem());
        btnDelete.addActionListener(e -> deleteMenuItem());
        btnRefresh.addActionListener(e -> { loadData(); loadCategories(); });

        // Table selection
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtName.setText(model.getValueAt(row, 1).toString());
                txtPrice.setText(model.getValueAt(row, 3).toString());
                txtStock.setText(model.getValueAt(row, 4).toString());
                String catName = model.getValueAt(row, 2).toString();
                cmbCategory.setSelectedItem(catName);
                // For description, we need to fetch from DB or store in model; for simplicity, we’ll load from DB again.
                int id = (int) model.getValueAt(row, 0);
                loadDescription(id);
            }
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

    private void loadCategories() {
        cmbCategory.removeAllItems();
        List<Category> list = categoryDao.findAll();
        for (Category c : list) {
            cmbCategory.addItem(c.getName());
        }
    }

    private int getCategoryIdByName(String name) {
        List<Category> list = categoryDao.findAll();
        for (Category c : list) {
            if (c.getName().equals(name)) return c.getId();
        }
        return -1;
    }

    private void loadDescription(int id) {
        MenuItem item = dao.findById(id);
        if (item != null && item.getDescription() != null) {
            txtDescription.setText(item.getDescription());
        } else {
            txtDescription.setText("");
        }
    }

    private void loadData() {
        model.setRowCount(0);
        List<MenuItem> list = dao.findAll();
        for (MenuItem item : list) {
            model.addRow(new Object[]{
                item.getId(),
                item.getName(),
                item.getCategoryName(),
                item.getPrice(),
                item.getStockQuantity()
            });
        }
    }

    private void addMenuItem() {
        try {
            String name = txtName.getText().trim();
            String desc = txtDescription.getText().trim();
            Double price = Double.parseDouble(txtPrice.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            String catName = (String) cmbCategory.getSelectedItem();
            int catId = getCategoryIdByName(catName);
            if (name.isEmpty() || catId == -1) {
                JOptionPane.showMessageDialog(this, "Fill all required fields");
                return;
            }
            MenuItem item = new MenuItem();
            item.setName(name);
            item.setDescription(desc);
            item.setPrice(price);
            item.setStockQuantity(stock);
            item.setCategoryId(catId);
            if (dao.add(item)) {
                JOptionPane.showMessageDialog(this, "Menu item added");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Add failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price or stock", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMenuItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update");
            return;
        }
        try {
            int id = (int) model.getValueAt(row, 0);
            String name = txtName.getText().trim();
            String desc = txtDescription.getText().trim();
            Double price = Double.parseDouble(txtPrice.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            String catName = (String) cmbCategory.getSelectedItem();
            int catId = getCategoryIdByName(catName);
            if (name.isEmpty() || catId == -1) return;
            MenuItem item = new MenuItem();
            item.setId(id);
            item.setName(name);
            item.setDescription(desc);
            item.setPrice(price);
            item.setStockQuantity(stock);
            item.setCategoryId(catId);
            if (dao.update(item)) {
                JOptionPane.showMessageDialog(this, "Updated");
                loadData();
                clearForm();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMenuItem() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this item?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(id)) {
                JOptionPane.showMessageDialog(this, "Deleted");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed (might be used in orders)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        if (cmbCategory.getItemCount() > 0) cmbCategory.setSelectedIndex(0);
        table.clearSelection();
    }
}

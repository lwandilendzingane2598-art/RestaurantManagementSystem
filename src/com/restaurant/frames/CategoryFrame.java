package com.restaurant.frames;

import com.restaurant.dao.CategoryDAO;
import com.restaurant.models.Category;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoryFrame extends JFrame {
    private CategoryDAO dao = new CategoryDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtName;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public CategoryFrame() {
        setTitle("Manage Categories");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID", "Category Name", "Image"}, 0) {
    @Override
    public boolean isCellEditable(int row, int col) { return false; }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 2) return ImageIcon.class;
        return super.getColumnClass(columnIndex);
    }
};

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(new JLabel("Category Name:"));
        txtName = new JTextField(15);
        bottom.add(txtName);
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");
        bottom.add(btnAdd);
        bottom.add(btnUpdate);
        bottom.add(btnDelete);
        bottom.add(btnRefresh);
        add(bottom, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addCategory());
        btnUpdate.addActionListener(e -> updateCategory());
        btnDelete.addActionListener(e -> deleteCategory());
        btnRefresh.addActionListener(e -> loadData());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtName.setText(model.getValueAt(row, 1).toString());
            }
        });
    }

   private void loadData() {
    model.setRowCount(0);
    List<Category> list = dao.findAll();
    for (Category c : list) {
        String catName = c.getName();
        ImageIcon icon = getCategoryIcon(catName);
        model.addRow(new Object[]{c.getId(), catName, icon});
    }
    table.setRowHeight(40);
    if (table.getColumnCount()> 2){
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
         table.getColumnModel().getColumn(2).setMaxWidth(120);
        
}
   }
private ImageIcon getCategoryIcon(String categoryName) {
    String filename = "";
    switch (categoryName.toLowerCase()) {
        case "appetizers": filename = "appetizers.png"; break;
        case "beverages": filename = "beverages.png"; break;
        case "desserts": filename = "desserts.png"; break;
        case "main course": filename = "main_course.png"; break;
        case "salads": filename = "salads.png"; break;
        case "side dishes": filename = "side_dishes.png"; break;
        default: return null;
    }
    try {
        java.net.URL imgURL = getClass().getResource("/images/" + filename);
        if (imgURL != null) {
            ImageIcon original = new ImageIcon(imgURL);
            // Scale to 32x32 pixels
            Image scaled = original.getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
    } catch (Exception e) {}
    return null;
}

    private void addCategory() {
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter category name");
            return;
        }
        Category c = new Category(0, name);
        if (dao.add(c)) {
            JOptionPane.showMessageDialog(this, "Added");
            loadData();
            txtName.setText("");
        }
    }

    private void updateCategory() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) model.getValueAt(row, 0);
        String name = txtName.getText().trim();
        if (name.isEmpty()) return;
        Category c = new Category(id, name);
        if (dao.update(c)) {
            JOptionPane.showMessageDialog(this, "Updated");
            loadData();
            txtName.setText("");
        }
    }

    private void deleteCategory() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(id)) {
                JOptionPane.showMessageDialog(this, "Deleted");
                loadData();
                txtName.setText("");
            }
        }
    }
}

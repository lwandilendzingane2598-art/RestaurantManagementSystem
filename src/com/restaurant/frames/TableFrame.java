package com.restaurant.frames;

import com.restaurant.dao.TableDAO;
import com.restaurant.models.DiningTable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class TableFrame extends JFrame {
    private TableDAO dao = new TableDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNumber, txtCapacity;
    private JComboBox<String> cmbStatus;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public TableFrame() {
        setTitle("Manage Tables");
        setSize(700, 500);
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

        // Table model
        model = new DefaultTableModel(new String[]{"ID", "Table Number", "Capacity", "Status"}, 0) {
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

        // Form panel (bottom)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(248, 245, 240));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Table Number
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Table Number:"), gbc);
        txtNumber = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(txtNumber, gbc);

        // Capacity
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Capacity:"), gbc);
        txtCapacity = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(txtCapacity, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Status:"), gbc);
        cmbStatus = new JComboBox<>(new String[]{"available", "occupied", "reserved"});
        cmbStatus.setPreferredSize(new Dimension(120, 25));
        gbc.gridx = 1;
        formPanel.add(cmbStatus, gbc);

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

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);

        // Actions
        btnAdd.addActionListener(e -> addTable());
        btnUpdate.addActionListener(e -> updateTable());
        btnDelete.addActionListener(e -> deleteTable());
        btnRefresh.addActionListener(e -> loadData());

        // Table selection
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtNumber.setText(model.getValueAt(row, 1).toString());
                txtCapacity.setText(model.getValueAt(row, 2).toString());
                cmbStatus.setSelectedItem(model.getValueAt(row, 3).toString());
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

    private void loadData() {
        model.setRowCount(0);
        List<DiningTable> list = dao.findAll();
        for (DiningTable t : list) {
            model.addRow(new Object[]{t.getId(), t.getTableNumber(), t.getCapacity(), t.getStatus()});
        }
    }

    private void addTable() {
        String number = txtNumber.getText().trim();
        String capText = txtCapacity.getText().trim();
        String status = (String) cmbStatus.getSelectedItem();
        if (number.isEmpty() || capText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields");
            return;
        }
        try {
            int capacity = Integer.parseInt(capText);
            DiningTable tableObj = new DiningTable();
            tableObj.setTableNumber(number);
            tableObj.setCapacity(capacity);
            tableObj.setStatus(status);
            if (dao.add(tableObj)) {
                JOptionPane.showMessageDialog(this, "Table added");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Add failed (maybe duplicate number)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        String number = txtNumber.getText().trim();
        String capText = txtCapacity.getText().trim();
        String status = (String) cmbStatus.getSelectedItem();
        if (number.isEmpty() || capText.isEmpty()) return;
        try {
            int capacity = Integer.parseInt(capText);
            DiningTable tableObj = new DiningTable();
            tableObj.setId(id);
            tableObj.setTableNumber(number);
            tableObj.setCapacity(capacity);
            tableObj.setStatus(status);
            if (dao.update(tableObj)) {
                JOptionPane.showMessageDialog(this, "Table updated");
                loadData();
                clearForm();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid capacity", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTable() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this table?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(id)) {
                JOptionPane.showMessageDialog(this, "Table deleted");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed (might be linked to orders/reservations)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtNumber.setText("");
        txtCapacity.setText("");
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
    }
}

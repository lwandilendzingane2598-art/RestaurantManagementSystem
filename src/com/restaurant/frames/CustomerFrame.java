package com.restaurant.frames;

import com.restaurant.dao.CustomerDAO;
import com.restaurant.models.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class CustomerFrame extends JFrame {
    private CustomerDAO dao = new CustomerDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtName, txtPhone, txtEmail, txtPoints;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public CustomerFrame() {
        setTitle("Manage Customers");
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

        // Table model
        model = new DefaultTableModel(new String[]{"ID", "Name", "Phone", "Email", "Loyalty Points"}, 0) {
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

        // Row 0: Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        txtName = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtName, gbc);

        // Row 1: Phone
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        txtPhone = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(txtPhone, gbc);

        // Row 2: Email & Loyalty Points
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Loyalty Points:"), gbc);
        txtPoints = new JTextField(10);
        gbc.gridx = 3;
        formPanel.add(txtPoints, gbc);

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

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);

        // Actions
        btnAdd.addActionListener(e -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnRefresh.addActionListener(e -> loadData());

        // Table selection
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtName.setText(model.getValueAt(row, 1).toString());
                txtPhone.setText(model.getValueAt(row, 2).toString());
                txtEmail.setText(model.getValueAt(row, 3).toString());
                txtPoints.setText(model.getValueAt(row, 4).toString());
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
        List<Customer> list = dao.findAll();
        for (Customer c : list) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), c.getEmail(), c.getLoyaltyPoints()});
        }
    }

    private void addCustomer() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String pointsText = txtPoints.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required");
            return;
        }
        int points = 0;
        if (!pointsText.isEmpty()) {
            try {
                points = Integer.parseInt(pointsText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Points must be a number");
                return;
            }
        }
        Customer cust = new Customer();
        cust.setName(name);
        cust.setPhone(phone);
        cust.setEmail(email);
        cust.setLoyaltyPoints(points);
        if (dao.add(cust)) {
            JOptionPane.showMessageDialog(this, "Customer added");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Add failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String pointsText = txtPoints.getText().trim();

        if (name.isEmpty()) return;
        int points = 0;
        if (!pointsText.isEmpty()) {
            try {
                points = Integer.parseInt(pointsText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid points");
                return;
            }
        }
        Customer cust = new Customer();
        cust.setId(id);
        cust.setName(name);
        cust.setPhone(phone);
        cust.setEmail(email);
        cust.setLoyaltyPoints(points);
        if (dao.update(cust)) {
            JOptionPane.showMessageDialog(this, "Customer updated");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this customer?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(id)) {
                JOptionPane.showMessageDialog(this, "Customer deleted");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed (maybe has orders/reservations)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtPoints.setText("");
        table.clearSelection();
    }
}

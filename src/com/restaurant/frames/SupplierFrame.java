package com.restaurant.frames;

import com.restaurant.dao.SupplierDAO;
import com.restaurant.models.Supplier;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class SupplierFrame extends JFrame {
    private SupplierDAO dao = new SupplierDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtCompany, txtContact, txtPhone, txtEmail;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public SupplierFrame() {
        setTitle("Manage Suppliers");
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
        model = new DefaultTableModel(new String[]{"ID", "Company Name", "Contact Person", "Phone", "Email"}, 0) {
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

        // Row 0: Company Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Company Name:"), gbc);
        txtCompany = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtCompany, gbc);

        // Row 1: Contact Person
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Contact Person:"), gbc);
        txtContact = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtContact, gbc);

        // Row 2: Phone & Email
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Phone:"), gbc);
        txtPhone = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(txtPhone, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 3;
        formPanel.add(txtEmail, gbc);

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
        btnAdd.addActionListener(e -> addSupplier());
        btnUpdate.addActionListener(e -> updateSupplier());
        btnDelete.addActionListener(e -> deleteSupplier());
        btnRefresh.addActionListener(e -> loadData());

        // Table selection
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtCompany.setText(model.getValueAt(row, 1).toString());
                txtContact.setText(model.getValueAt(row, 2).toString());
                txtPhone.setText(model.getValueAt(row, 3).toString());
                txtEmail.setText(model.getValueAt(row, 4).toString());
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
        List<Supplier> list = dao.findAll();
        for (Supplier s : list) {
            model.addRow(new Object[]{s.getId(), s.getCompanyName(), s.getContactPerson(), s.getPhone(), s.getEmail()});
        }
    }

    private void addSupplier() {
        String company = txtCompany.getText().trim();
        String contact = txtContact.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();

        if (company.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Company name is required");
            return;
        }
        Supplier s = new Supplier();
        s.setCompanyName(company);
        s.setContactPerson(contact);
        s.setPhone(phone);
        s.setEmail(email);
        if (dao.add(s)) {
            JOptionPane.showMessageDialog(this, "Supplier added");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Add failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSupplier() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        String company = txtCompany.getText().trim();
        String contact = txtContact.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();

        if (company.isEmpty()) return;
        Supplier s = new Supplier();
        s.setId(id);
        s.setCompanyName(company);
        s.setContactPerson(contact);
        s.setPhone(phone);
        s.setEmail(email);
        if (dao.update(s)) {
            JOptionPane.showMessageDialog(this, "Supplier updated");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSupplier() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this supplier?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(id)) {
                JOptionPane.showMessageDialog(this, "Supplier deleted");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtCompany.setText("");
        txtContact.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        table.clearSelection();
    }
}

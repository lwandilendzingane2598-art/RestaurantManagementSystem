package com.restaurant.frames;

import com.restaurant.dao.EmployeeDAO;
import com.restaurant.models.Employee;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EmployeeFrame extends JFrame {
    private JTextField tfId, tfFullName, tfEmail, tfRole, tfSalary, tfHireDate;
    private JPasswordField pfPassword;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private EmployeeDAO employeeDAO;

    public EmployeeFrame() {
        employeeDAO = new EmployeeDAO();
        initUI();
        loadEmployeeData();
    }

    private void initUI() {
        setTitle("Employee Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        inputPanel.add(new JLabel("ID:"));
        tfId = new JTextField();
        tfId.setEditable(false);
        inputPanel.add(tfId);

        inputPanel.add(new JLabel("Full Name:"));
        tfFullName = new JTextField();
        inputPanel.add(tfFullName);

        inputPanel.add(new JLabel("Email:"));
        tfEmail = new JTextField();
        inputPanel.add(tfEmail);

        inputPanel.add(new JLabel("Password (only for update):"));
        pfPassword = new JPasswordField();
        inputPanel.add(pfPassword);

        inputPanel.add(new JLabel("Role:"));
        tfRole = new JTextField();
        inputPanel.add(tfRole);

        inputPanel.add(new JLabel("Salary:"));
        tfSalary = new JTextField();
        inputPanel.add(tfSalary);

        inputPanel.add(new JLabel("Hire Date (YYYY-MM-DD):"));
        tfHireDate = new JTextField();
        inputPanel.add(tfHireDate);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        // Table
        String[] columns = {"ID", "Full Name", "Email", "Role", "Salary", "Hire Date"};
        tableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Button actions
        btnAdd.addActionListener(e -> addEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());
        btnClear.addActionListener(e -> clearFields());
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = employeeTable.getSelectedRow();
                if (row != -1) displaySelectedEmployee(row);
            }
        });
    }

    private void loadEmployeeData() {
        tableModel.setRowCount(0);
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            for (Employee emp : employees) {
                tableModel.addRow(new Object[]{
                    emp.getId(),
                    emp.getFullName(),
                    emp.getEmail(),
                    emp.getRole(),
                    emp.getSalary(),
                    emp.getHireDate()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + ex.getMessage());
        }
    }

    private void displaySelectedEmployee(int row) {
        tfId.setText(tableModel.getValueAt(row, 0).toString());
        tfFullName.setText(tableModel.getValueAt(row, 1).toString());
        tfEmail.setText(tableModel.getValueAt(row, 2).toString());
        tfRole.setText(tableModel.getValueAt(row, 3).toString());
        tfSalary.setText(tableModel.getValueAt(row, 4).toString());
        tfHireDate.setText(tableModel.getValueAt(row, 5).toString());
        pfPassword.setText(""); // clear password field
    }

    private void addEmployee() {
        try {
            Employee emp = new Employee(); // use no-arg constructor
            emp.setFullName(tfFullName.getText().trim());
            emp.setEmail(tfEmail.getText().trim());
            emp.setPasswordHash(new String(pfPassword.getPassword()).trim());
            emp.setRole(tfRole.getText().trim());
            emp.setSalary(Double.parseDouble(tfSalary.getText().trim()));
            emp.setHireDate(tfHireDate.getText().trim());

            employeeDAO.addEmployee(emp);
            JOptionPane.showMessageDialog(this, "Employee added successfully!");
            clearFields();
            loadEmployeeData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid salary format.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEmployee() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select an employee to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(tfId.getText());
            Employee emp = new Employee();
            emp.setId(id);
            emp.setFullName(tfFullName.getText().trim());
            emp.setEmail(tfEmail.getText().trim());
            emp.setRole(tfRole.getText().trim());
            emp.setSalary(Double.parseDouble(tfSalary.getText().trim()));
            emp.setHireDate(tfHireDate.getText().trim());

            employeeDAO.updateEmployee(emp);

            // If password field is not empty, update password separately
            String newPwd = new String(pfPassword.getPassword()).trim();
            if (!newPwd.isEmpty()) {
                employeeDAO.updatePassword(id, newPwd);
            }

            JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            clearFields();
            loadEmployeeData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid salary format.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select an employee to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(tfId.getText());
                employeeDAO.deleteEmployee(id);
                JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
                clearFields();
                loadEmployeeData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        tfId.setText("");
        tfFullName.setText("");
        tfEmail.setText("");
        pfPassword.setText("");
        tfRole.setText("");
        tfSalary.setText("");
        tfHireDate.setText("");
        employeeTable.clearSelection();
    }
}

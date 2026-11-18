package com.restaurant.frames;

import com.restaurant.dao.ReservationDAO;
import com.restaurant.dao.CustomerDAO;
import com.restaurant.dao.TableDAO;
import com.restaurant.models.Reservation;
import com.restaurant.models.Customer;
import com.restaurant.models.DiningTable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class ReservationFrame extends JFrame {
    private ReservationDAO dao = new ReservationDAO();
    private CustomerDAO customerDao = new CustomerDAO();
    private TableDAO tableDao = new TableDAO();
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cmbCustomer, cmbTable, cmbStatus;
    private JTextField txtDate, txtTime, txtGuests;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public ReservationFrame() {
        setTitle("Manage Reservations");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(248, 245, 240));
        setIconImage(loadLogo());
        initUI();
        loadData();
        loadComboBoxes();
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
        model = new DefaultTableModel(new String[]{"ID", "Customer", "Table", "Date", "Time", "Guests", "Status"}, 0) {
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

        // Row 0: Customer
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Customer:"), gbc);
        cmbCustomer = new JComboBox<>();
        cmbCustomer.setPreferredSize(new Dimension(180, 25));
        gbc.gridx = 1;
        formPanel.add(cmbCustomer, gbc);

        // Row 1: Table
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Table:"), gbc);
        cmbTable = new JComboBox<>();
        cmbTable.setPreferredSize(new Dimension(100, 25));
        gbc.gridx = 1;
        formPanel.add(cmbTable, gbc);

        // Row 2: Date (YYYY-MM-DD)
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        txtDate = new JTextField(12);
        gbc.gridx = 1;
        formPanel.add(txtDate, gbc);

        // Row 3: Time (HH:MM:SS)
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Time (HH:MM:SS):"), gbc);
        txtTime = new JTextField(8);
        gbc.gridx = 1;
        formPanel.add(txtTime, gbc);

        // Row 4: Guests
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Guests:"), gbc);
        txtGuests = new JTextField(5);
        gbc.gridx = 1;
        formPanel.add(txtGuests, gbc);

        // Row 5: Status
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Status:"), gbc);
        cmbStatus = new JComboBox<>(new String[]{"pending", "confirmed", "cancelled"});
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

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);

        // Actions
        btnAdd.addActionListener(e -> addReservation());
        btnUpdate.addActionListener(e -> updateReservation());
        btnDelete.addActionListener(e -> deleteReservation());
        btnRefresh.addActionListener(e -> { loadData(); loadComboBoxes(); });

        // Table selection
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                // For update, we need to set the comboboxes by matching text
                String customerName = model.getValueAt(row, 1).toString();
                String tableNum = model.getValueAt(row, 2).toString();
                cmbCustomer.setSelectedItem(customerName);
                cmbTable.setSelectedItem(tableNum);
                txtDate.setText(model.getValueAt(row, 3).toString());
                txtTime.setText(model.getValueAt(row, 4).toString());
                txtGuests.setText(model.getValueAt(row, 5).toString());
                cmbStatus.setSelectedItem(model.getValueAt(row, 6).toString());
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

    private void loadComboBoxes() {
        // Load customers
        cmbCustomer.removeAllItems();
        List<Customer> customers = customerDao.findAll();
        for (Customer c : customers) {
            cmbCustomer.addItem(c.getName() + " (ID:" + c.getId() + ")");
        }
        // Load tables (available ones? But show all for now)
        cmbTable.removeAllItems();
        List<DiningTable> tables = tableDao.findAll();
        for (DiningTable t : tables) {
            cmbTable.addItem(t.getTableNumber() + " (Cap:" + t.getCapacity() + ")");
        }
    }

    private int getCustomerIdFromSelection() {
        String selected = (String) cmbCustomer.getSelectedItem();
        if (selected == null) return -1;
        int start = selected.indexOf("ID:") + 3;
        int end = selected.indexOf(")", start);
        return Integer.parseInt(selected.substring(start, end));
    }

    private int getTableIdFromSelection() {
        String selected = (String) cmbTable.getSelectedItem();
        if (selected == null) return -1;
        // Table number is before space, but we need ID. Simpler: fetch by table number from DAO.
        String tableNumber = selected.split(" ")[0];
        List<DiningTable> tables = tableDao.findAll();
        for (DiningTable t : tables) {
            if (t.getTableNumber().equals(tableNumber)) return t.getId();
        }
        return -1;
    }

    private void loadData() {
        model.setRowCount(0);
        List<Reservation> list = dao.findAll();
        for (Reservation r : list) {
            model.addRow(new Object[]{
                r.getId(),
                r.getCustomerName(),
                r.getTableNumber(),
                r.getReservationDate(),
                r.getReservationTime(),
                r.getGuests(),
                r.getStatus()
            });
        }
    }

    private void addReservation() {
        int customerId = getCustomerIdFromSelection();
        int tableId = getTableIdFromSelection();
        String dateStr = txtDate.getText().trim();
        String timeStr = txtTime.getText().trim();
        String guestsStr = txtGuests.getText().trim();
        String status = (String) cmbStatus.getSelectedItem();

        if (customerId == -1 || tableId == -1 || dateStr.isEmpty() || timeStr.isEmpty() || guestsStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields");
            
        }
        try {
            int guests = Integer.parseInt(guestsStr);
            Reservation res = new Reservation();
            res.setCustomerId(customerId);
            res.setTableId(tableId);
            res.setReservationDate(Date.valueOf(dateStr));
            res.setReservationTime(Time.valueOf(timeStr));
            res.setGuests(guests);
            res.setStatus(status);
            if (dao.update(res)) {
                JOptionPane.showMessageDialog(this, "Reservation added");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Add failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Guests must be a number", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date/time format. Use YYYY-MM-DD and HH:MM:SS", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
    private void updateReservation() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        int customerId = getCustomerIdFromSelection();
        int tableId = getTableIdFromSelection();
        String dateStr = txtDate.getText().trim();
        String timeStr = txtTime.getText().trim();
        String guestsStr = txtGuests.getText().trim();
        String status = (String) cmbStatus.getSelectedItem();

        if (customerId == -1 || tableId == -1 || dateStr.isEmpty() || timeStr.isEmpty() || guestsStr.isEmpty()) return;
        try {
            int guests = Integer.parseInt(guestsStr);
            Reservation res = new Reservation();
            res.setId(id);
            res.setCustomerId(customerId);
            res.setTableId(tableId);
            res.setReservationDate(Date.valueOf(dateStr));
            res.setReservationTime(Time.valueOf(timeStr));
            res.setGuests(guests);
            res.setStatus(status);
            if (dao.update(res)) {
                JOptionPane.showMessageDialog(this, "Reservation updated");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteReservation() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this reservation?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(id)) {
                JOptionPane.showMessageDialog(this, "Reservation deleted");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        if (cmbCustomer.getItemCount() > 0) cmbCustomer.setSelectedIndex(0);
        if (cmbTable.getItemCount() > 0) cmbTable.setSelectedIndex(0);
        txtDate.setText("");
        txtTime.setText("");
        txtGuests.setText("'");
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
    }
}

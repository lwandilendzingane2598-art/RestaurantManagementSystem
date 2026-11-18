package com.restaurant.frames;

import com.restaurant.dao.OrderDAO;
import com.restaurant.models.Order;
import com.restaurant.models.OrderItem;
import com.restaurant.utils.SessionManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderFrame extends JFrame {
    private OrderDAO dao = new OrderDAO();
    private JComboBox<String> cmbItem, cmbTable, cmbCustomer;
    private JTextField txtQuantity;
    private JButton btnAddItem, btnPlaceOrder;
    private JTable cartTable;
    private DefaultTableModel cartModel;
    private JLabel lblSubtotal, lblTax, lblTotal;
    private List<OrderItem> cart = new ArrayList<>();
    private double subtotal = 0;

    public OrderFrame() {
        setTitle("Point of Sale – New Order");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(248, 245, 240));
        setIconImage(loadLogo());
        initUI();
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

        // Top panel: item selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(new Color(248, 245, 240));
        topPanel.setBorder(BorderFactory.createTitledBorder("Add Items"));
        topPanel.add(new JLabel("Item:"));
        cmbItem = new JComboBox<>();
        cmbItem.setPreferredSize(new Dimension(250, 25));
        topPanel.add(cmbItem);
        topPanel.add(new JLabel("Qty:"));
        txtQuantity = new JTextField(5);
        topPanel.add(txtQuantity);
        btnAddItem = createStyledButton("➕ Add to Cart");
        topPanel.add(btnAddItem);
        add(topPanel, BorderLayout.NORTH);

        // Center: cart table
        cartModel = new DefaultTableModel(new String[]{"Item", "Qty", "Unit Price", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        cartTable = new JTable(cartModel);
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cartTable.setRowHeight(28);
        cartTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        cartTable.getTableHeader().setBackground(new Color(25, 55, 85));
        cartTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(cartTable);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        // Bottom panel: order info and totals
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(248, 245, 240));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Order info (table, customer)
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        infoPanel.setBackground(new Color(248, 245, 240));
        infoPanel.add(new JLabel("Table:"));
        cmbTable = new JComboBox<>();
        cmbTable.setPreferredSize(new Dimension(80, 25));
        infoPanel.add(cmbTable);
        infoPanel.add(new JLabel("Customer:"));
        cmbCustomer = new JComboBox<>();
        cmbCustomer.setPreferredSize(new Dimension(180, 25));
        infoPanel.add(cmbCustomer);
        bottomPanel.add(infoPanel, BorderLayout.NORTH);

        // Totals
        JPanel totalsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        totalsPanel.setBackground(new Color(248, 245, 240));
        lblSubtotal = new JLabel("Subtotal: $0.00");
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTax = new JLabel("Tax (10%): $0.00");
        lblTax.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(new Color(25, 55, 85));
        totalsPanel.add(lblSubtotal);
        totalsPanel.add(lblTax);
        totalsPanel.add(lblTotal);
        bottomPanel.add(totalsPanel, BorderLayout.CENTER);

        btnPlaceOrder = createStyledButton("💰 Place Order");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(new Color(248, 245, 240));
        btnPanel.add(btnPlaceOrder);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        btnAddItem.addActionListener(e -> addToCart());
        btnPlaceOrder.addActionListener(e -> placeOrder());
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
        // Menu items
        List<OrderItem> items = dao.getMenuItems();
        cmbItem.removeAllItems();
        for (OrderItem i : items) {
            cmbItem.addItem(i.getItemId() + " – " + i.getItemName() + " ($" + i.getUnitPrice() + ")");
        }
        // Tables
        List<Integer> tables = dao.getAvailableTables();
        cmbTable.removeAllItems();
        for (int t : tables) cmbTable.addItem(String.valueOf(t));
        // Customers
        List<String> customers = dao.getCustomers();
        cmbCustomer.removeAllItems();
        for (String c : customers) cmbCustomer.addItem(c);
    }

    private void addToCart() {
        if (cmbItem.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Select an item");
            return;
        }
        String qtyText = txtQuantity.getText().trim();
        if (qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter quantity");
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(qtyText);
            if (quantity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity");
            return;
        }
        String selected = (String) cmbItem.getSelectedItem();
        int itemId = Integer.parseInt(selected.split(" – ")[0]);
        String itemName = selected.split(" – ")[1].split(" \\$")[0];
        double price = Double.parseDouble(selected.split("\\$")[1]);

        // Check if already in cart
       for (OrderItem existing : cart) {
            if (existing.getItemId() == itemId) {
                existing.setQuantity(existing.getQuantity() + quantity);
                updateCartTable();
                return;
            }
        }
        OrderItem newItem = new OrderItem();
        newItem.setItemId(itemId);
        newItem.setItemName(itemName);
        newItem.setQuantity(quantity);
        newItem.setUnitPrice(price);
        cart.add(newItem);
        updateCartTable();
    }

    private void updateCartTable() {
        cartModel.setRowCount(0);
        subtotal = 0;
        for (OrderItem item : cart) {
            double itemSubtotal = item.getQuantity() * item.getUnitPrice();
            subtotal += itemSubtotal;
            cartModel.addRow(new Object[]{item.getItemName(), item.getQuantity(), "$" + item.getUnitPrice(), "$" + itemSubtotal});
        }
        double tax = subtotal * 0.10;
        double total = subtotal + tax;
        lblSubtotal.setText("Subtotal: $" + String.format("%.2f", subtotal));
        lblTax.setText("Tax (10%): $" + String.format("%.2f", tax));
        lblTotal.setText("Total: $" + String.format("%.2f", total));
    }

    private void placeOrder() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty");
            return;
        }
        if (cmbTable.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Select a table");
            return;
        }
        if (cmbCustomer.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer");
            return;
        }
        int tableId = Integer.parseInt((String) cmbTable.getSelectedItem());
        String customerSelection = (String) cmbCustomer.getSelectedItem();
        int customerId = Integer.parseInt(customerSelection.split(" – ")[0]);
        int employeeId = SessionManager.getLoggedInUser().getId();
        double total = subtotal + (subtotal * 0.10);

        Order order = new Order();
        order.setEmployeeId(employeeId);
        order.setTableId(tableId);
        order.setCustomerId(customerId);
        order.setTotalAmount(total);
        order.setStatus("pending");

        int orderId = dao.createOrder(order);
        if (orderId == -1) {
            JOptionPane.showMessageDialog(this, "Failed to create order", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (OrderItem item : cart) {
            OrderItem dbItem = new OrderItem();
            dbItem.setOrderId(orderId);
            dbItem.setItemId(item.getItemId());
            dbItem.setQuantity(item.getQuantity());
            dbItem.setUnitPrice(item.getUnitPrice());
            dao.addOrderItem(dbItem);
        }
        JOptionPane.showMessageDialog(this, "Order placed successfully! Order ID: " + orderId);
        cart.clear();
        updateCartTable();
        txtQuantity.setText("");
        loadComboBoxes(); // refresh available tables
    }
}

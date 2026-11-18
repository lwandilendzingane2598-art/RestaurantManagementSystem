package com.restaurant.frames;

import com.restaurant.dao.OrderHistoryDAO;
import com.restaurant.models.OrderHistoryDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class OrderHistoryFrame extends JFrame {
    private OrderHistoryDAO dao = new OrderHistoryDAO();
    private JTable orderTable;
    private DefaultTableModel orderModel;
    private JTextArea detailsArea;
    private JButton btnRefresh;

    public OrderHistoryFrame() {
        setTitle("Order History");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(248, 245, 240));
        setIconImage(loadLogo());
        initUI();
        loadOrders();
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

        // Orders table
        orderModel = new DefaultTableModel(new String[]{"Order ID", "Date", "Customer", "Table", "Total", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        orderTable = new JTable(orderModel);
        orderTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        orderTable.setRowHeight(28);
        orderTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        orderTable.getTableHeader().setBackground(new Color(25, 55, 85));
        orderTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollOrders = new JScrollPane(orderTable);
        scrollOrders.setBorder(BorderFactory.createTitledBorder("Orders"));
        add(scrollOrders, BorderLayout.CENTER);

        // Details area
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollDetails = new JScrollPane(detailsArea);
        scrollDetails.setBorder(BorderFactory.createTitledBorder("Order Items"));
        scrollDetails.setPreferredSize(new Dimension(300, 0));
        add(scrollDetails, BorderLayout.EAST);

        // Refresh button
        JPanel bottom = new JPanel(new FlowLayout());
        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(25, 55, 85));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> loadOrders());
        bottom.add(btnRefresh);
        add(bottom, BorderLayout.SOUTH);

        // Selection listener
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            int row = orderTable.getSelectedRow();
            if (row != -1) {
                int orderId = (int) orderModel.getValueAt(row, 0);
                loadOrderDetails(orderId);
            }
        });
    }

    private void loadOrders() {
        orderModel.setRowCount(0);
        List<OrderHistoryDTO> orders = dao.getAllOrders();
        for (OrderHistoryDTO dto : orders) {
            orderModel.addRow(new Object[]{
                dto.getOrderId(),
                dto.getOrderDate(),
                dto.getCustomerName(),
                dto.getTableNumber(),
                "$" + dto.getTotalAmount(),
                dto.getStatus()
            });
        }
    }

    private void loadOrderDetails(int orderId) {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("------------------------\n");
        sb.append("Item               Qty   Price    Subtotal\n");
        sb.append("------------------------\n");
        List<Object[]> items = dao.getOrderItems(orderId);
        for (Object[] item : items) {
            String name = (String) item[0];
            int qty = (int) item[1];
            double price = (double) item[2];
            double sub = qty * price;
            sb.append(String.format("%-20s %3d   $%6.2f   $%7.2f\n", name, qty, price, sub));
        }
        detailsArea.setText(sb.toString());
    }
}

package com.restaurant.frames;

import com.restaurant.dao.DatabaseConnection;
import com.restaurant.frames.ReportViewerFrame;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;

public class ReportViewerFrame extends JFrame {

    public ReportViewerFrame() {
        setTitle("Reports Dashboard");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(248, 245, 240));
        setIconImage(loadLogo());
        initUI();
    }

    private Image loadLogo() {
        try {
            URL url = getClass().getResource("/images/logo.png");
            if (url != null) return new ImageIcon(url).getImage();
        } catch (Exception e) {}
        return null;
    }

    private void initUI() {
        setLayout(new GridLayout(5, 1, 15, 15));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton btnDaily = createReportButton("📊 Daily Sales Report", "daily_sales");
        JButton btnLowStock = createReportButton("⚠️ Low Stock Report", "low_stock");
        JButton btnReservations = createReportButton("📅 Today’s Reservations", "reservations");
        JButton btnEmployee = createReportButton("👨‍🍳 Employee Performance", "employee_perf");
        JButton btnPayment = createReportButton("💰 Payment Summary", "payment_summary");

        add(btnDaily);
        add(btnLowStock);
        add(btnReservations);
        add(btnEmployee);
        add(btnPayment);
    }

    private JButton createReportButton(String text, String reportName) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(25, 55, 85));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
       btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> generateReport(reportName));
        return btn;
    }

    private void generateReport(String reportName) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    String path = "/reports/" + reportName + ".jrxml";
                    URL url = getClass().getResource(path);
                    if (url == null) {
                        JOptionPane.showMessageDialog(ReportViewerFrame.this,
                            "Report file not found: " + path,
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                    JasperReport jasperReport = JasperCompileManager.compileReport(url.getPath());
                    try (Connection conn = DatabaseConnection.getConnection()) {
                        JasperPrint print = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);
                        JasperViewer.viewReport(print, false);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ReportViewerFrame.this,
                        "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
                return null;
            }
        }.execute();
    }
}

package com.restaurant.frames;
import com.restaurant.frames.ReportViewerFrame;
import com.restaurant.utils.SessionManager;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Restaurant Management System – Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setJMenuBar(createMenuBar());
        setContentPane(new BackgroundPanel());
        setIconImage(loadImage("images/logo.png"));
        setVisible(true);
    }

    private Image loadImage(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) return new ImageIcon(url).getImage();
        } catch (Exception e) {}
        return null;
    }

    private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBackground(new Color(25, 55, 85));
    menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    menuBar.setLayout(new FlowLayout(FlowLayout.RIGHT, 30, 5));
    
    
    
           // Orders
        JMenu orders = new JMenu("Orders");
orders.setForeground(Color.WHITE);
orders.setFont(new Font("Segoe UI", Font.BOLD, 14));
JMenuItem newOrder = new JMenuItem("New Order");
newOrder.addActionListener(e -> new OrderFrame().setVisible(true));
orders.add(newOrder);
JMenuItem orderHistory = new JMenuItem("Order History");
orderHistory.addActionListener(e -> new OrderHistoryFrame().setVisible(true));
orders.add(orderHistory);
menuBar.add(orders);



        // Inventory
        JMenu inventory = new JMenu("Inventory");
        inventory.setForeground(Color.WHITE);
        inventory.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JMenuItem categories = new JMenuItem("Categories");
        categories.addActionListener(e -> new CategoryFrame().setVisible(true));
        inventory.add(categories);
        JMenuItem menuItems = new JMenuItem("Menu Items");
        menuItems.addActionListener(e -> new MenuItemFrame().setVisible(true));
        inventory.add(menuItems);

       
        inventory.add(new JMenuItem("Stock Level"));
        menuBar.add(inventory);

        // People
        JMenu people = new JMenu("People");
        people.setForeground(Color.WHITE);
        people.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JMenuItem employees = new JMenuItem("Employees");
       employees.addActionListener(e -> new EmployeeFrame().setVisible(true));
        menuBar.add(employees);

       JMenuItem customers = new JMenuItem("Customers");
      customers.addActionListener(e -> new CustomerFrame().setVisible(true));
      menuBar.add(customers);


        // Operations
        JMenu ops = new JMenu("Operations");
        ops.setForeground(Color.WHITE);
        ops.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JMenuItem tables = new JMenuItem("Tables");
       tables.addActionListener(e -> new TableFrame().setVisible(true));
       menuBar.add(tables);

        JMenuItem reservations = new JMenuItem("Reservations");
        reservations.addActionListener(e -> new ReservationFrame().setVisible(true));
        menuBar.add(reservations);

        JMenuItem suppliers = new JMenuItem("Suppliers");
        suppliers.addActionListener(e -> new SupplierFrame().setVisible(true));
        menuBar.add(suppliers);

        // Reports
        JMenu reports = new JMenu("Reports");
        reports.setForeground(Color.WHITE);
        reports.setFont(new Font("Segoe UI", Font.BOLD, 14));
       JMenuItem viewReports = new JMenuItem("View Reports");
        viewReports.addActionListener(e -> new ReportViewerFrame().setVisible(true));
        reports.add(viewReports);
        menuBar.add(reports);

        // Admin
        JMenu admin = new JMenu("Admin");
        admin.setForeground(Color.WHITE);
        admin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(e -> {
            SessionManager.logout();
            new LoginFrame().setVisible(true);
            dispose();
        });
        admin.add(logout);
        menuBar.add(admin);

        return menuBar;
    }
 // Panel with background image and top panel (logo + title)
    class BackgroundPanel extends JPanel {
        private Image backgroundImg;
        private Image smallLogo;

        public BackgroundPanel() {
            setLayout(new BorderLayout());
            // Load background image using ClassLoader (more reliable)
            URL bgUrl = getClass().getClassLoader().getResource("images/bg.jpg");
            if (bgUrl != null) {
                backgroundImg = new ImageIcon(bgUrl).getImage();
                System.out.println("Background image loaded successfully");
            } else {
                System.out.println("Background image NOT found – using gradient fallback");
            }
                      smallLogo = loadImage("/images/logo.png");
            add(createTopPanel(), BorderLayout.NORTH);

        }
                private JPanel createTopPanel() {
    // LEFT: logo + title
    JPanel top= new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 5));
    top.setOpaque(false);
    JLabel logoLabel = new JLabel();
    
    try {
        URL imgURL = getClass().getResource("/images/logo.png");
        if (imgURL != null) {
                            Image scaled = smallLogo.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaled));

        } else {
            logoLabel.setText("🍽️");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        }
    } catch (Exception e) {
        logoLabel.setText("🍽️");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
    top.add(logoLabel);
    JLabel titleLabel = new JLabel("Restaurant Management System");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    titleLabel.setForeground(Color.WHITE);
    top.add(titleLabel);
                return top;



                }
        

     
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImg != null) {
                g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Fallback gradient (dark blue to black)
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, new Color(25, 55, 85), getWidth(), getHeight(), Color.BLACK));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 36));
                g2d.setColor(Color.WHITE);
                
            }
        }
    }

    }

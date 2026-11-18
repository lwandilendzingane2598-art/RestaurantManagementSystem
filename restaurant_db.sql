-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 01, 2026 at 05:49 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `restaurant_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories_902012598_902012661_902011766`
--

CREATE TABLE `categories_902012598_902012661_902011766` (
  `Cat_id` int(11) NOT NULL,
  `Cat_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories_902012598_902012661_902011766`
--

INSERT INTO `categories_902012598_902012661_902011766` (`Cat_id`, `Cat_name`) VALUES
(1, 'Appetizers'),
(9, 'Beverages'),
(8, 'Desserts'),
(2, 'Main Course'),
(5, 'Salads'),
(7, 'Side Dishes');

-- --------------------------------------------------------

--
-- Table structure for table `customers_902012598_902012661_902011766`
--

CREATE TABLE `customers_902012598_902012661_902011766` (
  `Cust_id` int(11) NOT NULL,
  `Cust_name` varchar(100) NOT NULL,
  `Phone` varchar(20) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Loyalty_points` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers_902012598_902012661_902011766`
--

INSERT INTO `customers_902012598_902012661_902011766` (`Cust_id`, `Cust_name`, `Phone`, `Email`, `Loyalty_points`) VALUES
(1, 'Alice Wonder', '12345678', 'alice@mail.com', 50),
(2, 'Bonsile Gwebu', '76323422', 'boni@mail.com', 43);

-- --------------------------------------------------------

--
-- Table structure for table `employees_902012598_902012661_902011766`
--

CREATE TABLE `employees_902012598_902012661_902011766` (
  `Emp_id` int(11) NOT NULL,
  `Full_name` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Password_hash` varchar(255) NOT NULL,
  `Role` enum('admin','cashier','manager','chef') DEFAULT 'cashier',
  `Salary` decimal(10,2) DEFAULT NULL,
  `Hire_date` date DEFAULT curdate()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employees_902012598_902012661_902011766`
--

INSERT INTO `employees_902012598_902012661_902011766` (`Emp_id`, `Full_name`, `Email`, `Password_hash`, `Role`, `Salary`, `Hire_date`) VALUES
(1, 'Admin User', 'lwandile17@mail.com', 'lwah17', 'admin', 5000.00, '2026-05-29');

-- --------------------------------------------------------

--
-- Table structure for table `feedback_902012598_902012661_902011766`
--

CREATE TABLE `feedback_902012598_902012661_902011766` (
  `Feedback_id` int(11) NOT NULL,
  `Cust_id` int(11) DEFAULT NULL,
  `Order_id` int(11) DEFAULT NULL,
  `Rating` int(11) DEFAULT NULL CHECK (`Rating` between 1 and 5),
  `Comment` text DEFAULT NULL,
  `Feedback_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `inventory_logs_902012598_902012661_902011766`
--

CREATE TABLE `inventory_logs_902012598_902012661_902011766` (
  `Log_id` int(11) NOT NULL,
  `Item_id` int(11) DEFAULT NULL,
  `Change_quantity` int(11) NOT NULL,
  `Change_type` enum('sale','restock','waste','adjustment') NOT NULL,
  `Change_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `Note` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `loyalty_redemptions_902012598_902012661_902011766`
--

CREATE TABLE `loyalty_redemptions_902012598_902012661_902011766` (
  `Redemption_id` int(11) NOT NULL,
  `Cust_id` int(11) DEFAULT NULL,
  `Points_used` int(11) NOT NULL,
  `Discount_amount` decimal(10,2) DEFAULT NULL,
  `Redemption_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `Order_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `menu_items_902012598_902012661_902011766`
--

CREATE TABLE `menu_items_902012598_902012661_902011766` (
  `Item_id` int(11) NOT NULL,
  `Item_name` varchar(100) NOT NULL,
  `Description` text DEFAULT NULL,
  `Price` decimal(8,2) NOT NULL,
  `Stock_quantity` int(11) DEFAULT 0,
  `Cat_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `menu_items_902012598_902012661_902011766`
--

INSERT INTO `menu_items_902012598_902012661_902011766` (`Item_id`, `Item_name`, `Description`, `Price`, `Stock_quantity`, `Cat_id`) VALUES
(1, 'Spring Rolls', 'Crispy wrapper filled with shredded cabbage and carrots', 14.99, 100, 1),
(2, 'Grilled Chicken', '1/4 piece grilled chicken', 99.99, 100, 2),
(4, 'Bruschetta', '', 20.00, 100, 1),
(5, 'Mozzarella Sticks', 'Breaded and fried cheese served with marinara sauce.', 19.99, 100, 1),
(6, 'Chicken Wings', 'Tossed in buffalo, barbecue, or sweet chili sauce.', 29.99, 100, 1),
(8, 'Calamari', 'Crispy fried squid rings served with lemon and dipping sauce.', 14.99, 100, 1),
(9, 'Caesar Salad', '', 19.99, 100, 5),
(10, 'Greek Salad', 'Cucumbers, tomatoes, olives, feta cheese, and olive oil.', 39.99, 100, 5),
(11, 'Ribeye Steak', 'Grilled beef cut served with herb butter.', 119.99, 100, 2),
(12, 'Fettuccine Alfredo', 'Pasta tossed in a rich parmesan cream sauce.', 50.00, 100, 2),
(13, 'Chicken Tikka Masala', 'Spiced chicken pieces in a creamy tomato sauce.', 49.99, 100, 2),
(14, 'Beef Burger', 'Grilled patty on a brioche bun with cheese, lettuce, and tomato.', 49.99, 100, 2),
(15, 'Salmon Fillet', 'Pan-seared or grilled fish with a lemon-dill sauce.', 99.99, 100, 2),
(16, 'French Fries', 'Deep-fried potato strips with sea salt.', 49.99, 100, 7),
(17, 'Mashed Potatoes', 'Whipped potatoes mixed with butter and cream.', 29.99, 100, 7),
(18, 'Macaroni and Cheese', 'Baked pasta in a rich cheese sauce.', 39.99, 100, 7),
(19, 'Roasted Asparagus', 'Tender spears tossed in garlic and olive oil.', 30.00, 100, 7),
(20, 'New York Cheesecake', 'Dense, creamy cake with a graham cracker crust.', 50.00, 100, 8),
(21, 'Chocolate Lava Cake', 'Warm cake with a liquid chocolate center.', 70.00, 100, 8),
(22, 'Tiramisu', 'Coffee-soaked ladyfingers layered with mascarpone cream.', 60.50, 100, 8),
(23, 'Apple Crisp', 'Baked apples topped with a crunchy oat crumble.', 80.00, 100, 8),
(24, 'Craft Beer', 'Locally brewed IPA or lager.', 30.00, 100, 9),
(25, 'Iced Tea', 'Sweetened or unsweetened brewed black tea.', 50.00, 100, 9),
(26, 'Espresso', 'Concentrated shot of freshly brewed coffee.', 40.00, 100, 9),
(27, 'Mocktails', 'Non-alcoholic mixed drinks like a virgin mojito.', 50.00, 100, 9);

-- --------------------------------------------------------

--
-- Table structure for table `orders_902012598_902012661_902011766`
--

CREATE TABLE `orders_902012598_902012661_902011766` (
  `Order_id` int(11) NOT NULL,
  `Order_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `Emp_id` int(11) DEFAULT NULL,
  `Table_id` int(11) DEFAULT NULL,
  `Cust_id` int(11) DEFAULT NULL,
  `Total_amount` decimal(10,2) DEFAULT 0.00,
  `Status` enum('pending','paid','cancelled') DEFAULT 'pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders_902012598_902012661_902011766`
--

INSERT INTO `orders_902012598_902012661_902011766` (`Order_id`, `Order_date`, `Emp_id`, `Table_id`, `Cust_id`, `Total_amount`, `Status`) VALUES
(1, '2026-05-29 10:33:15', 1, 1, 1, 17.98, 'pending');

-- --------------------------------------------------------

--
-- Table structure for table `order_items_902012598_902012661_902011766`
--

CREATE TABLE `order_items_902012598_902012661_902011766` (
  `Order_item_id` int(11) NOT NULL,
  `Order_id` int(11) DEFAULT NULL,
  `Item_id` int(11) DEFAULT NULL,
  `Quantity` int(11) NOT NULL,
  `Unit_price` decimal(8,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items_902012598_902012661_902011766`
--

INSERT INTO `order_items_902012598_902012661_902011766` (`Order_item_id`, `Order_id`, `Item_id`, `Quantity`, `Unit_price`) VALUES
(1, 1, 1, 1, 4.99),
(2, 1, 2, 1, 12.99);

-- --------------------------------------------------------

--
-- Table structure for table `payments_902012598_902012661_902011766`
--

CREATE TABLE `payments_902012598_902012661_902011766` (
  `Payment_id` int(11) NOT NULL,
  `Order_id` int(11) DEFAULT NULL,
  `Payment_method` enum('cash','card','mobile') NOT NULL,
  `Amount` decimal(10,2) NOT NULL,
  `Payment_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reservations_902012598_902012661_902011766`
--

CREATE TABLE `reservations_902012598_902012661_902011766` (
  `Res_id` int(11) NOT NULL,
  `Cust_id` int(11) DEFAULT NULL,
  `Table_id` int(11) DEFAULT NULL,
  `Reservation_date` date NOT NULL,
  `Reservation_time` time NOT NULL,
  `Guests` int(11) NOT NULL,
  `Status` enum('pending','confirmed','cancelled') DEFAULT 'pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `shifts_902012598_902012661_902011766`
--

CREATE TABLE `shifts_902012598_902012661_902011766` (
  `Shift_id` int(11) NOT NULL,
  `Emp_id` int(11) DEFAULT NULL,
  `Shift_date` date NOT NULL,
  `Start_time` time NOT NULL,
  `End_time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `suppliers_902012598_902012661_902011766`
--

CREATE TABLE `suppliers_902012598_902012661_902011766` (
  `Supplier_id` int(11) NOT NULL,
  `Company_name` varchar(100) NOT NULL,
  `Contact_person` varchar(100) DEFAULT NULL,
  `Phone` varchar(20) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `suppliers_902012598_902012661_902011766`
--

INSERT INTO `suppliers_902012598_902012661_902011766` (`Supplier_id`, `Company_name`, `Contact_person`, `Phone`, `Email`) VALUES
(1, 'Embiveni', 'Manager', '76556666', 'embiveni@mail.com');

-- --------------------------------------------------------

--
-- Table structure for table `tables_902012598_902012661_902011766`
--

CREATE TABLE `tables_902012598_902012661_902011766` (
  `Table_id` int(11) NOT NULL,
  `Table_number` varchar(10) NOT NULL,
  `Capacity` int(11) NOT NULL,
  `Status` enum('available','occupied','reserved') DEFAULT 'available'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tables_902012598_902012661_902011766`
--

INSERT INTO `tables_902012598_902012661_902011766` (`Table_id`, `Table_number`, `Capacity`, `Status`) VALUES
(1, 'T1', 4, 'available'),
(2, 'T2', 2, 'available'),
(3, 'T3', 6, 'occupied');

-- --------------------------------------------------------

--
-- Table structure for table `table_assignments_902012598_902012661_902011766`
--

CREATE TABLE `table_assignments_902012598_902012661_902011766` (
  `Assignment_id` int(11) NOT NULL,
  `Emp_id` int(11) DEFAULT NULL,
  `Table_id` int(11) DEFAULT NULL,
  `Assignment_date` date NOT NULL,
  `Shift_period` enum('lunch','dinner') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories_902012598_902012661_902011766`
--
ALTER TABLE `categories_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Cat_id`),
  ADD UNIQUE KEY `Cat_name` (`Cat_name`);

--
-- Indexes for table `customers_902012598_902012661_902011766`
--
ALTER TABLE `customers_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Cust_id`);

--
-- Indexes for table `employees_902012598_902012661_902011766`
--
ALTER TABLE `employees_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Emp_id`),
  ADD UNIQUE KEY `Email` (`Email`);

--
-- Indexes for table `feedback_902012598_902012661_902011766`
--
ALTER TABLE `feedback_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Feedback_id`),
  ADD KEY `Cust_id` (`Cust_id`),
  ADD KEY `Order_id` (`Order_id`);

--
-- Indexes for table `inventory_logs_902012598_902012661_902011766`
--
ALTER TABLE `inventory_logs_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Log_id`),
  ADD KEY `Item_id` (`Item_id`);

--
-- Indexes for table `loyalty_redemptions_902012598_902012661_902011766`
--
ALTER TABLE `loyalty_redemptions_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Redemption_id`),
  ADD KEY `Cust_id` (`Cust_id`),
  ADD KEY `Order_id` (`Order_id`);

--
-- Indexes for table `menu_items_902012598_902012661_902011766`
--
ALTER TABLE `menu_items_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Item_id`),
  ADD KEY `Cat_id` (`Cat_id`);

--
-- Indexes for table `orders_902012598_902012661_902011766`
--
ALTER TABLE `orders_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Order_id`),
  ADD KEY `Emp_id` (`Emp_id`),
  ADD KEY `Table_id` (`Table_id`),
  ADD KEY `Cust_id` (`Cust_id`);

--
-- Indexes for table `order_items_902012598_902012661_902011766`
--
ALTER TABLE `order_items_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Order_item_id`),
  ADD KEY `Order_id` (`Order_id`),
  ADD KEY `Item_id` (`Item_id`);

--
-- Indexes for table `payments_902012598_902012661_902011766`
--
ALTER TABLE `payments_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Payment_id`),
  ADD UNIQUE KEY `Order_id` (`Order_id`);

--
-- Indexes for table `reservations_902012598_902012661_902011766`
--
ALTER TABLE `reservations_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Res_id`),
  ADD KEY `Cust_id` (`Cust_id`),
  ADD KEY `Table_id` (`Table_id`);

--
-- Indexes for table `shifts_902012598_902012661_902011766`
--
ALTER TABLE `shifts_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Shift_id`),
  ADD KEY `Emp_id` (`Emp_id`);

--
-- Indexes for table `suppliers_902012598_902012661_902011766`
--
ALTER TABLE `suppliers_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Supplier_id`);

--
-- Indexes for table `tables_902012598_902012661_902011766`
--
ALTER TABLE `tables_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Table_id`),
  ADD UNIQUE KEY `Table_number` (`Table_number`);

--
-- Indexes for table `table_assignments_902012598_902012661_902011766`
--
ALTER TABLE `table_assignments_902012598_902012661_902011766`
  ADD PRIMARY KEY (`Assignment_id`),
  ADD KEY `Emp_id` (`Emp_id`),
  ADD KEY `Table_id` (`Table_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories_902012598_902012661_902011766`
--
ALTER TABLE `categories_902012598_902012661_902011766`
  MODIFY `Cat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `customers_902012598_902012661_902011766`
--
ALTER TABLE `customers_902012598_902012661_902011766`
  MODIFY `Cust_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `employees_902012598_902012661_902011766`
--
ALTER TABLE `employees_902012598_902012661_902011766`
  MODIFY `Emp_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `feedback_902012598_902012661_902011766`
--
ALTER TABLE `feedback_902012598_902012661_902011766`
  MODIFY `Feedback_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `inventory_logs_902012598_902012661_902011766`
--
ALTER TABLE `inventory_logs_902012598_902012661_902011766`
  MODIFY `Log_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `loyalty_redemptions_902012598_902012661_902011766`
--
ALTER TABLE `loyalty_redemptions_902012598_902012661_902011766`
  MODIFY `Redemption_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `menu_items_902012598_902012661_902011766`
--
ALTER TABLE `menu_items_902012598_902012661_902011766`
  MODIFY `Item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `orders_902012598_902012661_902011766`
--
ALTER TABLE `orders_902012598_902012661_902011766`
  MODIFY `Order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `order_items_902012598_902012661_902011766`
--
ALTER TABLE `order_items_902012598_902012661_902011766`
  MODIFY `Order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `payments_902012598_902012661_902011766`
--
ALTER TABLE `payments_902012598_902012661_902011766`
  MODIFY `Payment_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `reservations_902012598_902012661_902011766`
--
ALTER TABLE `reservations_902012598_902012661_902011766`
  MODIFY `Res_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `shifts_902012598_902012661_902011766`
--
ALTER TABLE `shifts_902012598_902012661_902011766`
  MODIFY `Shift_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `suppliers_902012598_902012661_902011766`
--
ALTER TABLE `suppliers_902012598_902012661_902011766`
  MODIFY `Supplier_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tables_902012598_902012661_902011766`
--
ALTER TABLE `tables_902012598_902012661_902011766`
  MODIFY `Table_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `table_assignments_902012598_902012661_902011766`
--
ALTER TABLE `table_assignments_902012598_902012661_902011766`
  MODIFY `Assignment_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `feedback_902012598_902012661_902011766`
--
ALTER TABLE `feedback_902012598_902012661_902011766`
  ADD CONSTRAINT `feedback_902012598_902012661_902011766_ibfk_1` FOREIGN KEY (`Cust_id`) REFERENCES `customers_902012598_902012661_902011766` (`Cust_id`),
  ADD CONSTRAINT `feedback_902012598_902012661_902011766_ibfk_2` FOREIGN KEY (`Order_id`) REFERENCES `orders_902012598_902012661_902011766` (`Order_id`);

--
-- Constraints for table `inventory_logs_902012598_902012661_902011766`
--
ALTER TABLE `inventory_logs_902012598_902012661_902011766`
  ADD CONSTRAINT `inventory_logs_902012598_902012661_902011766_ibfk_1` FOREIGN KEY (`Item_id`) REFERENCES `menu_items_902012598_902012661_902011766` (`Item_id`) ON DELETE CASCADE;

--
-- Constraints for table `loyalty_redemptions_902012598_902012661_902011766`
--
ALTER TABLE `loyalty_redemptions_902012598_902012661_902011766`
  ADD CONSTRAINT `loyalty_redemptions_902012598_902012661_902011766_ibfk_1` FOREIGN KEY (`Cust_id`) REFERENCES `customers_902012598_902012661_902011766` (`Cust_id`),
  ADD CONSTRAINT `loyalty_redemptions_902012598_902012661_902011766_ibfk_2` FOREIGN KEY (`Order_id`) REFERENCES `orders_902012598_902012661_902011766` (`Order_id`);

--
-- Constraints for table `menu_items_902012598_902012661_902011766`
--
ALTER TABLE `menu_items_902012598_902012661_902011766`
  ADD CONSTRAINT `menu_items_902012598_902012661_902011766_ibfk_1` FOREIGN KEY (`Cat_id`) REFERENCES `categories_902012598_902012661_902011766` (`Cat_id`) ON DELETE SET NULL;

--
-- Constraints for table `orders_902012598_902012661_902011766`
--
ALTER TABLE `orders_902012598_902012661_902011766`
  ADD CONSTRAINT `orders_902012598_902012661_902011766_ibfk_1` FOREIGN KEY (`Emp_id`) REFERENCES `employees_902012598_902012661_902011766` (`Emp_id`),
  ADD CONSTRAINT `orders_902012598_902012661_902011766_ibfk_2` FOREIGN KEY (`Table_id`) REFERENCES `tables_902012598_902012661_902011766` (`Table_id`),
  ADD CONSTRAINT `orders_902012598_902012661_902011766_ibfk_3` FOREIGN KEY (`Cust_id`) REFERENCES `customers_902012598_902012661_902011766` (`Cust_id`);

--
-- Constraints for table `order_items_902012598_902012661_902011766`
--
ALTER TABLE `order_items_902012598_902012661_902011766`
  ADD CONSTRAINT `order_items_902012598_902012661_902011766_ibfk_1` FOREIGN KEY (`Order_id`) REFERENCES `orders_902012598_902012661_902011766` (`Order_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `order_items_902012598_902012661_902011766_ibfk_2` FOREIGN KEY (`Item_id`) REFERENCES `menu_items_902012598_902012661_902011766` (`Item_id`);

--
-- Constraints for table `payments_902012598_902012661_902011766`
--
ALTER TABLE `payments_902012598_902012661_902011766`
  ADD CONSTRAINT `payments_902012598_902012661_902011766_ibfk_1` FOREIGN KEY (`Order_id`) REFERENCES `orders_902012598_902012661_902011766` (`Order_id`) ON DELETE CASCADE;

--
-- Constraints for table `reservations_902012598_902012661_902011766`
--
ALTER TABLE `reservations_902012598_902012661_902011766`
  ADD CONSTRAINT `reservations_902012598_902012661_902011766_ibfk_1` FOREIGN KEY (`Cust_id`) REFERENCES `customers_902012598_902012661_902011766` (`Cust_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `reservations_902012598_902012661_902011766_ibfk_2` FOREIGN KEY (`Table_id`) REFERENCES `tables_902012598_902012661_902011766` (`Table_id`) ON DELETE CASCADE;

--
-- Constraints for table `shifts_902012598_902012661_902011766`
--
ALTER TABLE `shifts_902012598_902012661_902011766`
  ADD CONSTRAINT `shifts_902012598_902012661_902011766_ibfk_1` FOREIGN KEY (`Emp_id`) REFERENCES `employees_902012598_902012661_902011766` (`Emp_id`) ON DELETE CASCADE;

--
-- Constraints for table `table_assignments_902012598_902012661_902011766`
--
ALTER TABLE `table_assignments_902012598_902012661_902011766`
  ADD CONSTRAINT `table_assignments_902012598_902012661_902011766_ibfk_1` FOREIGN KEY (`Emp_id`) REFERENCES `employees_902012598_902012661_902011766` (`Emp_id`),
  ADD CONSTRAINT `table_assignments_902012598_902012661_902011766_ibfk_2` FOREIGN KEY (`Table_id`) REFERENCES `tables_902012598_902012661_902011766` (`Table_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

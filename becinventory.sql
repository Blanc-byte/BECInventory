-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 05, 2025 at 11:28 AM
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
-- Database: `becinventory`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `username` varchar(254) NOT NULL,
  `password` varchar(254) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `borrow`
--

CREATE TABLE `borrow` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `equipment_id` int(11) NOT NULL,
  `serial_id` int(11) NOT NULL,
  `status` varchar(254) NOT NULL DEFAULT 'borrowed',
  `borrowed_at` datetime(6) NOT NULL DEFAULT current_timestamp(6),
  `returned_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `borrow`
--

INSERT INTO `borrow` (`id`, `user_id`, `equipment_id`, `serial_id`, `status`, `borrowed_at`, `returned_at`) VALUES
(181, 2, 35, 230, 'returned', '2025-04-30 15:50:17.045501', '2025-04-30 15:52:55.000000'),
(182, 2, 35, 231, 'returned', '2025-04-30 15:50:17.051494', '2025-04-30 16:18:02.000000'),
(183, 2, 35, 233, 'returned', '2025-04-30 15:50:17.061474', '2025-04-30 15:52:55.000000'),
(184, 2, 35, 234, 'returned', '2025-04-30 15:50:17.068152', '2025-05-05 11:00:33.000000'),
(185, 2, 35, 235, 'returned', '2025-04-30 15:50:17.075309', '2025-05-05 11:00:34.000000'),
(186, 10, 35, 232, 'returned', '2025-04-30 15:52:19.458048', '2025-05-05 11:00:32.000000'),
(187, 10, 35, 232, 'returned', '2025-04-30 15:52:19.463500', '2025-05-05 11:00:32.000000'),
(188, 12, 35, 230, 'returned', '2025-05-05 11:00:23.932884', '2025-05-05 11:00:30.000000'),
(189, 12, 35, 231, 'returned', '2025-05-05 11:00:23.940493', '2025-05-05 11:00:31.000000');

-- --------------------------------------------------------

--
-- Table structure for table `equipments`
--

CREATE TABLE `equipments` (
  `id` int(11) NOT NULL,
  `name` varchar(254) NOT NULL,
  `stock` int(11) NOT NULL,
  `available` int(11) NOT NULL,
  `date_created` datetime(6) NOT NULL DEFAULT current_timestamp(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `equipments`
--

INSERT INTO `equipments` (`id`, `name`, `stock`, `available`, `date_created`) VALUES
(35, 'Mouse', 50, 50, '2025-04-30 15:49:28.607293');

-- --------------------------------------------------------

--
-- Table structure for table `reservation`
--

CREATE TABLE `reservation` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `equipment_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `date_reserve` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reservation`
--

INSERT INTO `reservation` (`id`, `user_id`, `equipment_id`, `quantity`, `date_reserve`) VALUES
(5, 2, 2, 12, '2024-12-23 20:04:24'),
(9, 2, 2, 12, '2024-12-24 13:43:03');

-- --------------------------------------------------------

--
-- Table structure for table `serial`
--

CREATE TABLE `serial` (
  `id` int(11) NOT NULL,
  `serial_num` varchar(254) NOT NULL,
  `equipment_id` int(11) NOT NULL,
  `status` varchar(254) NOT NULL DEFAULT 'available'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `serial`
--

INSERT INTO `serial` (`id`, `serial_num`, `equipment_id`, `status`) VALUES
(230, 'Mouse-00', 35, 'available'),
(231, 'Mouse-01', 35, 'available'),
(232, 'Mouse-02', 35, 'available'),
(233, 'Mouse-03', 35, 'available'),
(234, 'Mouse-04', 35, 'available'),
(235, 'Mouse-05', 35, 'available'),
(236, 'Mouse-06', 35, 'available'),
(237, 'Mouse-07', 35, 'available'),
(238, 'Mouse-08', 35, 'available'),
(239, 'Mouse-09', 35, 'available'),
(240, 'Mouse-010', 35, 'available'),
(241, 'Mouse-011', 35, 'available'),
(242, 'Mouse-012', 35, 'available'),
(243, 'Mouse-013', 35, 'available'),
(244, 'Mouse-014', 35, 'available'),
(245, 'Mouse-015', 35, 'available'),
(246, 'Mouse-016', 35, 'available'),
(247, 'Mouse-017', 35, 'available'),
(248, 'Mouse-018', 35, 'available'),
(249, 'Mouse-019', 35, 'available'),
(250, 'Mouse-020', 35, 'available'),
(251, 'Mouse-021', 35, 'available'),
(252, 'Mouse-022', 35, 'available'),
(253, 'Mouse-023', 35, 'available'),
(254, 'Mouse-024', 35, 'available'),
(255, 'Mouse-025', 35, 'available'),
(256, 'Mouse-026', 35, 'available'),
(257, 'Mouse-027', 35, 'available'),
(258, 'Mouse-028', 35, 'available'),
(259, 'Mouse-029', 35, 'available'),
(260, 'Mouse-030', 35, 'available'),
(261, 'Mouse-031', 35, 'available'),
(262, 'Mouse-032', 35, 'available'),
(263, 'Mouse-033', 35, 'available'),
(264, 'Mouse-034', 35, 'available'),
(265, 'Mouse-035', 35, 'available'),
(266, 'Mouse-036', 35, 'available'),
(267, 'Mouse-037', 35, 'available'),
(268, 'Mouse-038', 35, 'available'),
(269, 'Mouse-039', 35, 'available'),
(270, 'Mouse-040', 35, 'available'),
(271, 'Mouse-041', 35, 'available'),
(272, 'Mouse-042', 35, 'available'),
(273, 'Mouse-043', 35, 'available'),
(274, 'Mouse-044', 35, 'available'),
(275, 'Mouse-045', 35, 'available'),
(276, 'Mouse-046', 35, 'available'),
(277, 'Mouse-047', 35, 'available'),
(278, 'Mouse-048', 35, 'available'),
(279, 'Mouse-049', 35, 'available');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `school_id` varchar(254) DEFAULT NULL,
  `fname` varchar(254) NOT NULL,
  `mname` varchar(254) NOT NULL,
  `lname` varchar(254) NOT NULL,
  `suffix` varchar(254) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `department` varchar(254) DEFAULT NULL,
  `year_section` varchar(254) DEFAULT NULL,
  `role` varchar(254) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `school_id`, `fname`, `mname`, `lname`, `suffix`, `gender`, `username`, `password`, `department`, `year_section`, `role`) VALUES
(1, '2021-5555', 'Non', 'H', 'Tempest', 'II', 'Male', 'Noirxd', '141', 'BSIT', '4B', 'student'),
(2, '2021-5523', 'Cristian', 'H', 'Tempest', 'Jr', 'Male', 'Cris', 'cris', 'BSIT', '4B', 'student'),
(3, '2021-1523', 'wwewe', 'ewe', 'wewew', 'Jr', 'Male', 'vodak', '2232', 'BSBA', '1A', 'student'),
(4, '222', 'Crus', 'l', 'mantika', 'Jr', 'Male', 'karabaw', '123', 'BSA', '2C', 'student'),
(5, '2121', '2112', '12121', '2121', '2121', 'Male', 'grr', 'gtt', 'BSBA', 'N/A', 'staff'),
(6, '2021-1669', 'asasa', 'e', 'sasa', '', 'Female', 'madam', 'mad', 'BSBA', 'N/A', 'staff'),
(7, 'asd', 'sad', '', 'asd', '', 'Male', NULL, NULL, 'BSIT', '1b', 'faculty'),
(8, '2021-4451', 'Cnalb', '', 'Pmet', '', 'Male', NULL, NULL, 'BSIT', '1A', 'student'),
(9, '2022-5555', 'James', '', 'Boncales', '', 'Male', NULL, NULL, 'BSIT', '4A', 'student'),
(10, '2023-6565', 'Neil', '', 'Mutia', '', 'Male', NULL, NULL, 'BSIT', '4A', 'faculty'),
(11, '2021-5555', 'Luck', '', 'Clover', '', 'Male', NULL, NULL, 'BSIT', '4A', 'student'),
(12, NULL, 'Asta', '', 'Clover', '', 'null', NULL, NULL, NULL, NULL, 'faculty');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `borrow`
--
ALTER TABLE `borrow`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `equipments`
--
ALTER TABLE `equipments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `serial`
--
ALTER TABLE `serial`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `borrow`
--
ALTER TABLE `borrow`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=190;

--
-- AUTO_INCREMENT for table `equipments`
--
ALTER TABLE `equipments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `serial`
--
ALTER TABLE `serial`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=300;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 27, 2025 at 11:00 AM
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
(162, 1, 33, 200, 'returned', '2025-04-25 22:14:01.320209', '2025-04-27 16:50:31.000000'),
(163, 1, 33, 201, 'returned', '2025-04-25 22:14:01.330258', '2025-04-26 16:19:17.000000'),
(164, 2, 33, 204, 'returned', '2025-04-26 16:19:35.553553', '2025-04-27 16:50:32.000000'),
(165, 5, 33, 207, 'returned', '2025-04-26 16:28:20.373390', '2025-04-27 16:50:32.000000'),
(166, 6, 33, 202, 'returned', '2025-04-26 20:16:52.122596', '2025-04-27 16:50:33.000000'),
(167, 5, 33, 205, 'returned', '2025-04-26 20:33:33.264981', '2025-04-27 16:50:33.000000');

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
(33, 'Projector', 10, 10, '2025-04-25 22:13:25.504345');

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
(200, 'Projector-00', 33, 'available'),
(201, 'Projector-01', 33, 'available'),
(202, 'Projector-02', 33, 'available'),
(203, 'Projector-03', 33, 'available'),
(204, 'Projector-04', 33, 'available'),
(205, 'Projector-05', 33, 'available'),
(206, 'Projector-06', 33, 'available'),
(207, 'Projector-07', 33, 'available'),
(208, 'Projector-08', 33, 'available'),
(209, 'Projector-09', 33, 'available');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `school_id` varchar(254) NOT NULL,
  `fname` varchar(254) NOT NULL,
  `mname` varchar(254) NOT NULL,
  `lname` varchar(254) NOT NULL,
  `suffix` varchar(254) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `department` varchar(254) NOT NULL,
  `year_section` varchar(254) NOT NULL,
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
(8, '2021-4451', 'Cnalb', '', 'Pmet', '', 'Male', NULL, NULL, 'BSIT', '1A', 'student');

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=168;

--
-- AUTO_INCREMENT for table `equipments`
--
ALTER TABLE `equipments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `serial`
--
ALTER TABLE `serial`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=210;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

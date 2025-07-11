-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 11, 2025 at 02:43 PM
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
(189, 12, 35, 231, 'returned', '2025-05-05 11:00:23.940493', '2025-05-05 11:00:31.000000'),
(190, 8, 37, 300, 'borrowed', '2025-07-10 22:31:44.478003', NULL),
(191, 3, 37, 302, 'borrowed', '2025-07-10 22:41:46.593049', NULL),
(192, 3, 37, 301, 'borrowed', '2025-07-10 23:22:22.698246', NULL),
(193, 6, 37, 304, 'returned', '2025-07-10 23:22:32.031932', '2025-07-11 18:01:44.000000');

-- --------------------------------------------------------

--
-- Table structure for table `equipments`
--

CREATE TABLE `equipments` (
  `id` int(11) NOT NULL,
  `name` varchar(254) NOT NULL,
  `stock` int(11) NOT NULL DEFAULT 0,
  `available` int(11) NOT NULL DEFAULT 0,
  `date_created` datetime(6) NOT NULL DEFAULT current_timestamp(6),
  `stockRoom` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `equipments`
--

INSERT INTO `equipments` (`id`, `name`, `stock`, `available`, `date_created`, `stockRoom`) VALUES
(37, 'Keyboardss', 36, 39, '2025-07-10 22:07:47.058522', 1),
(38, 'Mouse', 29, 29, '2025-07-11 17:30:36.399016', 1),
(39, 'Computer', 65, 65, '2025-07-11 17:32:19.144497', 1),
(40, 'antipara', 34, 34, '2025-07-11 19:52:48.371970', 1);

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE `inventory` (
  `id` int(11) NOT NULL,
  `equipment_id` int(11) NOT NULL,
  `date_purchased` date NOT NULL,
  `property_num` varchar(256) NOT NULL,
  `description` varchar(256) NOT NULL,
  `serial_num` varchar(256) NOT NULL,
  `amount` int(11) NOT NULL,
  `par_num` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `inventory`
--

INSERT INTO `inventory` (`id`, `equipment_id`, `date_purchased`, `property_num`, `description`, `serial_num`, `amount`, `par_num`) VALUES
(1, 39, '2024-10-21', 'BEC-IT', 'desktop i5', 'aoivjfe-123', 20000, '1965'),
(2, 40, '2025-10-10', '024751', 'oijmoui', '4-41645-54', 20000, '6541');

-- --------------------------------------------------------

--
-- Table structure for table `remarks`
--

CREATE TABLE `remarks` (
  `id` int(11) NOT NULL,
  `equipment_id` int(11) NOT NULL,
  `remark` varchar(256) NOT NULL,
  `date` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `remarks`
--

INSERT INTO `remarks` (`id`, `equipment_id`, `remark`, `date`) VALUES
(1, 40, '2 items failure', '2025-07-11'),
(2, 39, '5 computers so slow to respond', '2025-07-11'),
(3, 38, '2 Missing', '2025-07-11'),
(4, 37, '16 missing', '2025-07-11'),
(5, 38, 'asdasdasdsasdasdasdsasdasdasds', '2025-07-11'),
(6, 39, 'asdasdasdsasdasdasdsasdasdasdsasdasdasds', '2025-07-11'),
(7, 40, 'asdasdasdsasdasdasdsasdasdasdsasdasdasdsasdasdasdsasdasdasdsasdasdasdsasdasdasds', '2025-07-11');

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
(300, 'Keyboardss-1', 37, 'unavailable'),
(301, 'Keyboardss-2', 37, 'unavailable'),
(302, 'Keyboardss-3', 37, 'unavailable'),
(303, 'Keyboardss-4', 37, 'available'),
(304, 'Keyboardss-5', 37, 'available'),
(305, 'Keyboardss-6', 37, 'available'),
(306, 'Keyboardss-7', 37, 'available'),
(307, 'Keyboardss-8', 37, 'available'),
(308, 'Keyboardss-9', 37, 'available'),
(309, 'Keyboardss-10', 37, 'available'),
(310, 'Keyboardss-11', 37, 'available'),
(311, 'Keyboardss-12', 37, 'available'),
(312, 'Keyboardss-13', 37, 'available'),
(313, 'Keyboardss-14', 37, 'available'),
(314, 'Keyboardss-15', 37, 'available'),
(315, 'Keyboardss-16', 37, 'available'),
(316, 'Keyboardss-17', 37, 'available'),
(317, 'Keyboardss-18', 37, 'available'),
(318, 'Keyboardss-19', 37, 'available'),
(319, 'Keyboardss-20', 37, 'available'),
(320, 'Keyboardss-21', 37, 'available'),
(321, 'Keyboardss-22', 37, 'available'),
(322, 'Keyboardss-23', 37, 'available'),
(323, 'Keyboardss-24', 37, 'available'),
(324, 'Keyboardss-25', 37, 'available'),
(325, 'Keyboardss-26', 37, 'available'),
(326, 'Keyboardss-27', 37, 'available'),
(327, 'Keyboardss-28', 37, 'available'),
(328, 'Keyboardss-29', 37, 'available'),
(329, 'Keyboardss-30', 37, 'available'),
(330, 'antipara-1', 40, 'available'),
(331, 'antipara-2', 40, 'available'),
(332, 'antipara-3', 40, 'available'),
(333, 'antipara-4', 40, 'available'),
(334, 'antipara-5', 40, 'available'),
(335, 'antipara-6', 40, 'available'),
(336, 'antipara-7', 40, 'available'),
(337, 'antipara-8', 40, 'available'),
(338, 'antipara-9', 40, 'available'),
(339, 'antipara-10', 40, 'available'),
(340, 'Keyboardss-31', 37, 'available'),
(341, 'Keyboardss-32', 37, 'available'),
(342, 'Keyboardss-33', 37, 'available'),
(343, 'Keyboardss-34', 37, 'available'),
(344, 'Keyboardss-35', 37, 'available'),
(345, 'antipara-11', 40, 'available'),
(346, 'antipara-12', 40, 'available'),
(347, 'antipara-13', 40, 'available'),
(348, 'antipara-14', 40, 'available'),
(349, 'antipara-15', 40, 'available'),
(350, 'antipara-16', 40, 'available'),
(351, 'antipara-17', 40, 'available'),
(352, 'antipara-18', 40, 'available'),
(353, 'antipara-19', 40, 'available'),
(354, 'antipara-20', 40, 'available'),
(355, 'Keyboardss-36', 37, 'available'),
(356, 'Keyboardss-37', 37, 'available'),
(357, 'Keyboardss-38', 37, 'available'),
(358, 'Keyboardss-39', 37, 'available'),
(359, 'Mouse-1', 38, 'available'),
(360, 'Mouse-2', 38, 'available'),
(361, 'Mouse-3', 38, 'available'),
(362, 'Mouse-4', 38, 'available'),
(363, 'Mouse-5', 38, 'available'),
(364, 'Mouse-6', 38, 'available'),
(365, 'Mouse-7', 38, 'available'),
(366, 'Mouse-8', 38, 'available'),
(367, 'Mouse-9', 38, 'available'),
(368, 'Mouse-10', 38, 'available'),
(369, 'Mouse-11', 38, 'available'),
(370, 'Mouse-12', 38, 'available'),
(371, 'Mouse-13', 38, 'available'),
(372, 'Mouse-14', 38, 'available'),
(373, 'Mouse-15', 38, 'available'),
(374, 'Computer-1', 39, 'available'),
(375, 'Computer-2', 39, 'available'),
(376, 'Computer-3', 39, 'available'),
(377, 'Computer-4', 39, 'available'),
(378, 'Computer-5', 39, 'available'),
(379, 'Computer-6', 39, 'available'),
(380, 'Computer-7', 39, 'available'),
(381, 'Computer-8', 39, 'available'),
(382, 'Computer-9', 39, 'available'),
(383, 'Computer-10', 39, 'available'),
(384, 'Computer-11', 39, 'available'),
(385, 'Computer-12', 39, 'available'),
(386, 'Computer-13', 39, 'available'),
(387, 'Computer-14', 39, 'available'),
(388, 'Computer-15', 39, 'available'),
(389, 'Computer-16', 39, 'available'),
(390, 'Computer-17', 39, 'available'),
(391, 'Computer-18', 39, 'available'),
(392, 'Computer-19', 39, 'available'),
(393, 'Computer-20', 39, 'available'),
(394, 'Computer-21', 39, 'available'),
(395, 'Computer-22', 39, 'available'),
(396, 'Computer-23', 39, 'available'),
(397, 'Computer-24', 39, 'available'),
(398, 'Computer-25', 39, 'available'),
(399, 'Computer-26', 39, 'available'),
(400, 'Computer-27', 39, 'available'),
(401, 'Computer-28', 39, 'available'),
(402, 'Computer-29', 39, 'available'),
(403, 'Computer-30', 39, 'available'),
(404, 'Computer-31', 39, 'available'),
(405, 'Computer-32', 39, 'available'),
(406, 'Computer-33', 39, 'available'),
(407, 'Computer-34', 39, 'available'),
(408, 'Computer-35', 39, 'available'),
(409, 'Computer-36', 39, 'available'),
(410, 'Computer-37', 39, 'available'),
(411, 'Computer-38', 39, 'available'),
(412, 'Computer-39', 39, 'available'),
(413, 'Computer-40', 39, 'available'),
(414, 'Computer-41', 39, 'available'),
(415, 'Computer-42', 39, 'available'),
(416, 'Computer-43', 39, 'available'),
(417, 'Computer-44', 39, 'available'),
(418, 'Computer-45', 39, 'available'),
(419, 'Computer-46', 39, 'available'),
(420, 'Computer-47', 39, 'available'),
(421, 'Computer-48', 39, 'available'),
(422, 'Computer-49', 39, 'available'),
(423, 'Computer-50', 39, 'available'),
(424, 'antipara-21', 40, 'available'),
(425, 'antipara-22', 40, 'available'),
(426, 'antipara-23', 40, 'available'),
(427, 'antipara-24', 40, 'available'),
(428, 'antipara-25', 40, 'available'),
(429, 'antipara-26', 40, 'available'),
(430, 'antipara-27', 40, 'available'),
(431, 'antipara-28', 40, 'available'),
(432, 'antipara-29', 40, 'available'),
(433, 'antipara-30', 40, 'available'),
(434, 'antipara-31', 40, 'available'),
(435, 'antipara-32', 40, 'available'),
(436, 'Computer-51', 39, 'available'),
(437, 'Computer-52', 39, 'available'),
(438, 'Computer-53', 39, 'available'),
(439, 'Computer-54', 39, 'available'),
(440, 'Computer-55', 39, 'available'),
(441, 'Computer-56', 39, 'available'),
(442, 'Computer-57', 39, 'available'),
(443, 'Computer-58', 39, 'available'),
(444, 'Computer-59', 39, 'available'),
(445, 'Computer-60', 39, 'available'),
(446, 'Computer-61', 39, 'available'),
(447, 'Computer-62', 39, 'available'),
(448, 'Mouse-16', 38, 'available'),
(449, 'Mouse-17', 38, 'available'),
(450, 'Mouse-18', 38, 'available'),
(451, 'Mouse-19', 38, 'available'),
(452, 'Mouse-20', 38, 'available'),
(453, 'Mouse-21', 38, 'available'),
(454, 'Mouse-22', 38, 'available'),
(455, 'Mouse-23', 38, 'available'),
(456, 'Mouse-24', 38, 'available'),
(457, 'Mouse-25', 38, 'available'),
(458, 'Mouse-26', 38, 'available'),
(459, 'Mouse-27', 38, 'available'),
(460, 'Mouse-28', 38, 'available'),
(461, 'Mouse-29', 38, 'available'),
(462, 'Computer-63', 39, 'available'),
(463, 'Computer-64', 39, 'available'),
(464, 'Computer-65', 39, 'available'),
(465, 'antipara-33', 40, 'available'),
(466, 'antipara-34', 40, 'available');

-- --------------------------------------------------------

--
-- Table structure for table `stocks`
--

CREATE TABLE `stocks` (
  `id` int(56) NOT NULL,
  `equipment_id` int(56) NOT NULL,
  `stocks` int(56) NOT NULL,
  `dated_added` datetime(6) NOT NULL DEFAULT current_timestamp(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stocks`
--

INSERT INTO `stocks` (`id`, `equipment_id`, `stocks`, `dated_added`) VALUES
(1, 37, 10, '2025-07-10 22:26:52.000000'),
(2, 37, 10, '2025-07-10 22:28:04.000000'),
(3, 37, 10, '2025-07-10 22:28:59.000000'),
(4, 40, 10, '2025-07-11 19:53:59.000000'),
(5, 37, 5, '2025-07-11 20:01:30.000000'),
(6, 40, 10, '2025-07-11 20:02:47.000000'),
(7, 37, 4, '2025-07-11 20:05:17.000000'),
(8, 38, 5, '2025-07-11 20:06:24.000000'),
(9, 38, 10, '2025-07-11 20:06:35.000000'),
(10, 39, 50, '2025-07-11 20:07:52.000000'),
(11, 40, 12, '2025-07-11 20:09:11.000000'),
(12, 39, 12, '2025-07-11 20:09:16.000000'),
(13, 38, 12, '2025-07-11 20:09:21.000000'),
(14, 38, 2, '2025-07-11 20:09:28.000000'),
(15, 39, 3, '2025-07-11 20:09:33.000000'),
(16, 40, 2, '2025-07-11 20:09:38.000000');

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
-- Indexes for table `inventory`
--
ALTER TABLE `inventory`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `remarks`
--
ALTER TABLE `remarks`
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
-- Indexes for table `stocks`
--
ALTER TABLE `stocks`
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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=194;

--
-- AUTO_INCREMENT for table `equipments`
--
ALTER TABLE `equipments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT for table `inventory`
--
ALTER TABLE `inventory`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `remarks`
--
ALTER TABLE `remarks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `serial`
--
ALTER TABLE `serial`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=467;

--
-- AUTO_INCREMENT for table `stocks`
--
ALTER TABLE `stocks`
  MODIFY `id` int(56) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

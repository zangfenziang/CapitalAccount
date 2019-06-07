-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 07, 2019 at 12:55 AM
-- Server version: 10.2.22-MariaDB
-- PHP Version: 7.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `stock`
--

-- --------------------------------------------------------

--
-- Table structure for table `capital_account`
--

CREATE TABLE `capital_account` (
  `user_id` varchar(255) NOT NULL,
  `id` varchar(255) DEFAULT NULL,
  `fund` decimal(10,2) DEFAULT NULL,
  `freezing` decimal(10,2) NOT NULL DEFAULT 0.00,
  `login_pwd` varchar(255) DEFAULT NULL,
  `securities_id` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `user_right` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `capital_account`
--

INSERT INTO `capital_account` (`user_id`, `id`, `fund`, `freezing`, `login_pwd`, `securities_id`, `status`, `user_right`) VALUES
('zfx', '111', '0.00', '10.00', '202cb962ac59075b964b07152d234b70', 'zfxzfx', 'Normal', NULL),
('account_add_by_banker', 'account_add_by_banker', '0.00', '0.00', 'b0ab9e6f55266a972952be23031a4235', 'zfxzfxzfx', 'Normal', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `capital_account_banker`
--

CREATE TABLE `capital_account_banker` (
  `id` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `capital_account_banker`
--

INSERT INTO `capital_account_banker` (`id`, `password`, `status`) VALUES
('admin', '21232f297a57a5a743894a0e4a801fc3', '0');

-- --------------------------------------------------------

--
-- Table structure for table `capital_account_legal_user`
--

CREATE TABLE `capital_account_legal_user` (
  `account_id` varchar(255) NOT NULL,
  `authorize_address` varchar(255) DEFAULT NULL,
  `authorize_id_num` varchar(255) DEFAULT NULL,
  `authorize_name` varchar(255) DEFAULT NULL,
  `authorize_phone` varchar(255) DEFAULT NULL,
  `legal_address` varchar(255) DEFAULT NULL,
  `legal_id_num` varchar(255) DEFAULT NULL,
  `legal_name` varchar(255) DEFAULT NULL,
  `legal_num` varchar(255) DEFAULT NULL,
  `legal_phone` varchar(255) DEFAULT NULL,
  `license_num` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `capital_account_legal_user`
--

INSERT INTO `capital_account_legal_user` (`account_id`, `authorize_address`, `authorize_id_num`, `authorize_name`, `authorize_phone`, `legal_address`, `legal_id_num`, `legal_name`, `legal_num`, `legal_phone`, `license_num`) VALUES
('zfxzfx', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1');

-- --------------------------------------------------------

--
-- Table structure for table `capital_account_personal_user`
--

CREATE TABLE `capital_account_personal_user` (
  `account_id` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `agency` bit(1) NOT NULL,
  `agent_id_num` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `degree` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `id_num` varchar(255) DEFAULT NULL,
  `job` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `organization` varchar(255) DEFAULT NULL,
  `phone_num` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `capital_account_personal_user`
--

INSERT INTO `capital_account_personal_user` (`account_id`, `address`, `agency`, `agent_id_num`, `date`, `degree`, `gender`, `id_num`, `job`, `name`, `organization`, `phone_num`) VALUES
('zfxzfxzfx', '11', b'1', 'dd', '2019-06-02 00:00:00', 'wer', 'werw', 'wer', 'erwer', 'wer', 'wer', 'we');

-- --------------------------------------------------------

--
-- Table structure for table `capital_account_user`
--

CREATE TABLE `capital_account_user` (
  `account_id` varchar(255) NOT NULL,
  `account_type` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `capital_account_user`
--

INSERT INTO `capital_account_user` (`account_id`, `account_type`) VALUES
('zfxzfx', 'Legal'),
('zfxzfxzfx', 'Personal');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `capital_account`
--
ALTER TABLE `capital_account`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `capital_account_banker`
--
ALTER TABLE `capital_account_banker`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `capital_account_legal_user`
--
ALTER TABLE `capital_account_legal_user`
  ADD PRIMARY KEY (`account_id`);

--
-- Indexes for table `capital_account_personal_user`
--
ALTER TABLE `capital_account_personal_user`
  ADD PRIMARY KEY (`account_id`);

--
-- Indexes for table `capital_account_user`
--
ALTER TABLE `capital_account_user`
  ADD PRIMARY KEY (`account_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

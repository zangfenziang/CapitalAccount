-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 21, 2019 at 11:19 AM
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

-- --------------------------------------------------------

--
-- Table structure for table `capital_account_user`
--

CREATE TABLE `capital_account_user` (
  `account_id` varchar(255) NOT NULL,
  `account_type` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

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

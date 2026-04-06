-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Apr 06, 2026 at 04:27 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `recipeforum_db`
--
CREATE DATABASE IF NOT EXISTS `recipeforum_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `recipeforum_db`;

-- --------------------------------------------------------

--
-- Table structure for table `allergy`
--

CREATE TABLE `allergy` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `allergy`
--

INSERT INTO `allergy` (`id`, `name`) VALUES
(1, 'Gluten'),
(2, 'Lactose'),
(3, 'Dairy'),
(4, 'Eggs'),
(5, 'Peanuts'),
(6, 'Tree nuts'),
(7, 'Soy'),
(8, 'Wheat'),
(9, 'Fish'),
(10, 'Shellfish'),
(11, 'Sesame'),
(12, 'Corn'),
(13, 'Sugar'),
(14, 'Caffeine'),
(15, 'Alcohol'),
(16, 'Chocolate');

-- --------------------------------------------------------

--
-- Table structure for table `allergy_post`
--

CREATE TABLE `allergy_post` (
  `allergy_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `allergy_user`
--

CREATE TABLE `allergy_user` (
  `allergy_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `name`) VALUES
(1, 'Vegan'),
(2, 'Dessert'),
(6, 'Cooking'),
(7, 'Baking'),
(8, 'No cooking/baking required'),
(9, 'Quickly done'),
(10, 'Original recipe');

-- --------------------------------------------------------

--
-- Table structure for table `category_post`
--

CREATE TABLE `category_post` (
  `category_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `country`
--

CREATE TABLE `country` (
  `code` varchar(3) NOT NULL,
  `name` varchar(52) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `country`
--

INSERT INTO `country` (`code`, `name`) VALUES
('AFG', 'Afghanistan'),
('AGO', 'Angola'),
('ALB', 'Albania'),
('AND', 'Andorra'),
('ARE', 'United Arab Emirates'),
('ARG', 'Argentina'),
('ARM', 'Armenia'),
('AUS', 'Australia'),
('AUT', 'Austria'),
('AZE', 'Azerbaijan'),
('BDI', 'Burundi'),
('BEL', 'Belgium'),
('BEN', 'Benin'),
('BFA', 'Burkina Faso'),
('BGD', 'Bangladesh'),
('BGR', 'Bulgaria'),
('BHR', 'Bahrain'),
('BHS', 'Bahamas'),
('BIH', 'Bosnia and Herzegovina'),
('BLR', 'Belarus'),
('BLZ', 'Belize'),
('BOL', 'Bolivia'),
('BRA', 'Brazil'),
('BRN', 'Brunei'),
('BTN', 'Bhutan'),
('BWA', 'Botswana'),
('CAN', 'Canada'),
('CHE', 'Switzerland'),
('CHL', 'Chile'),
('CHN', 'China'),
('CMR', 'Cameroon'),
('COL', 'Colombia'),
('COM', 'Comoros'),
('CRI', 'Costa Rica'),
('CUB', 'Cuba'),
('CYP', 'Cyprus'),
('CZE', 'Czech Republic'),
('DEU', 'Germany'),
('DNK', 'Denmark'),
('DOM', 'Dominican Republic'),
('DZA', 'Algeria'),
('ECU', 'Ecuador'),
('EGY', 'Egypt'),
('ESP', 'Spain'),
('EST', 'Estonia'),
('ETH', 'Ethiopia'),
('FIN', 'Finland'),
('FRA', 'France'),
('GBR', 'United Kingdom'),
('GEO', 'Georgia'),
('GHA', 'Ghana'),
('GRC', 'Greece'),
('GTM', 'Guatemala'),
('HND', 'Honduras'),
('HRV', 'Croatia'),
('HTI', 'Haiti'),
('HUN', 'Hungary'),
('IDN', 'Indonesia'),
('IND', 'India'),
('IRL', 'Ireland'),
('IRN', 'Iran'),
('IRQ', 'Iraq'),
('ISL', 'Iceland'),
('ISR', 'Israel'),
('ITA', 'Italy'),
('JAM', 'Jamaica'),
('JOR', 'Jordan'),
('JPN', 'Japan'),
('KAZ', 'Kazakhstan'),
('KEN', 'Kenya'),
('KGZ', 'Kyrgyzstan'),
('KHM', 'Cambodia'),
('KOR', 'South Korea'),
('KWT', 'Kuwait'),
('LAO', 'Laos'),
('LBN', 'Lebanon'),
('LBY', 'Libya'),
('LKA', 'Sri Lanka'),
('LTU', 'Lithuania'),
('LUX', 'Luxembourg'),
('LVA', 'Latvia'),
('MAR', 'Morocco'),
('MCO', 'Monaco'),
('MDA', 'Moldova'),
('MDG', 'Madagascar'),
('MDV', 'Maldives'),
('MEX', 'Mexico'),
('MKD', 'North Macedonia'),
('MLI', 'Mali'),
('MLT', 'Malta'),
('MMR', 'Myanmar'),
('MNE', 'Montenegro'),
('MNG', 'Mongolia'),
('MOZ', 'Mozambique'),
('MYS', 'Malaysia'),
('NAM', 'Namibia'),
('NER', 'Niger'),
('NGA', 'Nigeria'),
('NIC', 'Nicaragua'),
('NLD', 'Netherlands'),
('NOR', 'Norway'),
('NPL', 'Nepal'),
('NZL', 'New Zealand'),
('OMN', 'Oman'),
('PAK', 'Pakistan'),
('PAN', 'Panama'),
('PER', 'Peru'),
('PHL', 'Philippines'),
('POL', 'Poland'),
('PRK', 'North Korea'),
('PRT', 'Portugal'),
('PRY', 'Paraguay'),
('QAT', 'Qatar'),
('ROU', 'Romania'),
('RUS', 'Russia'),
('SAU', 'Saudi Arabia'),
('SDN', 'Sudan'),
('SGP', 'Singapore'),
('SLV', 'El Salvador'),
('SRB', 'Serbia'),
('SVK', 'Slovakia'),
('SVN', 'Slovenia'),
('SWE', 'Sweden'),
('SYR', 'Syria'),
('TCD', 'Chad'),
('THA', 'Thailand'),
('TJK', 'Tajikistan'),
('TKM', 'Turkmenistan'),
('TUN', 'Tunisia'),
('TUR', 'Turkey'),
('TWN', 'Taiwan'),
('TZA', 'Tanzania'),
('UGA', 'Uganda'),
('UKR', 'Ukraine'),
('URY', 'Uruguay'),
('USA', 'United States'),
('UZB', 'Uzbekistan'),
('VEN', 'Venezuela'),
('VNM', 'Vietnam'),
('YEM', 'Yemen'),
('ZAF', 'South Africa'),
('ZMB', 'Zambia'),
('ZWE', 'Zimbabwe');

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE `feedback` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `content` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`id`, `user_id`, `post_id`, `rating`, `content`) VALUES
(1, 2, 1, 5, 'Absolutely delicious! Tastes just like Italy.'),
(2, 3, 2, 4, 'Great burger, but I added extra spices.'),
(3, 4, 3, 5, 'Loved the flavors, very authentic curry.'),
(4, 5, 4, 4, 'Creamy and easy to make, perfect weeknight meal.'),
(5, 1, 5, 5, 'Perfect texture and rich chocolate flavor!');

-- --------------------------------------------------------

--
-- Table structure for table `post`
--

CREATE TABLE `post` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `title` varchar(40) NOT NULL,
  `content` text NOT NULL,
  `creation_date` date NOT NULL,
  `update_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `post`
--

INSERT INTO `post` (`id`, `user_id`, `title`, `content`, `creation_date`, `update_date`) VALUES
(1, 1, 'Classic Margherita Pizza', 'A simple Italian pizza with tomatoes, mozzarella, and basil.', '2026-01-10', '2026-01-10'),
(2, 2, 'Best Beef Burger', 'Juicy beef burger with homemade sauce and fresh veggies.', '2026-01-11', '2026-01-11'),
(3, 3, 'Spicy Chicken Curry', 'Traditional Indian chicken curry with rich spices.', '2026-01-12', '2026-01-12'),
(4, 4, 'Quick Pasta Alfredo', 'Creamy Alfredo pasta ready in under 30 minutes.', '2026-01-13', '2026-01-13'),
(5, 5, 'Chocolate croissants', 'Flaky croissants filled with dark chocolate', '2026-01-14', '2026-02-09');

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `name` varchar(21) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `name`) VALUES
(1, 'admin'),
(2, 'user');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `login` varchar(21) NOT NULL,
  `email` varchar(256) NOT NULL,
  `password` varchar(127) NOT NULL,
  `country` varchar(3) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `login`, `email`, `password`, `country`, `role_id`) VALUES
(1, 'chefanna', 'chefanna@gmail.com', 'hashed_pw_anna', 'HUN', 2),
(2, 'foodie_mark', 'mark.foodie@yahoo.com', 'hashed_pw_mark', 'HUN', 2),
(3, 'spicequeen', 'spicequeen@outlook.com', 'hashed_pw_spice', 'HUN', 2),
(4, 'homecook_john', 'john.cook@gmail.com', 'hashed_pw_john', 'HUN', 2),
(5, 'baker_lily', 'lily.bakes@gmail.com', 'hashed_pw_lily', 'HUN', 2),
(7, 'admin', 'admin@recipeforum.com', 'hashed_admin_pw', 'HUN', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `allergy`
--
ALTER TABLE `allergy`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `allergy_post`
--
ALTER TABLE `allergy_post`
  ADD KEY `allergy_id` (`allergy_id`,`post_id`),
  ADD KEY `post_id` (`post_id`);

--
-- Indexes for table `allergy_user`
--
ALTER TABLE `allergy_user`
  ADD KEY `fk_allergyconn_user_id` (`user_id`),
  ADD KEY `fk_allergyconn_allergy_id` (`allergy_id`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `category_post`
--
ALTER TABLE `category_post`
  ADD KEY `category_id` (`category_id`,`post_id`),
  ADD KEY `post_id` (`post_id`);

--
-- Indexes for table `country`
--
ALTER TABLE `country`
  ADD PRIMARY KEY (`code`);

--
-- Indexes for table `feedback`
--
ALTER TABLE `feedback`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_post_id` (`post_id`),
  ADD KEY `fk_user_id2` (`user_id`);

--
-- Indexes for table `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user_id` (`user_id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`login`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `fk_role_id` (`role_id`),
  ADD KEY `country` (`country`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `allergy`
--
ALTER TABLE `allergy`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `feedback`
--
ALTER TABLE `feedback`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `post`
--
ALTER TABLE `post`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `allergy_post`
--
ALTER TABLE `allergy_post`
  ADD CONSTRAINT `allergy_post_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  ADD CONSTRAINT `allergy_post_ibfk_2` FOREIGN KEY (`allergy_id`) REFERENCES `allergy` (`id`);

--
-- Constraints for table `allergy_user`
--
ALTER TABLE `allergy_user`
  ADD CONSTRAINT `fk_allergyconn_allergy_id` FOREIGN KEY (`allergy_id`) REFERENCES `allergy` (`id`),
  ADD CONSTRAINT `fk_allergyconn_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `category_post`
--
ALTER TABLE `category_post`
  ADD CONSTRAINT `category_post_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  ADD CONSTRAINT `category_post_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

--
-- Constraints for table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `fk_post_id` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  ADD CONSTRAINT `fk_user_id2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`country`) REFERENCES `country` (`code`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

/*
 Navicat Premium Data Transfer

 Source Server         : szn
 Source Server Type    : MySQL
 Source Server Version : 80030 (8.0.30)
 Source Host           : localhost:3306
 Source Schema         : pension

 Target Server Type    : MySQL
 Target Server Version : 80030 (8.0.30)
 File Encoding         : 65001

 Date: 26/06/2024 01:04:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for Insurance
-- ----------------------------
DROP TABLE IF EXISTS `Insurance`;
CREATE TABLE `Insurance` (
  `id` varchar(255) DEFAULT NULL,
  `company_address` varchar(255) DEFAULT NULL,
  `social_security_addr` varchar(255) DEFAULT NULL,
  `payment_base` varchar(255) DEFAULT NULL,
  `personal_rate` varchar(255) DEFAULT NULL,
  `company_rate` varchar(255) DEFAULT NULL,
  `personal_payments` varchar(255) DEFAULT NULL,
  `company_payments` varchar(255) DEFAULT NULL,
  `total_payments` varchar(255) DEFAULT NULL,
  `insurance_date` varchar(255) DEFAULT NULL,
  `payment_date` varchar(255) DEFAULT NULL,
  `salary` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;

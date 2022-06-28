CREATE TABLE IF NOT EXISTS `event_log` (
  `id` bigint(20) PRIMARY KEY AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `event_class_name` varchar(255) NOT NULL,
  `occured_at` datetime NOT NULL,
  `target_id` varchar(32) NOT NULL,
  `body` text NOT NULL,
  `send_queue_status` varchar(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS `event_idempotent_log` (
  `event_type` varchar(190) NOT NULL,
  `unique_key` varchar(64) NOT NULL,
  `target_id` varchar(32) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`event_type`,`unique_key`),
  KEY `idx_type_key` (`event_type`)
);

CREATE TABLE IF NOT EXISTS `sample` (
  `id` bigint(20) PRIMARY KEY AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `foo` varchar(10)
);

CREATE TABLE IF NOT EXISTS `book` (
  `uuid` varchar(32) PRIMARY KEY,
  `name` varchar(64) NOT NULL,
  `author` varchar(64) NOT NULL,
  `isbn` varchar(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS `book_version` (
  `id` bigint(20) PRIMARY KEY AUTO_INCREMENT,
  `book_uuid` varchar(32) NOT NULL,
  `version` int NOT NULL
);

CREATE TABLE IF NOT EXISTS `book_coupon` (
  `id` bigint(20) PRIMARY KEY AUTO_INCREMENT,
  `coupon_no` varchar(32) NOT NULL,
  `token` varchar(64) NOT NULL,
  `used_to` varchar(32)
);


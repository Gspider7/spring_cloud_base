/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2018-11-29 19:34:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for filter_chain_config
-- ----------------------------
DROP TABLE IF EXISTS `filter_chain_config`;
CREATE TABLE `filter_chain_config` (
  `uri` varchar(255) DEFAULT NULL,
  `filter_config` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `test`.`filter_chain_config` (`uri`, `filter_config`, `sort`) VALUES ('/druid/**', 'roles[Admin]', '1');
INSERT INTO `test`.`filter_chain_config` (`uri`, `filter_config`, `sort`) VALUES ('/login', 'anon', '10');
INSERT INTO `test`.`filter_chain_config` (`uri`, `filter_config`, `sort`) VALUES ('/testA/**', 'perms[/testA/all]', '20');
INSERT INTO `test`.`filter_chain_config` (`uri`, `filter_config`, `sort`) VALUES ('/shiro/**', 'roles[Admin]', '50');
-- 后面的filter会覆盖前面filter的配置，这是一个差集
INSERT INTO `test`.`filter_chain_config` (`uri`, `filter_config`, `sort`) VALUES ('/**', 'authc', '10000000');
INSERT INTO `test`.`filter_chain_config` (`uri`, `filter_config`, `sort`) VALUES ('/unauthorized/**', 'anon', '10000001');
INSERT INTO `test`.`filter_chain_config` (`uri`, `filter_config`, `sort`) VALUES ('/login', 'anon', '10000002');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `salt` varchar(32) DEFAULT NULL COMMENT '用于MD5密码生成的随机数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

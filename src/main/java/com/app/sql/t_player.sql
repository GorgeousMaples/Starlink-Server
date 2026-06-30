/*
 Navicat Premium Dump SQL

 Source Server         : starlink
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : starlink

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 30/06/2026 18:19:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_player
-- ----------------------------
DROP TABLE IF EXISTS `t_player`;
CREATE TABLE `t_player`  (
  `C_UID` int NOT NULL COMMENT '玩家ID',
  `C_TYPE` tinyint NOT NULL COMMENT '账号类型【0：普通玩家，1：管理员。2：超级管理员】',
  `C_NAME` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '玩家名',
  `C_PASSWORD` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号密码',
  `C_CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '账号创建时间',
  `C_LOGIN_TIME` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`C_UID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : fusheng_init

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 02/02/2024 14:19:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name`        varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '角色名称',
    `role_key`    varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
    `status`      tinyint                                                       NOT NULL DEFAULT 1 COMMENT '角色状态（1正常 0停用）',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT 'NULL' COMMENT '备注',
    `is_deleted`  tinyint                                                       NULL     DEFAULT 0 COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   bigint                                                        NULL     DEFAULT NULL COMMENT '创建者',
    `create_time` datetime                                                      NULL     DEFAULT NULL COMMENT '创建时间',
    `update_by`   bigint                                                        NULL     DEFAULT NULL COMMENT '更新者',
    `update_time` datetime                                                      NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES (1, '超级管理员', 'admin', 1, '超管', 0, NULL, '2024-01-30 16:47:28', NULL, '2024-01-30 16:47:21');
INSERT INTO `sys_role`
VALUES (2, '用户', 'user', 1, '用户', 0, NULL, '2024-01-30 16:47:31', NULL, NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `avatar`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '头像',
    `username`    varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
    `password`    varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
    `nick_name`   varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '用户昵称',
    `email`       varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '邮箱',
    `phone`       varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '手机号',
    `roles`       varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '[]' COMMENT '角色',
    `user_status` tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态',
    `is_deleted`  tinyint                                                       NOT NULL DEFAULT 0 COMMENT '是否删除(0-未删, 1-已删)',
    `update_time` datetime                                                      NULL     DEFAULT NULL COMMENT '更新时间',
    `create_by`   bigint                                                        NULL     DEFAULT NULL COMMENT '创建者',
    `create_time` datetime                                                      NULL     DEFAULT NULL COMMENT '创建时间',
    `update_by`   bigint                                                        NULL     DEFAULT NULL COMMENT '更新者',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2043
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES (1, 'https://img1.baidu.com/it/u=2231549623,3237765525&fm=253&fmt=auto&app=138&f=JPEG?w=501&h=500', 'admin',
        '677019466004860b4386cb0756a4f328', 'admin', '123456@qq.com', '13112124545', '[1]', 1, 0, '2024-01-29 14:39:02',
        NULL, '2024-01-29 14:39:02', NULL);
INSERT INTO `sys_user`
VALUES (2, 'https://fs-im-kefu.7moor-fs1.com/29397395/4d2c3f00-7d4c-11e5-af15-41bf63ae4ea0/1706622232347/file.png',
        'user', '677019466004860b4386cb0756a4f328', 'user', '123456@qq.com	13112124545', '13112124545', '[2]', 1, 0,
        '2024-01-29 14:39:02', NULL, '2024-01-29 14:39:02', NULL);

SET FOREIGN_KEY_CHECKS = 1;

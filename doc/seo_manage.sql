/*
Navicat MySQL Data Transfer

Source Server         : 120.78.83.159
Source Server Version : 50717
Source Host           : 120.78.83.159:3306
Source Database       : seo_manage

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-04-16 15:41:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for s_content
-- ----------------------------
DROP TABLE IF EXISTS `s_content`;
CREATE TABLE `s_content` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `entry_id` int(11) NOT NULL COMMENT '记录表ID',
  `content` longtext COMMENT '内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flg` int(2) NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8 COMMENT='内容表';

-- ----------------------------
-- Table structure for s_entry
-- ----------------------------
DROP TABLE IF EXISTS `s_entry`;
CREATE TABLE `s_entry` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `module_id` int(11) NOT NULL COMMENT '模块ID',
  `website_id` varchar(255) DEFAULT NULL COMMENT '站点ID',
  `website_name` varchar(255) DEFAULT NULL,
  `thumbnail` longtext COMMENT '缩略图',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `source` varchar(255) DEFAULT NULL COMMENT '来源',
  `artical_abstract` varchar(255) DEFAULT NULL COMMENT '摘要',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `release_date` date DEFAULT NULL COMMENT '发布时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flg` int(2) NOT NULL DEFAULT '0' COMMENT '删除标志',
  `issue` int(2) NOT NULL DEFAULT '1' COMMENT '是否发布1：是，0：否',
  `keyword` varchar(255) DEFAULT NULL COMMENT '关键词',
  `def1` varchar(255) DEFAULT NULL COMMENT '自定义字段',
  `def2` varchar(255) DEFAULT NULL COMMENT '自定义字段',
  `def3` varchar(255) DEFAULT NULL COMMENT '自定义字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8 COMMENT='条目表';

-- ----------------------------
-- Table structure for s_linkurl
-- ----------------------------
DROP TABLE IF EXISTS `s_linkurl`;
CREATE TABLE `s_linkurl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `website_id` int(11) DEFAULT NULL COMMENT '站点表主键',
  `station_name` varchar(255) DEFAULT NULL COMMENT '站点名称',
  `weburl` varchar(255) DEFAULT NULL COMMENT '站点链接',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flg` int(2) NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COMMENT='友情链接表';

-- ----------------------------
-- Table structure for s_module
-- ----------------------------
DROP TABLE IF EXISTS `s_module`;
CREATE TABLE `s_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `module_name` varchar(255) DEFAULT NULL COMMENT '模块名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flg` int(2) NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='模块表';

-- ----------------------------
-- Table structure for s_praise
-- ----------------------------
DROP TABLE IF EXISTS `s_praise`;
CREATE TABLE `s_praise` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `entry_id` int(11) NOT NULL COMMENT '记录表ID',
  `ip_address` varchar(255) DEFAULT NULL COMMENT 'ip地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `praise_flg` int(2) NOT NULL DEFAULT '1' COMMENT '点赞标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='点赞记录表';

-- ----------------------------
-- Table structure for s_system
-- ----------------------------
DROP TABLE IF EXISTS `s_system`;
CREATE TABLE `s_system` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `website_id` int(11) DEFAULT NULL COMMENT '站点ID',
  `keyword` varchar(255) DEFAULT NULL COMMENT '关键词',
  `website_abstract` varchar(255) DEFAULT NULL COMMENT '摘要',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flg` int(2) NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='系统管理表';

-- ----------------------------
-- Table structure for s_user
-- ----------------------------
DROP TABLE IF EXISTS `s_user`;
CREATE TABLE `s_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` int(11) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密文密码',
  `password1` varchar(255) DEFAULT NULL COMMENT '明文密码',
  `type` int(2) DEFAULT '2' COMMENT '用户类型1：管理员；2：普通用户',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flg` int(2) NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for s_views
-- ----------------------------
DROP TABLE IF EXISTS `s_views`;
CREATE TABLE `s_views` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `entry_id` int(11) NOT NULL COMMENT '记录表ID',
  `ip_address` varchar(255) DEFAULT NULL COMMENT 'ip地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='浏览记录表';

-- ----------------------------
-- Table structure for s_website
-- ----------------------------
DROP TABLE IF EXISTS `s_website`;
CREATE TABLE `s_website` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `website` varchar(255) DEFAULT NULL COMMENT '站点名称',
  `url` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL COMMENT '关键词',
  `website_abstract` varchar(255) DEFAULT NULL COMMENT '摘要',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flg` int(2) NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='站点管理表';

-- ----------------------------
-- Table structure for s_wm_relation
-- ----------------------------
DROP TABLE IF EXISTS `s_wm_relation`;
CREATE TABLE `s_wm_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `website_id` int(11) DEFAULT NULL COMMENT '站点表主键',
  `module_id` int(11) DEFAULT NULL COMMENT '模块表主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COMMENT='站点和模块关系表';

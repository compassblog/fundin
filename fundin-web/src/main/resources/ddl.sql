ALTER TABLE `fundin_user`
	ADD COLUMN `invite_user_id` INT(11) UNSIGNED NULL DEFAULT NULL COMMENT '邀请人ID' AFTER `transaction_amount`,
	ADD COLUMN `invite_count` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '邀请的人数' AFTER `invite_user_id`;

ALTER TABLE `fundin_repay`
	DROP COLUMN `amount`;

ALTER TABLE `fundin_repay`
	ALTER `content` DROP DEFAULT;
ALTER TABLE `fundin_repay`
	CHANGE COLUMN `content` `content` VARCHAR(10000) NULL COMMENT '回报内容' AFTER `proj_id`;
	
ALTER TABLE `wx_pay_order`
	ADD COLUMN `primary_amount` INT(10) UNSIGNED NULL DEFAULT '0' COMMENT '原始金额' AFTER `status`,
	ADD COLUMN `redpacket_amount` INT(10) UNSIGNED NULL DEFAULT '0' COMMENT '红包金额' AFTER `primary_amount`;

ALTER TABLE `fundin_pay_record`
	ALTER `order_id` DROP DEFAULT;
ALTER TABLE `fundin_pay_record`
	CHANGE COLUMN `order_id` `order_id` INT(11) UNSIGNED NULL COMMENT '订单ID' AFTER `user_id`,
	ADD COLUMN `primary_amount` INT(11) UNSIGNED NULL DEFAULT '0' COMMENT '原始应付金额' AFTER `time`,
	ADD COLUMN `redpacket_amount` INT(11) UNSIGNED NULL DEFAULT '0' COMMENT '红包金额' AFTER `primary_amount`;


CREATE TABLE `fundin_redpacket` (
	`id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
	`user_id` INT(11) UNSIGNED NOT NULL COMMENT '用戶ID',
	`proj_id` INT(11) UNSIGNED NULL DEFAULT NULL COMMENT '项目ID',
	`type` TINYINT(1) UNSIGNED NOT NULL COMMENT '红包类型(1支持红包 2发起红包)',
	`amount` INT(5) UNSIGNED NOT NULL COMMENT '红包额度',
	`status` TINYINT(1) UNSIGNED NOT NULL COMMENT '红包状态(1可以用 2已用 3过期)',
	`create_time` DATETIME NOT NULL COMMENT '红包创建时间',
	`used_time` DATETIME NULL DEFAULT NULL COMMENT '红包使用时间',
	`begin_time` DATETIME NOT NULL COMMENT '红包有效起始时间',
	`end_time` DATETIME NOT NULL COMMENT '红包有效终止时间',
	PRIMARY KEY (`id`)
)
COMMENT='红包表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

ALTER TABLE `fundin_user`
ADD COLUMN `openid`  varchar(200) NULL COMMENT '微信openid' AFTER `invite_count`;

ALTER TABLE `fundin_user`
MODIFY COLUMN `passwd`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '密码' AFTER `name`,
MODIFY COLUMN `salt`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '加密盐' AFTER `passwd`,
MODIFY COLUMN `email`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '邮箱' AFTER `type`;

ALTER TABLE `fundin_proj`
	ADD COLUMN `repay_content` VARCHAR(10000) NULL DEFAULT NULL COMMENT '回报内容' AFTER `status`,
	ADD COLUMN `repay_image` VARCHAR(256) NULL DEFAULT NULL COMMENT '回报图片' AFTER `repay_content`;

ALTER TABLE `wx_pay_order`
	DROP COLUMN `repay_id`;

ALTER TABLE `fundin_pay_record`
	DROP COLUMN `repay_id`;


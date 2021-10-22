CREATE TABLE `t_internal_config`
(
    `k`       VARCHAR(255) PRIMARY KEY NOT NULL,
    `v`       VARCHAR(255)             NOT NULL,
    `details` VARCHAR(255)             NOT NULL DEFAULT ''
) ENGINE 'InnoDB' COMMENT '内部配置表';
INSERT INTO `t_internal_config`(`k`, `v`, details)
VALUES ('user-group.default.id', '1', '默认管理员组ID');
CREATE TABLE `t_config`
(
    `k` VARCHAR(255) PRIMARY KEY NOT NULL,
    `v` MEDIUMTEXT               NOT NULL
) ENGINE 'InnoDB' COMMENT '配置表';

CREATE TABLE `t_user`
(
    `t_id`          INT(20) PRIMARY KEY NOT NULL COMMENT '用户id',
    `email`         VARCHAR(128)        NOT NULL COMMENT '邮箱',
    `name`          VARCHAR(128)        NOT NULL COMMENT '姓名',
    `password`      VARCHAR(512)        NOT NULL COMMENT '密码',
    `2fa_code`      VARCHAR(64)         NOT NULL DEFAULT '' COMMENT '2FA验证',
    `register_time` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    UNIQUE INDEX user_email (email) USING BTREE

) ENGINE 'InnoDB';
CREATE TABLE t_user_group
(
    `t_id`          INT(11)      NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    `name`          VARCHAR(128) NOT NULL COMMENT '角色名称',
    `default_group` BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '默认用户组',
    `details`       VARCHAR(255) NOT NULL DEFAULT '' COMMENT '角色简易介绍'
) ENGINE = 'InnoDB';
CREATE TABLE t_user_authority
(
    `t_id`    VARCHAR(255) NOT NULL COMMENT '权限名称',
    `details` VARCHAR(255) NOT NULL COMMENT '权限简易介绍',
    PRIMARY KEY (`t_id`) USING BTREE
) ENGINE = 'InnoDB';
CREATE TABLE `t_user_to_group`
(
    `user_id`  INT(11) NOT NULL,
    `group_id` INT(11) NOT NULL,
    PRIMARY KEY (`user_id`, `group_id`) USING BTREE,
    INDEX (`group_id`) USING BTREE,
    CONSTRAINT FOREIGN KEY (`group_id`) REFERENCES t_user_group (`t_id`)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `t_user` (`t_id`)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
) ENGINE = 'InnoDB';
CREATE TABLE t_user_group_to_authority
(
    `group_id`     INT(11)      NOT NULL,
    `authority_id` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_unicode_ci',
    PRIMARY KEY (`group_id`, `authority_id`) USING BTREE,
    INDEX (`authority_id`) USING BTREE,
    CONSTRAINT FOREIGN KEY (`group_id`) REFERENCES t_user_group (`t_id`)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    CONSTRAINT FOREIGN KEY (`authority_id`) REFERENCES t_user_authority (`t_id`)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
) ENGINE = InnoDB;

INSERT INTO t_user(t_id, email, name, password)
VALUES (1, 'admin@admin.com',
        'admin',
        CONCAT('V1:', SHA2(CONCAT('watch-dog-', SHA2('admin', 256)), 256)));

SET @default_admin_group_id := (SELECT v
                                FROM t_internal_config
                                WHERE k = 'user-group.default.id');

INSERT INTO t_user_group(t_id, name, details)
VALUES (@default_admin_group_id, '管理员', '管理员角色，具有一切权限');

INSERT INTO t_user_to_group(user_id, group_id)
VALUES (1, @default_admin_group_id);

INSERT INTO t_user_authority(t_id, details)
VALUES ('user.login', '用户登录权限'),
       ('user.info.pub.ro', '查询其他用户公开信息权限'),
       ('user.info.own.rw', '操作用户自身所有信息的权限'),
       ('user.info.all.rw', '操作其他用户所有信息的权限');



DROP PROCEDURE IF EXISTS update_admin_group_authority;
CREATE PROCEDURE update_admin_group_authority()
BEGIN

    SET @admin_group_id :=
            (SELECT v
             FROM t_internal_config
             WHERE k = 'user-group.default.id');
    INSERT INTO t_user_group_to_authority(group_id, authority_id)
    SELECT @admin_group_id, t_id
    FROM t_user_authority
    WHERE t_id not in
          (SELECT authority_id
           FROM t_user_group_to_authority
           WHERE group_id = @admin_group_id);

END;
CALL update_admin_group_authority();

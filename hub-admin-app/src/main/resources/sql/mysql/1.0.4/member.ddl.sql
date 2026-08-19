-- 会员用户
drop table if exists hub_member;
create table hub_member
(
    member_id      int auto_increment primary key comment '会员id',
    tenant_id    varchar(64) comment '租户id',
    member_type    varchar(64) comment '会员类型（授权服务类型 gitlab/qq/wechat）',
    member_code    varchar(64) comment '会员编码',
    member_account varchar(64) not null comment '会员账号',
    member_name    varchar(64) not null comment '会员名称',
    member_avatar  varchar(256) comment '会员头像',
    member_email   varchar(128) comment '会员邮箱',
    member_status  smallint default 1 comment '会员状态',
    create_by    varchar(64) comment '创建人',
    create_time  datetime comment '创建时间',
    update_by    varchar(64) comment '更新人',
    update_time  datetime comment '更新时间',
    unique key hub_member_member_code (member_code),
    unique key hub_member_member_account (tenant_id, member_type, member_account)
) comment='会员用户';

-- 会员角色
drop table if exists hub_member_role;
create table hub_member_role
(
    member_id int not null comment '会员id',
    role_id int not null comment '角色id',
    primary key (member_id, role_id)
) comment='会员角色';

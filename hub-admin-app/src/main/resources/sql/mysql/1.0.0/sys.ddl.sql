-- 系统配置
drop table if exists sys_config;
CREATE TABLE sys_config
(
    config_id    int auto_increment primary key comment '参数id',
    tenant_id    varchar(64) comment '租户id',
    config_name  varchar(100) DEFAULT '' comment '参数名称',
    config_key   varchar(100) DEFAULT '' comment '参数键',
    config_value varchar(500) DEFAULT '' comment '参数值',
    value_type   varchar(64) comment '值类型',
    value_parser varchar(100) comment '值转换器',
    is_default   smallint DEFAULT 0 comment '是否默认 1是 0否',
    remark       varchar(500) DEFAULT NULL comment '备注',
    create_by    varchar(64) comment '创建人',
    create_time  datetime comment '创建时间',
    update_by    varchar(64) comment '更新人',
    update_time  datetime comment '更新时间'
) comment='系统配置';
create unique index sys_config_config_key on sys_config(tenant_id, config_key);

-- 字典数据
drop table if exists sys_dict;
create table sys_dict(
    id bigint auto_increment primary key comment '主键',
    parent_code varchar(100) comment '父字典编码',
    dict_code varchar(100) comment '字典编码',
    dict_name varchar(100) comment '字典名称',
    dict_value varchar(100) comment '字典值',
    value_type varchar(64) comment '值类型',
    value_parser varchar(100) comment '值转换器',
    dict_order smallint default 0 comment '字典排序',
    is_default smallint default 0 comment '是否默认',
    css varchar(100) comment '字典展示样式',
    status smallint default 1 comment '字典状态',
    remark varchar(200) comment '备注',
    create_by varchar(64) comment '创建人',
    create_time datetime comment '创建时间',
    update_by varchar(64) comment '更新人',
    update_time datetime comment '更新时间'
) comment='字典数据';
create unique index sys_dict_uk on sys_dict(dict_code);

-- 系统公告
drop table if exists sys_notice;
create table sys_notice
(
    notice_id     bigint auto_increment primary key comment '公告id',
    tenant_id     varchar(64) comment '租户id',
    notice_title  varchar(255) comment '标题',
    notice_status smallint default 0 comment '公告状态 0草稿 1已发布 2已撤回 3已删除',
    notice_type   smallint default 0 comment '公告类型 0公告 1通知',
    notice_level  smallint default 0 comment '公告等级 0普通 1紧急',
    content       text comment '公告内容',
    is_system     smallint default 0 comment '是否系统公告 0否 1是',
    stat_total    int default 0 comment '目标数',
    stat_read     int default 0 comment '已读数',
    goals_all     smallint default 0 comment '是否全员 0否 1是',
    goals_dept    text comment '目标单位',
    goals_role    text comment '目标角色',
    goals_user    text comment '目标用户',
    publish_time  datetime comment '发布时间',
    create_by     varchar(64) comment '创建人',
    create_time   datetime comment '创建时间',
    update_by     varchar(64) comment '更新人',
    update_time   datetime comment '更新时间'
) comment='系统公告';

-- 公告已读
drop table if exists sys_notice_user;
create table sys_notice_user
(
    id          bigint auto_increment primary key comment '主键',
    notice_id   bigint comment '公告id',
    user_code   varchar(64) comment '用户',
    read_status smallint default 0 comment '已读状态',
    read_back   varchar(512) comment '读反馈',
    read_time   datetime comment '读时间'
) comment='公告已读';
create unique index sys_notice_user_uk on sys_notice_user(user_code, notice_id);

-- 附件信息
drop table if exists sys_attach;
create table sys_attach
(
    attach_id     bigint auto_increment primary key comment '附件id',
    tenant_id     varchar(64) comment '租户id',
    owner_id      varchar(64) comment '宿主id',
    owner_module  varchar(64) comment '宿主类型',
    attach_type   varchar(64) comment '附件类型',
    attach_name   varchar(1024) comment '附件名称',
    attach_size   bigint comment '附件大小',
    attach_path   varchar(1024) comment '附件路径',
    is_private    smallint default 0 comment '是否私有的 0否 1是',
    expire_time   datetime comment '过期时间',
    create_by     varchar(64) comment '创建人',
    create_time   datetime comment '创建时间',
    update_by     varchar(64) comment '更新人',
    update_time   datetime comment '更新时间'
) comment='附件信息';
create index sys_attach_master on sys_attach(owner_id, owner_module, attach_type);

-- 系统告警
drop table if exists sys_alarm;
create table sys_alarm(
    id            bigint auto_increment primary key comment '告警id',
    alarm_code    varchar(128) not null comment '唯一编码',
    alarm_type    bigint comment '告警类型',
    alarm_level   smallint default 1 comment '告警级别：1提示 2普通 3重要 4严重 5灾难',
    source_id     bigint comment '告警来源id',
    source_name   varchar(64) comment '告警来源名称',
    source_type   varchar(64) comment '告警来源类型',
    alarm_status  smallint default 0 comment '告警状态 0未处理 1已确认 2已解决',
    alarm_times   int default 1 comment '告警次数',
    first_time    datetime comment '首次告警时间',
    last_time     datetime comment '最后告警时间',
    alarm_desc    varchar(255) comment '告警描述',
    alarm_content jsonb default ('{}') comment '告警内容',
    resolve_user  bigint comment '处理人',
    resolve_msg   varchar(255) comment '处理意见',
    resolve_time  datetime comment '处理时间',
    resolve_type  smallint default 1 comment '处理方式：1:手动 2:自动'
) comment='系统告警';
create index sys_alarm_alarm_code on sys_alarm(alarm_code);

-- 告警类型
drop table if exists sys_alarm_type;
create table sys_alarm_type(
    id          bigint auto_increment primary key comment '主键',
    type_name   varchar(128) comment '类型名称',
    type_view   varchar(128) comment '类型表单',
    description text comment '类型描述'
) comment='告警类型';

-- 系统配置
drop table if exists sys_config;
CREATE TABLE sys_config
(
    config_id    serial primary key,
    tenant_id    character varying(64),
    config_name  varchar(100) DEFAULT '',
    config_key   varchar(100) DEFAULT '',
    config_value varchar(500) DEFAULT '',
    value_type   varchar(64),
    value_parser varchar(100),
    is_default   int2         DEFAULT 0,
    remark       varchar(500) DEFAULT NULL,
    create_by    varchar(64),
    create_time  timestamp,
    update_by    varchar(64),
    update_time  timestamp
);
create unique index sys_config_config_key on sys_config(tenant_id, config_key);
comment on table sys_config is '系统配置';
comment on column sys_config.config_id is '参数id';
comment on column sys_config.config_name is '参数名称';
comment on column sys_config.config_key is '参数键';
comment on column sys_config.config_value is '参数值';
comment on column sys_config.value_parser is '值转换器';
comment on column sys_config.value_type is '值类型';
comment on column sys_config.is_default is '是否默认 1是 0否';
comment on column sys_config.remark is '备注';
comment on column sys_config.create_by is '创建人';
comment on column sys_config.create_time is '创建时间';
comment on column sys_config.update_by is '更新人';
comment on column sys_config.update_time is '更新时间';

-- 字典数据
drop table if exists sys_dict;
create table sys_dict(
    id bigserial primary key,
    parent_code character varying(100),
	dict_code character varying(100),
	dict_name character varying(100),
    dict_value character varying(100),
    value_type varchar(64),
    value_parser varchar(100),
    dict_order int2 default 0,
	is_default int2 default 0,
	css character varying(100),
    status int2 default 1,
    remark      character varying(200),
    create_by    varchar(64),
    create_time  timestamp,
    update_by    varchar(64),
    update_time  timestamp
);
create unique index sys_dict_uk on sys_dict(dict_code);
comment on table sys_dict is '字典数据';
comment on column sys_dict.id is '主键';
comment on column sys_dict.parent_code is '父字典编码';
comment on column sys_dict.dict_code is '字典编码';
comment on column sys_dict.dict_name is '字典名称';
comment on column sys_dict.dict_value is '字典值';
comment on column sys_dict.value_parser is '值转换器';
comment on column sys_dict.value_type is '值类型';
comment on column sys_dict.dict_order is '字典排序';
comment on column sys_dict.status is '字典状态';
comment on column sys_dict.is_default is '是否默认';
comment on column sys_dict.css is '字典展示样式';
comment on column sys_dict.remark is '备注';
comment on column sys_dict.create_by is '创建人';
comment on column sys_dict.create_time is '创建时间';
comment on column sys_dict.update_by is '更新人';
comment on column sys_dict.update_time is '更新时间';

-- 系统公告
drop table if exists sys_notice;
create table sys_notice
(
    notice_id     bigserial primary key,
    tenant_id     character varying(64),
    notice_title  character varying(255),
    notice_status int2 default 0,
    notice_type   int2 default 0,
    notice_level  int2 default 0,
    content       text,
    is_system     int2 default 0,
    stat_total    int4 default 0,
    stat_read     int4 default 0,
    goals_all     int2 default 0,
    goals_dept    int8[],
    goals_role    int8[],
    goals_user    int8[],
    publish_time  timestamp,
    create_by     character varying(64),
    create_time   timestamp,
    update_by     character varying(64),
    update_time   timestamp
);
comment on table sys_notice is '系统公告';
comment on column sys_notice.notice_id is '公告id';
comment on column sys_notice.notice_title is '标题';
comment on column sys_notice.notice_status is '公告状态 0草稿 1已发布 2已撤回 3已删除';
comment on column sys_notice.notice_type is '公告类型 0公告 1通知';
comment on column sys_notice.notice_level is '公告等级 0普通 1紧急';
comment on column sys_notice.content is '公告内容';
comment on column sys_notice.is_system is '是否系统公告 0否 1是';
comment on column sys_notice.stat_total is '目标数';
comment on column sys_notice.stat_read is '已读数';
comment on column sys_notice.goals_all is '是否全员 0否 1是';
comment on column sys_notice.goals_dept is '目标单位';
comment on column sys_notice.goals_role is '目标角色';
comment on column sys_notice.goals_user is '目标用户';

-- 公告已读
drop table if exists sys_notice_user;
create table sys_notice_user
(
    id          bigserial primary key,
    notice_id   int8,
    user_code   character varying(64),
    read_status int2 default 0,
    read_back   character varying(512),
    read_time   timestamp
);
create unique index sys_notice_user_uk on sys_notice_user(user_code, notice_id);
comment on table sys_notice_user is '公告已读';
comment on column sys_notice_user.id is '主键';
comment on column sys_notice_user.notice_id is '公告id';
comment on column sys_notice_user.user_code is '用户';
comment on column sys_notice_user.read_status is '已读状态';
comment on column sys_notice_user.read_back is '读反馈';
comment on column sys_notice_user.read_time is '读时间';

-- 附件信息
drop table if exists sys_attach;
create table sys_attach
(
    attach_id    bigserial primary key,
    tenant_id    character varying(64),
    owner_id     varchar(64),
    owner_module character varying(64),
    md5          character varying(32),
    attach_type  character varying(64),
    attach_name  character varying(1024),
    attach_size  int8,
    attach_path  character varying(1024),
    is_private   int2 default 0,
    expire_time  timestamp,
    create_by    varchar(64),
    create_time  timestamp,
    update_by    varchar(64),
    update_time  timestamp
);
create index sys_attach_master on sys_attach(owner_id, owner_module, attach_type);
create index sys_attach_md5_idx on sys_attach(tenant_id, md5);
comment on table sys_attach is '附件信息';
comment on column sys_attach.attach_id is '附件id';
comment on column sys_attach.tenant_id is '租户id';
comment on column sys_attach.owner_id is '宿主id';
comment on column sys_attach.owner_module is '宿主类型';
comment on column sys_attach.md5 is '文件内容md5';
comment on column sys_attach.attach_type is '附件类型';
comment on column sys_attach.attach_name is '附件名称';
comment on column sys_attach.attach_size is '附件大小';
comment on column sys_attach.attach_path is '附件路径';
comment on column sys_attach.is_private is '是否私有的 0否 1是';
comment on column sys_attach.expire_time is '过期时间';

-- 系统告警
drop table if exists sys_alarm;
create table sys_alarm(
    id            bigserial primary key,
    alarm_code    character varying(128) not null,
    alarm_type    int8,
    alarm_level   int2 default 1,
    source_id     int8,
    source_name   character varying(64),
    source_type   character varying(64),
    alarm_status  int2 default 0,
    alarm_times   int4 default 1,
    first_time    timestamp,
    last_time     timestamp,
    alarm_desc    character varying(255),
    alarm_content jsonb default '{}',
    resolve_user  int8,
    resolve_msg   character varying(255),
    resolve_time  timestamp,
    resolve_type  int2 default 1

);
create index sys_alarm_alarm_code on sys_alarm(alarm_code);
comment on table sys_alarm is '系统告警';
comment on column sys_alarm.id is '告警id';
comment on column sys_alarm.alarm_code is '唯一编码';
comment on column sys_alarm.alarm_type is '告警类型';
comment on column sys_alarm.source_id is '告警来源id';
comment on column sys_alarm.source_name is '告警来源名称';
comment on column sys_alarm.source_type is '告警来源类型';
comment on column sys_alarm.alarm_level is '告警级别：1提示 2普通 3重要 4严重 5灾难';
comment on column sys_alarm.alarm_status is '告警状态 0未处理 1已确认 2已解决';
comment on column sys_alarm.alarm_times is '告警次数';
comment on column sys_alarm.first_time is '首次告警时间';
comment on column sys_alarm.last_time is '最后告警时间';
comment on column sys_alarm.alarm_desc is '告警描述';
comment on column sys_alarm.alarm_content is '告警内容';
comment on column sys_alarm.resolve_user is '处理人';
comment on column sys_alarm.resolve_time is '处理时间';
comment on column sys_alarm.resolve_msg is '处理意见';
comment on column sys_alarm.resolve_type is '处理方式：1:手动 2:自动';

-- 告警类型
drop table if exists sys_alarm_type;
create table sys_alarm_type(
    id          bigserial primary key,
    type_name   character varying(128),
    type_view   character varying(128),
    description text
);
comment on table sys_alarm_type is '告警类型';
comment on column sys_alarm_type.id is '主键';
comment on column sys_alarm_type.type_name is '类型名称';
comment on column sys_alarm_type.type_view is '类型表单';
comment on column sys_alarm_type.description is '类型描述';

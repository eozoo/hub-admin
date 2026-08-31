-- 1.领域模块
drop table if exists sys_module;
create table sys_module
(
    module_id    serial primary key,
    tenant_id    int4 not null,
    parent_code  character varying(100),
    module_code  character varying(100) not null,
    module_name  character varying(100) not null,
    module_order int4 not null default 0,
    status       int2 not null default 1,
    is_template  int2 not null default 0,
    remark       character varying(200),
    create_by    character varying(64),
    create_time  timestamptz not null default current_timestamp,
    update_by    character varying(64),
    update_time  timestamptz
);
create unique index sys_module_code_uq on sys_module(tenant_id, module_code);
create index sys_module_parent on sys_module(tenant_id, parent_code, status);
comment on table sys_module is '领域模块';
comment on column sys_module.module_id is '模块id';
comment on column sys_module.tenant_id is '租户id';
comment on column sys_module.parent_code is '上级模块编码，空表示顶级模块';
comment on column sys_module.module_code is '租户内唯一模块编码';
comment on column sys_module.module_name is '模块名称';
comment on column sys_module.module_order is '模块排序';
comment on column sys_module.status is '状态 1启用 2停用';
comment on column sys_module.is_template is '是否新租户模板数据 1是 0否，仅system租户有效，复制后置0';
comment on column sys_module.remark is '备注';
comment on column sys_module.create_by is '创建人';
comment on column sys_module.create_time is '创建时间';
comment on column sys_module.update_by is '更新人';
comment on column sys_module.update_time is '更新时间';

-- 2.系统配置
drop table if exists sys_config;
create table sys_config
(
    config_id    serial primary key,
    tenant_id    int4 not null,
    config_name  character varying(100) not null,
    config_key   character varying(100) not null,
    config_value text,
    value_type   character varying(64) default 'string',
    value_parser character varying(100),
    is_default   int2 not null default 0,
    is_template  int2 not null default 0,
    status       int2 not null default 1,
    remark       character varying(500),
    create_by    character varying(64),
    create_time  timestamptz not null default current_timestamp,
    update_by    character varying(64),
    update_time  timestamptz
);
create unique index sys_config_key_uq on sys_config(tenant_id, config_key);
comment on table sys_config is '系统配置';
comment on column sys_config.config_id is '配置id';
comment on column sys_config.tenant_id is '租户id，系统默认配置归属tenant_code为system的系统租户';
comment on column sys_config.config_name is '配置名称';
comment on column sys_config.config_key is '租户内唯一配置键';
comment on column sys_config.config_value is '配置值';
comment on column sys_config.value_parser is '值转换器';
comment on column sys_config.value_type is '值类型，默认string';
comment on column sys_config.is_default is '是否系统默认配置 1是 0否';
comment on column sys_config.is_template is '是否新租户模板数据 1是 0否，仅system租户有效，复制后置0';
comment on column sys_config.status is '状态 1启用 2停用';
comment on column sys_config.remark is '备注';
comment on column sys_config.create_by is '创建人';
comment on column sys_config.create_time is '创建时间';
comment on column sys_config.update_by is '更新人';
comment on column sys_config.update_time is '更新时间';

-- 3.字典类型
drop table if exists sys_dict_type;
create table sys_dict_type
(
    type_id     serial primary key,
    tenant_id   int4 not null,
    module_code character varying(100),
    type_code   character varying(100) not null,
    type_name   character varying(100) not null,
    status      int2 not null default 1,
    is_template int2 not null default 0,
    remark      character varying(200),
    create_by   character varying(64),
    create_time timestamptz not null default current_timestamp,
    update_by   character varying(64),
    update_time timestamptz
);
create unique index sys_dict_type_code_uq on sys_dict_type(tenant_id, type_code);
create index sys_dict_type_module on sys_dict_type(tenant_id, module_code, status);
comment on table sys_dict_type is '字典类型';
comment on column sys_dict_type.type_id is '类型id';
comment on column sys_dict_type.tenant_id is '租户id';
comment on column sys_dict_type.module_code is '所属模块编码';
comment on column sys_dict_type.type_code is '租户内唯一类型编码';
comment on column sys_dict_type.type_name is '类型名称';
comment on column sys_dict_type.status is '状态 1启用 2停用';
comment on column sys_dict_type.is_template is '是否新租户模板数据 1是 0否，仅system租户有效，复制后置0';
comment on column sys_dict_type.remark is '备注';
comment on column sys_dict_type.create_by is '创建人';
comment on column sys_dict_type.create_time is '创建时间';
comment on column sys_dict_type.update_by is '更新人';
comment on column sys_dict_type.update_time is '更新时间';

-- 4.字典
drop table if exists sys_dict;
create table sys_dict
(
    dict_id      bigserial primary key,
    tenant_id    int4 not null,
    type_code    character varying(100) not null,
    dict_code    character varying(100) not null,
    dict_name    character varying(100) not null,
    dict_value   text,
    value_type   character varying(64) default 'string',
    value_parser character varying(100),
    dict_order   int4 not null default 0,
    is_default   int2 not null default 0,
    css          character varying(100),
    status       int2 not null default 1,
    is_template  int2 not null default 0,
    remark       character varying(200),
    create_by    character varying(64),
    create_time  timestamptz not null default current_timestamp,
    update_by    character varying(64),
    update_time  timestamptz
);
create unique index sys_dict_code_uq on sys_dict(tenant_id, type_code, dict_code);
create unique index sys_dict_default_uq
    on sys_dict(tenant_id, type_code)
    where is_default = 1 and status = 1;
create index sys_dict_type_order on sys_dict(tenant_id, type_code, status, dict_order);
comment on table sys_dict is '字典项';
comment on column sys_dict.dict_id is '字典项id';
comment on column sys_dict.tenant_id is '租户id';
comment on column sys_dict.type_code is '所属类型编码';
comment on column sys_dict.dict_code is '类型内唯一字典项编码';
comment on column sys_dict.dict_name is '字典项名称';
comment on column sys_dict.dict_value is '字典项值';
comment on column sys_dict.value_type is '值类型，默认string';
comment on column sys_dict.value_parser is '值转换器';
comment on column sys_dict.dict_order is '字典项排序';
comment on column sys_dict.is_default is '是否类型内默认项 1是 0否';
comment on column sys_dict.css is '展示样式';
comment on column sys_dict.status is '状态 1启用 2停用';
comment on column sys_dict.is_template is '是否新租户模板数据 1是 0否，仅system租户有效，复制后置0';
comment on column sys_dict.remark is '备注';
comment on column sys_dict.create_by is '创建人';
comment on column sys_dict.create_time is '创建时间';
comment on column sys_dict.update_by is '更新人';
comment on column sys_dict.update_time is '更新时间';

-- 5.系统告警（全局）
drop table if exists sys_alarm;
create table sys_alarm
(
    alarm_id            bigserial primary key,
    alarm_code          character varying(128) not null,
    alarm_type_id       int8 not null,
    alarm_level         int2 not null default 1,
    source_type         character varying(64) not null,
    source_id           character varying(128),
    source_name         character varying(128),
    alarm_status        int2 not null default 0,
    alarm_times         int4 not null default 1,
    first_time          timestamptz not null default current_timestamp,
    last_time           timestamptz not null default current_timestamp,
    alarm_desc          character varying(500),
    alarm_content       jsonb not null default '{}'::jsonb,
    acknowledge_user_id int4,
    acknowledge_msg     character varying(500),
    acknowledge_time    timestamptz,
    resolve_user_id     int4,
    resolve_msg         character varying(500),
    resolve_time        timestamptz,
    resolve_type        int2 not null default 1
);
create unique index sys_alarm_code_uq on sys_alarm(alarm_code);
create index sys_alarm_status on sys_alarm(alarm_status, alarm_level, last_time desc);
create index sys_alarm_source on sys_alarm(source_type, source_id);
comment on table sys_alarm is '全局系统告警';
comment on column sys_alarm.alarm_id is '告警id';
comment on column sys_alarm.alarm_code is '告警唯一编码或聚合指纹';
comment on column sys_alarm.alarm_type_id is '告警类型id';
comment on column sys_alarm.source_id is '告警来源业务标识';
comment on column sys_alarm.source_name is '告警来源名称快照';
comment on column sys_alarm.source_type is '告警来源类型';
comment on column sys_alarm.alarm_level is '告警级别：1提示 2普通 3重要 4严重 5灾难';
comment on column sys_alarm.alarm_status is '告警状态：0未处理 1已确认 2已解决 3已关闭';
comment on column sys_alarm.alarm_times is '同一告警累计发生次数';
comment on column sys_alarm.first_time is '首次告警时间';
comment on column sys_alarm.last_time is '最后告警时间';
comment on column sys_alarm.alarm_desc is '告警描述';
comment on column sys_alarm.alarm_content is '告警扩展内容';
comment on column sys_alarm.acknowledge_user_id is '确认人用户id';
comment on column sys_alarm.acknowledge_msg is '确认说明';
comment on column sys_alarm.acknowledge_time is '确认时间';
comment on column sys_alarm.resolve_user_id is '解决人用户id';
comment on column sys_alarm.resolve_time is '解决时间';
comment on column sys_alarm.resolve_msg is '解决意见';
comment on column sys_alarm.resolve_type is '解决方式：1手动 2自动';

-- 6.告警类型（全局）
drop table if exists sys_alarm_type;
create table sys_alarm_type
(
    alarm_type_id bigserial primary key,
    type_code     character varying(128) not null,
    type_name     character varying(128) not null,
    type_view     character varying(128),
    default_level int2 not null default 1,
    status        int2 not null default 1,
    description   text,
    create_by     character varying(64),
    create_time   timestamptz not null default current_timestamp,
    update_by     character varying(64),
    update_time   timestamptz
);
create unique index sys_alarm_type_code_uq on sys_alarm_type(type_code);
comment on table sys_alarm_type is '全局告警类型';
comment on column sys_alarm_type.alarm_type_id is '告警类型id';
comment on column sys_alarm_type.type_code is '告警类型稳定编码';
comment on column sys_alarm_type.type_name is '类型名称';
comment on column sys_alarm_type.type_view is '类型表单或展示组件';
comment on column sys_alarm_type.default_level is '默认告警级别';
comment on column sys_alarm_type.status is '状态 1启用 2停用';
comment on column sys_alarm_type.description is '类型描述';
comment on column sys_alarm_type.create_by is '创建人';
comment on column sys_alarm_type.create_time is '创建时间';
comment on column sys_alarm_type.update_by is '更新人';
comment on column sys_alarm_type.update_time is '更新时间';

-- 7.系统评分留言
drop table if exists sys_feedback;
create table sys_feedback
(
    feedback_id  bigserial primary key,
    tenant_id    int4 not null,
    user_id      int4 not null,
    user_name    character varying(128),
    feedback_type character varying(32) not null default 'rating',
    score        int2 not null default 5,
    content      text,
    images       jsonb not null default '[]'::jsonb,
    is_anonymous int2 not null default 0,
    status       int2 not null default 1,
    like_count   int4 not null default 0,
    reply_count  int4 not null default 0,
    create_time  timestamptz not null default current_timestamp,
    update_time  timestamptz
);
create index sys_feedback_tenant on sys_feedback(tenant_id, status, create_time desc);
create index sys_feedback_user on sys_feedback(tenant_id, user_id, create_time desc);
comment on table sys_feedback is '系统评分留言';
comment on column sys_feedback.feedback_id is '留言id';
comment on column sys_feedback.tenant_id is '租户id';
comment on column sys_feedback.user_id is '留言用户id';
comment on column sys_feedback.user_name is '留言用户名称快照';
comment on column sys_feedback.feedback_type is '反馈类型，如rating评分、suggestion建议、complaint投诉';
comment on column sys_feedback.score is '评分 1-5，非评分类型可由应用忽略';
comment on column sys_feedback.content is '留言内容';
comment on column sys_feedback.images is '图片信息JSON数组';
comment on column sys_feedback.is_anonymous is '是否匿名展示 1是 0否';
comment on column sys_feedback.status is '状态：1正常 2隐藏 3关闭';
comment on column sys_feedback.like_count is '点赞数冗余统计';
comment on column sys_feedback.reply_count is '评论数冗余统计';
comment on column sys_feedback.create_time is '创建时间';
comment on column sys_feedback.update_time is '更新时间';

-- 8.留言评论（支持嵌套回复）
drop table if exists sys_feedback_comment;
create table sys_feedback_comment
(
    comment_id       bigserial primary key,
    tenant_id        int4 not null,
    feedback_id      int8 not null,
    parent_id        int8 not null default 0,
    reply_to_user_id int4,
    reply_to_name    character varying(128),
    user_id          int4 not null,
    user_name        character varying(128),
    content          text not null,
    status           int2 not null default 1,
    like_count       int4 not null default 0,
    create_time      timestamptz not null default current_timestamp,
    update_time      timestamptz
);
create index sys_feedback_comment_feedback
    on sys_feedback_comment(tenant_id, feedback_id, status, create_time);
create index sys_feedback_comment_parent
    on sys_feedback_comment(tenant_id, parent_id, status, create_time);
comment on table sys_feedback_comment is '留言评论';
comment on column sys_feedback_comment.comment_id is '评论id';
comment on column sys_feedback_comment.tenant_id is '租户id';
comment on column sys_feedback_comment.feedback_id is '留言id';
comment on column sys_feedback_comment.parent_id is '父评论id，0表示顶级评论';
comment on column sys_feedback_comment.reply_to_user_id is '被回复用户id';
comment on column sys_feedback_comment.reply_to_name is '被回复用户名称快照';
comment on column sys_feedback_comment.user_id is '评论用户id';
comment on column sys_feedback_comment.user_name is '评论用户名称快照';
comment on column sys_feedback_comment.content is '评论内容';
comment on column sys_feedback_comment.status is '状态：1正常 2隐藏 3关闭';
comment on column sys_feedback_comment.like_count is '点赞数冗余统计';
comment on column sys_feedback_comment.create_time is '创建时间';
comment on column sys_feedback_comment.update_time is '更新时间';

-- 9.点赞（支持留言和评论）
drop table if exists sys_feedback_like;
create table sys_feedback_like
(
    like_id     bigserial primary key,
    tenant_id   int4 not null,
    target_type int2 not null default 1,
    target_id   int8 not null,
    user_id     int4 not null,
    user_name   character varying(128),
    create_time timestamptz not null default current_timestamp
);
create unique index sys_feedback_like_uq
    on sys_feedback_like(tenant_id, target_type, target_id, user_id);
create index sys_feedback_like_user
    on sys_feedback_like(tenant_id, user_id, create_time desc);
comment on table sys_feedback_like is '点赞记录';
comment on column sys_feedback_like.like_id is '点赞id';
comment on column sys_feedback_like.tenant_id is '租户id';
comment on column sys_feedback_like.target_type is '目标类型：1留言 2评论';
comment on column sys_feedback_like.target_id is '目标id';
comment on column sys_feedback_like.user_id is '点赞用户id';
comment on column sys_feedback_like.user_name is '点赞用户名称快照';
comment on column sys_feedback_like.create_time is '点赞时间';

-- 10.通用附件
drop table if exists sys_attach;
create table sys_attach
(
    attach_id     bigserial primary key,
    tenant_id     int4 not null,
    owner_module  character varying(64),
    owner_id      character varying(64),
    attach_type   character varying(64),
    attach_name   character varying(255) not null,
    file_type     character varying(64),
    content_type  character varying(128),
    attach_size   int8 not null default 0,
    attach_path   character varying(1024) not null,
    storage_type  character varying(32) not null default 'local',
    bucket_name   character varying(128),
    md5           character varying(64),
    is_private    int2 not null default 0,
    sort          int4 not null default 0,
    is_deleted    int2 not null default 0,
    expire_time   timestamptz,
    create_by     character varying(64),
    create_time   timestamptz not null default current_timestamp,
    update_by     character varying(64),
    update_time   timestamptz
);
create index sys_attach_owner
    on sys_attach(tenant_id, owner_module, owner_id, attach_type, is_deleted, sort);
create index sys_attach_md5
    on sys_attach(tenant_id, md5)
    where md5 is not null and is_deleted = 0;
create index sys_attach_expire
    on sys_attach(expire_time)
    where expire_time is not null and is_deleted = 0;
comment on table sys_attach is '通用附件，支持租户、用户、通知、反馈和流程等业务宿主';
comment on column sys_attach.attach_id is '附件id';
comment on column sys_attach.tenant_id is '租户id';
comment on column sys_attach.owner_module is '宿主模块，如tenant、user、notice、feedback、flow';
comment on column sys_attach.owner_id is '宿主业务id，允许上传后再绑定';
comment on column sys_attach.attach_type is '附件业务类型，如logo、avatar、image、document';
comment on column sys_attach.attach_name is '附件名称';
comment on column sys_attach.file_type is '文件扩展名';
comment on column sys_attach.content_type is '文件MIME类型';
comment on column sys_attach.attach_size is '附件大小，单位字节';
comment on column sys_attach.attach_path is '附件存储路径或对象键';
comment on column sys_attach.storage_type is '存储类型，如local、minio';
comment on column sys_attach.bucket_name is '对象存储桶名称';
comment on column sys_attach.md5 is '文件内容MD5';
comment on column sys_attach.is_private is '是否私有 0否 1是';
comment on column sys_attach.sort is '同一宿主下排序';
comment on column sys_attach.is_deleted is '是否删除 0否 1是';
comment on column sys_attach.expire_time is '过期时间，临时上传文件可用于清理';
comment on column sys_attach.create_by is '创建人';
comment on column sys_attach.create_time is '创建时间';
comment on column sys_attach.update_by is '更新人';
comment on column sys_attach.update_time is '更新时间';

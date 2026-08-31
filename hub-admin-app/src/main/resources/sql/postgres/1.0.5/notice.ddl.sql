-- 1.通知主表
drop table if exists sys_notice;
create table sys_notice(
    notice_id        bigserial primary key,
    tenant_id        int4  not null,
    notice_no        varchar(64)  not null,
    title            varchar(255) not null,
    content          text,
    content_format   int2         not null default 1,
    cover            varchar(500),
    notice_module    varchar(64),
    notice_priority  int2         not null default 0,
    type_id          int8,
    template_id      int8,
    template_version int4,
    variables        jsonb,
    channel_ids      int8[] not null default '{}',
    target_scope     jsonb        not null default '{}'::jsonb,
    to_future_members int2          not null default 0,
    sender_id        int4,
    sender_name      varchar(64),
    notice_status    int2         not null default 0,
    target_count     int4         not null default 0,
    receiver_count   int4         not null default 0,
    read_count       int4         not null default 0,
    comment_count    int4         not null default 0,
    like_count       int4         not null default 0,
    dislike_count    int4         not null default 0,
    link_type        int2         not null default 0,
    link_url         varchar(1000),
    schedule_time    timestamptz,
    publish_time     timestamptz,
    revoke_time      timestamptz,
    expire_time      timestamptz,
    is_deleted       int2         not null default 0,
    create_by        varchar(64),
    create_time      timestamptz  not null default current_timestamp,
    update_by        varchar(64),
    update_time      timestamptz
);
create unique index uk_notice_tenant_no on sys_notice(tenant_id, notice_no);
create index idx_notice_tenant_schedule on sys_notice(tenant_id, notice_status, schedule_time);
create index idx_notice_tenant_category on sys_notice(tenant_id, type_id);
create index idx_notice_channel_ids on sys_notice using gin(channel_ids);
create index idx_notice_target_scope on sys_notice using gin(target_scope);
comment on table sys_notice is '系统通知主表';
comment on column sys_notice.notice_id is '通知id';
comment on column sys_notice.tenant_id is '租户id';
comment on column sys_notice.notice_no is '通知业务编号，用于幂等和外部追踪';
comment on column sys_notice.title is '通知标题';
comment on column sys_notice.content is '通知内容';
comment on column sys_notice.content_format is '内容格式(1纯文本 2HTML 3Markdown 4富文本)';
comment on column sys_notice.cover is '通知封面';
comment on column sys_notice.notice_module is '业务模块标识';
comment on column sys_notice.notice_priority is '优先级(0普通 1重要 2紧急)';
comment on column sys_notice.type_id is '分类id';
comment on column sys_notice.template_id is '模板id';
comment on column sys_notice.template_version is '发布时使用的模板版本';
comment on column sys_notice.variables is '模板渲染变量(JSON)';
comment on column sys_notice.channel_ids is '发送渠道id数组';
comment on column sys_notice.target_scope is '发送目标(JSON)：all、userIds、deptIds、roleIds、groupIds，可组合选择';
comment on column sys_notice.to_future_members is '是否补发给发布后新进入目标范围的成员(0否 1是)';
comment on column sys_notice.sender_id is '发送人id';
comment on column sys_notice.sender_name is '发送人名称';
comment on column sys_notice.notice_status is '业务状态(0草稿 1待发布 2发布中 3已发布 4已撤回 5已过期 6发布失败)';
comment on column sys_notice.target_count is '原始发送目标数量';
comment on column sys_notice.receiver_count is '展开去重后的实际接收人数';
comment on column sys_notice.read_count is '已读人数统计';
comment on column sys_notice.comment_count is '评论数统计';
comment on column sys_notice.like_count is '点赞数统计';
comment on column sys_notice.dislike_count is '差评数统计';
comment on column sys_notice.link_type is '跳转类型(0无 1内部路由 2外部URL 3业务详情)';
comment on column sys_notice.link_url is '跳转地址';
comment on column sys_notice.schedule_time is '定时发送时间';
comment on column sys_notice.publish_time is '实际发布时间';
comment on column sys_notice.revoke_time is '撤回时间';
comment on column sys_notice.expire_time is '过期时间';
comment on column sys_notice.is_deleted is '是否删除(0否 1是)';
comment on column sys_notice.create_by is '创建人';
comment on column sys_notice.create_time is '创建时间';
comment on column sys_notice.update_by is '更新人';
comment on column sys_notice.update_time is '更新时间';

-- 2.通知分类
drop table if exists sys_notice_type;
create table sys_notice_type(
    type_id     bigserial primary key,
    tenant_id   int4,
    type_code   varchar(64),
    type_name   varchar(64) not null,
    parent_id   int8 default 0,
    sort        int4 default 0,
    status      int2 default 1,
    is_deleted  int2 default 0,
    create_by   varchar(64),
    create_time timestamptz not null default current_timestamp,
    update_by   varchar(64),
    update_time timestamptz
);
create unique index uk_notice_type_tenant_code on sys_notice_type(tenant_id, type_code);
comment on table sys_notice_type is '通知分类表';
comment on column sys_notice_type.type_id is '分类id';
comment on column sys_notice_type.tenant_id is '租户id';
comment on column sys_notice_type.type_code is '分类编码';
comment on column sys_notice_type.type_name is '分类名称';
comment on column sys_notice_type.parent_id is '父级id,0为顶级';
comment on column sys_notice_type.sort is '排序';
comment on column sys_notice_type.status is '状态(0停用 1启用)';
comment on column sys_notice_type.is_deleted is '是否删除(0否 1是)';
comment on column sys_notice_type.create_by is '创建人';
comment on column sys_notice_type.create_time is '创建时间';
comment on column sys_notice_type.update_by is '更新人';
comment on column sys_notice_type.update_time is '更新时间';

-- 3.通知渠道
drop table if exists sys_notice_channel;
create table sys_notice_channel(
    channel_id    bigserial primary key,
    tenant_id     int4,
    channel_code  varchar(64),
    channel_name  varchar(64) not null,
    channel_type  int2 default 1,
    config_json   jsonb,
    status        int2 default 1,
    remark        varchar(255),
    is_deleted    int2 default 0,
    create_by     varchar(64),
    create_time   timestamptz not null default current_timestamp,
    update_by     varchar(64),
    update_time   timestamptz
);
create unique index uk_notice_channel_tenant_code on sys_notice_channel(tenant_id, channel_code);
comment on table sys_notice_channel is '通知发送渠道配置表';
comment on column sys_notice_channel.channel_id is '渠道id';
comment on column sys_notice_channel.tenant_id is '租户id';
comment on column sys_notice_channel.channel_code is '渠道编码';
comment on column sys_notice_channel.channel_name is '渠道名称';
comment on column sys_notice_channel.channel_type is '渠道类型(1邮箱 2钉钉 3企微)';
comment on column sys_notice_channel.config_json is '渠道配置(JSON)';
comment on column sys_notice_channel.status is '状态(0停用 1启用)';
comment on column sys_notice_channel.remark is '备注';
comment on column sys_notice_channel.is_deleted is '是否删除(0否 1是)';
comment on column sys_notice_channel.create_by is '创建人';
comment on column sys_notice_channel.create_time is '创建时间';
comment on column sys_notice_channel.update_by is '更新人';
comment on column sys_notice_channel.update_time is '更新时间';

-- 4.通知模板
drop table if exists sys_notice_template;
create table sys_notice_template(
    template_id    bigserial primary key,
    tenant_id      int4,
    template_code  varchar(64) not null,
    title          varchar(255) not null,
    content        text not null,
    category_id    int8,
    channel_type   int2,
    variables      jsonb,
    lang           varchar(16) default 'zh-CN',
    version        int4 default 1,
    status         int2 default 1,
    remark         varchar(255),
    is_deleted     int2 default 0,
    create_by      varchar(64),
    create_time    timestamptz not null default current_timestamp,
    update_by      varchar(64),
    update_time    timestamptz
);
create unique index uk_notice_template_tenant_code_lang_ver
    on sys_notice_template(tenant_id, template_code, lang, version);
comment on table sys_notice_template is '通知模板表';
comment on column sys_notice_template.template_id is '模板id';
comment on column sys_notice_template.tenant_id is '租户id';
comment on column sys_notice_template.template_code is '模板编码';
comment on column sys_notice_template.title is '模板标题';
comment on column sys_notice_template.content is '模板内容';
comment on column sys_notice_template.category_id is '分类id';
comment on column sys_notice_template.channel_type is '适用渠道类型(1邮箱 2钉钉 3企微,空为通用)';
comment on column sys_notice_template.variables is '变量定义(JSON)';
comment on column sys_notice_template.lang is '语言';
comment on column sys_notice_template.version is '版本号';
comment on column sys_notice_template.status is '状态(0停用 1启用)';
comment on column sys_notice_template.remark is '备注';
comment on column sys_notice_template.is_deleted is '是否删除(0否 1是)';
comment on column sys_notice_template.create_by is '创建人';
comment on column sys_notice_template.create_time is '创建时间';
comment on column sys_notice_template.update_by is '更新人';
comment on column sys_notice_template.update_time is '更新时间';

-- 5.通知接收
drop table if exists sys_notice_receiver;
create table sys_notice_receiver(
    receiver_id    bigserial primary key,
    tenant_id      int4 not null,
    notice_id      int8 not null,
    user_id        int4 not null,
    user_name      varchar(128),
    receive_status int2 not null default 1,
    read_status    int2 not null default 0,
    read_time      timestamptz,
    click_status   int2 not null default 0,
    click_time     timestamptz,
    archive_status int2 not null default 0,
    archive_time   timestamptz,
    delete_status  int2 not null default 0,
    delete_time    timestamptz,
    action_status  int2 not null default 0,
    action_time    timestamptz,
    create_time    timestamptz not null default current_timestamp,
    update_time    timestamptz
);
create unique index uk_notice_receiver_tenant_notice_user
    on sys_notice_receiver(tenant_id, notice_id, user_id);
create index idx_notice_receiver_unread
    on sys_notice_receiver(tenant_id, user_id, read_status, delete_status, create_time desc);
comment on table sys_notice_receiver is '通知实际接收人表';
comment on column sys_notice_receiver.receiver_id is '主键';
comment on column sys_notice_receiver.tenant_id is '租户id';
comment on column sys_notice_receiver.notice_id is '通知id';
comment on column sys_notice_receiver.user_id is '接收用户id';
comment on column sys_notice_receiver.user_name is '接收用户名称快照';
comment on column sys_notice_receiver.receive_status is '接收状态(0生成失败 1已生成 2可送达)';
comment on column sys_notice_receiver.read_status is '阅读状态(0未读 1已读)';
comment on column sys_notice_receiver.read_time is '阅读时间';
comment on column sys_notice_receiver.click_status is '点击状态(0未点击 1已点击)';
comment on column sys_notice_receiver.click_time is '首次点击时间';
comment on column sys_notice_receiver.archive_status is '归档状态(0未归档 1已归档)';
comment on column sys_notice_receiver.archive_time is '归档时间';
comment on column sys_notice_receiver.delete_status is '用户侧删除状态(0未删除 1已删除)';
comment on column sys_notice_receiver.delete_time is '用户侧删除时间';
comment on column sys_notice_receiver.action_status is '业务处理状态(0未处理 1已处理)';
comment on column sys_notice_receiver.action_time is '业务处理时间';
comment on column sys_notice_receiver.create_time is '创建时间';
comment on column sys_notice_receiver.update_time is '更新时间';

-- 6.通知评论
drop table if exists sys_notice_comment;
create table sys_notice_comment(
    comment_id         bigserial primary key,
    tenant_id          int4,
    notice_id          int8 not null,
    parent_id          int8 default 0,
    root_id            int8 default 0,
    level              int2 default 1,
    comment_user_id    int4 not null,
    comment_user_name  varchar(64),
    reply_user_id      int4,
    reply_user_name    varchar(64),
    content            text not null,
    like_count         int4 default 0,
    dislike_count      int4 default 0,
    status             int2 default 1,
    is_deleted         int2 default 0,
    create_by          varchar(64),
    create_time        timestamptz not null default current_timestamp,
    update_by          varchar(64),
    update_time        timestamptz
);
create index idx_notice_comment_tenant_parent on sys_notice_comment(tenant_id, parent_id);
create index idx_notice_comment_tenant_root   on sys_notice_comment(tenant_id, root_id);
comment on table sys_notice_comment is '通知评论表';
comment on column sys_notice_comment.comment_id is '评论id';
comment on column sys_notice_comment.tenant_id is '租户id';
comment on column sys_notice_comment.notice_id is '通知id';
comment on column sys_notice_comment.parent_id is '上级评论id,0为顶级';
comment on column sys_notice_comment.root_id is '根评论id';
comment on column sys_notice_comment.level is '层级';
comment on column sys_notice_comment.comment_user_id is '评论人id';
comment on column sys_notice_comment.comment_user_name is '评论人昵称';
comment on column sys_notice_comment.reply_user_id is '回复人id';
comment on column sys_notice_comment.reply_user_name is '回复人昵称';
comment on column sys_notice_comment.content is '评论内容';
comment on column sys_notice_comment.like_count is '点赞数统计';
comment on column sys_notice_comment.dislike_count is '差评数统计';
comment on column sys_notice_comment.status is '状态(0屏蔽 1正常)';
comment on column sys_notice_comment.is_deleted is '是否删除(0否 1是)';
comment on column sys_notice_comment.create_by is '创建人';
comment on column sys_notice_comment.create_time is '创建时间';
comment on column sys_notice_comment.update_by is '更新人';
comment on column sys_notice_comment.update_time is '更新时间';

-- 7.通知/评论评价
drop table if exists sys_notice_reaction;
create table sys_notice_reaction(
    reaction_id   bigserial primary key,
    tenant_id     int4 not null,
    target_type   int2 not null,
    target_id     int8 not null,
    notice_id     int8,
    reaction_type int2 not null,
    user_id       int4 not null,
    user_name     varchar(64),
    create_time   timestamptz not null default current_timestamp,
    update_time   timestamptz
);
create unique index uk_notice_reaction_tenant_target_user
    on sys_notice_reaction(tenant_id, target_type, target_id, user_id);
create index idx_notice_reaction_tenant_notice on sys_notice_reaction(tenant_id, notice_id);
create index idx_notice_reaction_tenant_user   on sys_notice_reaction(tenant_id, user_id);
comment on table sys_notice_reaction is '通知/评论评价表';
comment on column sys_notice_reaction.reaction_id is '评价id';
comment on column sys_notice_reaction.tenant_id is '租户id';
comment on column sys_notice_reaction.target_type is '目标类型(1通知 2评论)';
comment on column sys_notice_reaction.target_id is '目标id';
comment on column sys_notice_reaction.notice_id is '通知id(冗余,便于按通知统计)';
comment on column sys_notice_reaction.reaction_type is '评价类型(1点赞 2差评)';
comment on column sys_notice_reaction.user_id is '评价用户id';
comment on column sys_notice_reaction.user_name is '评价用户昵称';
comment on column sys_notice_reaction.create_time is '首次评价时间';
comment on column sys_notice_reaction.update_time is '评价更新时间';

-- 8.通知渠道投递
drop table if exists sys_notice_delivery;
create table sys_notice_delivery(
    delivery_id      bigserial primary key,
    tenant_id        int4 not null,
    notice_id        int8 not null,
    receiver_id      int8 not null,
    channel_id       int8 not null,
    channel_type     int2 not null,
    delivery_status  int2 not null default 0,
    retry_count      int4 not null default 0,
    next_retry_time  timestamptz,
    send_time        timestamptz,
    success_time     timestamptz,
    error_code       varchar(128),
    error_message    varchar(2000),
    response         jsonb,
    create_time      timestamptz not null default current_timestamp,
    update_time      timestamptz
);
create unique index uk_notice_delivery
    on sys_notice_delivery(notice_id, receiver_id, channel_id);
create index idx_notice_delivery_pending
    on sys_notice_delivery(tenant_id, delivery_status, next_retry_time);
comment on table sys_notice_delivery is '通知渠道投递表';
comment on column sys_notice_delivery.delivery_id is '投递记录id';
comment on column sys_notice_delivery.tenant_id is '租户id';
comment on column sys_notice_delivery.notice_id is '通知id';
comment on column sys_notice_delivery.receiver_id is '实际接收记录id';
comment on column sys_notice_delivery.channel_id is '渠道id';
comment on column sys_notice_delivery.channel_type is '渠道类型快照';
comment on column sys_notice_delivery.delivery_status is '投递状态(0待发送 1发送中 2成功 3失败 4重试中 5放弃)';
comment on column sys_notice_delivery.retry_count is '已重试次数';
comment on column sys_notice_delivery.next_retry_time is '下次重试时间';
comment on column sys_notice_delivery.send_time is '最近一次发送时间';
comment on column sys_notice_delivery.success_time is '发送成功时间';
comment on column sys_notice_delivery.error_code is '失败错误码';
comment on column sys_notice_delivery.error_message is '失败原因';
comment on column sys_notice_delivery.response is '渠道响应(JSON)';
comment on column sys_notice_delivery.create_time is '创建时间';
comment on column sys_notice_delivery.update_time is '更新时间';

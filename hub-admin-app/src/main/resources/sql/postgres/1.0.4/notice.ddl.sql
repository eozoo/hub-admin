-- 消息评论
drop table if exists hub_notice_comment;
create table hub_notice_comment
(
    id            bigserial primary key,
    notice_id     int8,
    parent_id     int8 default 0,
    reply_to_code character varying(64),
    reply_to_name character varying(64),
    user_code     character varying(64),
    user_name     character varying(64),
    content       character varying(1000),
    like_count    int4 default 0,
    create_time   timestamp
);
comment on table hub_notice_comment is '消息评论';
comment on column hub_notice_comment.id is '主键';
comment on column hub_notice_comment.notice_id is '消息id';
comment on column hub_notice_comment.parent_id is '父评论id，0表示顶级评论';
comment on column hub_notice_comment.reply_to_code is '被回复用户账号';
comment on column hub_notice_comment.reply_to_name is '被回复用户名称';
comment on column hub_notice_comment.user_code is '评论者账号';
comment on column hub_notice_comment.user_name is '评论者名称';
comment on column hub_notice_comment.content is '评论内容';
comment on column hub_notice_comment.like_count is '点赞数';
comment on column hub_notice_comment.create_time is '创建时间';

-- 消息点赞（支持消息点赞和评论点赞）
drop table if exists hub_notice_like;
create table hub_notice_like
(
    id          bigserial primary key,
    target_type int2,
    target_id   int8,
    user_code   character varying(64),
    user_name   character varying(64),
    create_time timestamp
);
create unique index hub_notice_like_uk on hub_notice_like(target_type, target_id, user_code);
comment on table hub_notice_like is '消息点赞';
comment on column hub_notice_like.id is '主键';
comment on column hub_notice_like.target_type is '目标类型：1消息 2评论';
comment on column hub_notice_like.target_id is '目标id';
comment on column hub_notice_like.user_code is '用户账号';
comment on column hub_notice_like.user_name is '用户名称';
comment on column hub_notice_like.create_time is '点赞时间';

-- hub_notice_user 新增列，删除废弃列
alter table hub_notice_user add column if not exists like_status int2 default 0;
alter table hub_notice_user add column if not exists comment_count int4 default 0;
comment on column hub_notice_user.like_status is '好评差评：0未操作 1好评 2差评';
comment on column hub_notice_user.comment_count is '评论数';

alter table hub_notice_user drop column if exists read_back;

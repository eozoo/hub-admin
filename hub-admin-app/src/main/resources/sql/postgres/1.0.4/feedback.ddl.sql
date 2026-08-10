-- 系统评分留言
drop table if exists hub_feedback;
create table hub_feedback
(
    id          bigserial primary key,
    tenant_id   character varying(64),
    user_code   character varying(64),
    user_name   character varying(128),
    score       int2 default 5,
    content     character varying(2000),
    like_count  int4 default 0,
    reply_count int4 default 0,
    images      character varying(2000),
    create_time timestamp,
    update_time timestamp
);
comment on table hub_feedback is '系统评分留言';
comment on column hub_feedback.id is '主键';
comment on column hub_feedback.tenant_id is '租户id';
comment on column hub_feedback.user_code is '用户账号';
comment on column hub_feedback.user_name is '用户名称';
comment on column hub_feedback.score is '评分 1-5';
comment on column hub_feedback.content is '留言内容';
comment on column hub_feedback.like_count is '点赞数';
comment on column hub_feedback.reply_count is '评论数';
comment on column hub_feedback.images is '图片url列表，逗号分隔';
comment on column hub_feedback.create_time is '创建时间';
comment on column hub_feedback.update_time is '更新时间';

-- 留言评论（支持嵌套回复）
drop table if exists hub_feedback_comment;
create table hub_feedback_comment
(
    id             bigserial primary key,
    feedback_id    bigint,
    parent_id      bigint      default 0,
    reply_to_code  character varying(64),
    reply_to_name  character varying(128),
    user_code      character varying(64),
    user_name      character varying(128),
    content        character varying(1000),
    like_count     int4 default 0,
    create_time    timestamp
);
comment on table hub_feedback_comment is '留言评论';
comment on column hub_feedback_comment.id is '主键';
comment on column hub_feedback_comment.feedback_id is '留言id';
comment on column hub_feedback_comment.parent_id is '父评论id，0表示顶级评论';
comment on column hub_feedback_comment.reply_to_code is '被回复用户账号';
comment on column hub_feedback_comment.reply_to_name is '被回复用户名称';
comment on column hub_feedback_comment.user_code is '评论者账号';
comment on column hub_feedback_comment.user_name is '评论者名称';
comment on column hub_feedback_comment.content is '评论内容';
comment on column hub_feedback_comment.like_count is '点赞数';
comment on column hub_feedback_comment.create_time is '创建时间';

-- 点赞（支持留言和评论）
drop table if exists hub_feedback_like;
create table hub_feedback_like
(
    id          bigserial primary key,
    target_type int2        default 1,
    target_id   bigint,
    user_code   character varying(64),
    user_name   character varying(128),
    create_time timestamp
);
create unique index hub_feedback_like_uk on hub_feedback_like(target_type, target_id, user_code);
comment on table hub_feedback_like is '点赞记录';
comment on column hub_feedback_like.id is '主键';
comment on column hub_feedback_like.target_type is '目标类型 1-留言 2-评论';
comment on column hub_feedback_like.target_id is '目标id';
comment on column hub_feedback_like.user_code is '点赞用户账号';
comment on column hub_feedback_like.user_name is '点赞用户名称';
comment on column hub_feedback_like.create_time is '点赞时间';

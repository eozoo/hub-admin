-- 系统评分留言
drop table if exists sys_feedback;
create table sys_feedback
(
    id          bigint auto_increment primary key comment '主键',
    tenant_id   varchar(64) comment '租户id',
    user_code   varchar(64) comment '用户账号',
    user_name   varchar(128) comment '用户名称',
    score       smallint default 5 comment '评分 1-5',
    content     varchar(2000) comment '留言内容',
    like_count  int default 0 comment '点赞数',
    reply_count int default 0 comment '评论数',
    images      varchar(2000) comment '图片url列表，逗号分隔',
    create_time datetime comment '创建时间',
    update_time datetime comment '更新时间'
) comment='系统评分留言';

-- 留言评论（支持嵌套回复）
drop table if exists sys_feedback_comment;
create table sys_feedback_comment
(
    id             bigint auto_increment primary key comment '主键',
    feedback_id    bigint comment '留言id',
    parent_id      bigint default 0 comment '父评论id，0表示顶级评论',
    reply_to_code  varchar(64) comment '被回复用户账号',
    reply_to_name  varchar(128) comment '被回复用户名称',
    user_code      varchar(64) comment '评论者账号',
    user_name      varchar(128) comment '评论者名称',
    content        varchar(1000) comment '评论内容',
    like_count     int default 0 comment '点赞数',
    create_time    datetime comment '创建时间'
) comment='留言评论';

-- 点赞（支持留言和评论）
drop table if exists sys_feedback_like;
create table sys_feedback_like
(
    id          bigint auto_increment primary key comment '主键',
    target_type smallint default 1 comment '目标类型 1-留言 2-评论',
    target_id   bigint comment '目标id',
    user_code   varchar(64) comment '点赞用户账号',
    user_name   varchar(128) comment '点赞用户名称',
    create_time datetime comment '点赞时间'
) comment='点赞记录';
create unique index sys_feedback_like_uk on sys_feedback_like(target_type, target_id, user_code);

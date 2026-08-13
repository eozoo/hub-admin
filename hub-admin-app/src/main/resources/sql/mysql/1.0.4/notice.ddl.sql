-- 消息评论
drop table if exists sys_notice_comment;
create table sys_notice_comment
(
    id            bigint auto_increment primary key comment '主键',
    notice_id     bigint comment '消息id',
    parent_id     bigint default 0 comment '父评论id，0表示顶级评论',
    reply_to_code varchar(64) comment '被回复用户账号',
    reply_to_name varchar(64) comment '被回复用户名称',
    user_code     varchar(64) comment '评论者账号',
    user_name     varchar(64) comment '评论者名称',
    content       varchar(1000) comment '评论内容',
    like_count    int default 0 comment '点赞数',
    create_time   datetime comment '创建时间'
) comment='消息评论';

-- 消息点赞（支持消息点赞和评论点赞）
drop table if exists sys_notice_like;
create table sys_notice_like
(
    id          bigint auto_increment primary key comment '主键',
    target_type tinyint comment '目标类型：1消息 2评论',
    target_id   bigint comment '目标id',
    user_code   varchar(64) comment '用户账号',
    user_name   varchar(64) comment '用户名称',
    create_time datetime comment '点赞时间',
    unique key sys_notice_like_uk (target_type, target_id, user_code)
) comment='消息点赞';

-- sys_notice_user 新增列，删除废弃列
alter table sys_notice_user
    add column if not exists like_status tinyint default 0 comment '好评差评：0未操作 1好评 2差评',
    add column if not exists comment_count int default 0 comment '评论数',
    drop column if exists read_back;

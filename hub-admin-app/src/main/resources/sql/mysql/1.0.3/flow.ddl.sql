-- 请假申请
drop table if exists flow_leave;
create table flow_leave
(
    id             varchar(64) primary key comment 'id',
    leave_type     int comment '请假类型',
    reason         varchar(512) comment '请假原因',
    begin_time     datetime comment '开始时间',
    end_time       datetime comment '结束时间',
    apply_user     varchar(64) comment '发起人',
    apply_time     datetime comment '发起时间',
    process_id     varchar(64) comment '流程id',
    process_status int default 1 comment '流程状态'
) comment='请假申请';

-- 会议预约
drop table if exists flow_meeting;
create table flow_meeting
(
    id             varchar(64) primary key comment 'id',
    meeting_topic  varchar(128) comment '会议主题',
    meeting_room   varchar(64) comment '会议室',
    members        text comment '会议成员',
    content        text comment '会议纪要',
    begin_time     datetime comment '开始时间',
    end_time       datetime comment '结束时间',
    apply_user     varchar(64) comment '发起人',
    apply_time     datetime comment '发起时间',
    process_id     varchar(64) comment '流程id',
    process_status int default 1 comment '流程状态'
) comment='会议预约';

-- 采购申请
drop table if exists flow_purchase;
create table flow_purchase
(
    id             varchar(64) primary key comment 'id',
    content        text comment '采购内容',
    money          decimal(10,2) comment '总金额',
    apply_user     varchar(64) comment '申请人',
    apply_time     datetime comment '申请时间',
    process_id     varchar(64) comment '流程id',
    process_status int default 1 comment '流程状态'
) comment='采购申请';

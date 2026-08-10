-- 请假申请
drop table if exists flow_leave;
create table flow_leave
(
    id             varchar(64) primary key,
    leave_type     int4,
    reason         varchar(512),
    begin_time     timestamp,
    end_time       timestamp,
    apply_user     varchar(64),
    apply_time     timestamp,
    process_id     varchar(64),
    process_status int4 default 1
);
comment on table flow_leave is '请假申请';
comment on column flow_leave.id is 'id';
comment on column flow_leave.leave_type is '请假类型';
comment on column flow_leave.reason is '请假原因';
comment on column flow_leave.begin_time is '开始时间';
comment on column flow_leave.end_time is '结束时间';
comment on column flow_leave.apply_user is '发起人';
comment on column flow_leave.apply_time is '发起时间';
comment on column flow_leave.process_id is '流程id';
comment on column flow_leave.process_status is '流程状态';

-- 会议预约
drop table if exists flow_meeting;
create table flow_meeting
(
    id             varchar(64) primary key,
    meeting_topic  varchar(128),
    meeting_room   varchar(64),
    members        varchar(64)[],
    content        text,
    begin_time     timestamp,
    end_time       timestamp,
    apply_user     varchar(64),
    apply_time     timestamp,
    process_id     varchar(64),
    process_status int4 default 1
);
comment on table flow_meeting is '会议预约';
comment on column flow_meeting.id is 'id';
comment on column flow_meeting.meeting_topic is '会议主题';
comment on column flow_meeting.meeting_room is '会议室';
comment on column flow_meeting.members is '会议成员';
comment on column flow_meeting.content is '会议纪要';
comment on column flow_meeting.begin_time is '开始时间';
comment on column flow_meeting.end_time is '结束时间';
comment on column flow_meeting.apply_user is '发起人';
comment on column flow_leave.apply_time is '发起时间';
comment on column flow_leave.process_id is '流程id';
comment on column flow_leave.process_status is '流程状态';

-- 采购申请
drop table if exists flow_purchase;
create table flow_purchase
(
    id             varchar(64) primary key,
    content        text,
    money          numeric(10,2),
    apply_user     varchar(64),
    apply_time     timestamp,
    process_id     varchar(64),
    process_status int4 default 1
);
comment on table flow_purchase is '采购申请';
comment on column flow_purchase.id is 'id';
comment on column flow_purchase.content is '采购内容';
comment on column flow_purchase.money is '总金额';
comment on column flow_purchase.apply_user is '申请人';
comment on column flow_purchase.apply_time is '申请时间';
comment on column flow_purchase.process_id is '流程id';
comment on column flow_purchase.process_status is '流程状态';

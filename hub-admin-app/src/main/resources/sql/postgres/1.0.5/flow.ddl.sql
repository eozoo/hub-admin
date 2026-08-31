-- 流程业务表通用约定：
-- process_status：0草稿 1审批中 2已通过 3已驳回 4已撤销 5已终止
-- 流程引擎运行及历史数据由Flowable表维护，此处只保存业务数据和关键结果快照。

-- 1.请假申请
drop table if exists flow_leave;
create table flow_leave
(
    leave_id            character varying(64) primary key,
    tenant_id           int4 not null,
    leave_type          int2 not null,
    reason              text not null,
    begin_time          timestamptz not null,
    end_time            timestamptz not null,
    leave_duration      numeric(10, 2),
    duration_unit       character varying(16) not null default 'day',
    apply_user_id       int4 not null,
    apply_user_name     character varying(128) not null,
    apply_dept_id       int4,
    apply_dept_name     character varying(128),
    apply_time          timestamptz not null default current_timestamp,
    process_instance_id character varying(64),
    process_status      int2 not null default 1,
    finish_time         timestamptz,
    cancel_reason       character varying(500),
    update_by           character varying(64),
    update_time         timestamptz
);
create unique index flow_leave_process_uq
    on flow_leave(process_instance_id)
    where process_instance_id is not null;
create index flow_leave_tenant_status
    on flow_leave(tenant_id, process_status, apply_time desc);
create index flow_leave_user_time
    on flow_leave(tenant_id, apply_user_id, begin_time, end_time);
comment on table flow_leave is '请假申请';
comment on column flow_leave.leave_id is '请假申请业务id，同时作为流程业务键';
comment on column flow_leave.tenant_id is '租户id';
comment on column flow_leave.leave_type is '请假类型';
comment on column flow_leave.reason is '请假原因';
comment on column flow_leave.begin_time is '请假开始时间';
comment on column flow_leave.end_time is '请假结束时间';
comment on column flow_leave.leave_duration is '请假时长，由业务日历计算';
comment on column flow_leave.duration_unit is '时长单位，如day、hour';
comment on column flow_leave.apply_user_id is '申请人用户id';
comment on column flow_leave.apply_user_name is '申请人名称快照';
comment on column flow_leave.apply_dept_id is '申请时所属部门id';
comment on column flow_leave.apply_dept_name is '申请时所属部门名称快照';
comment on column flow_leave.apply_time is '申请时间';
comment on column flow_leave.process_instance_id is '流程实例id';
comment on column flow_leave.process_status is '审批状态：0草稿 1审批中 2已通过 3已驳回 4已撤销 5已终止';
comment on column flow_leave.finish_time is '审批结束时间';
comment on column flow_leave.cancel_reason is '撤销或终止原因';
comment on column flow_leave.update_by is '更新人';
comment on column flow_leave.update_time is '更新时间';

-- 2.会议预约
drop table if exists flow_meeting;
create table flow_meeting
(
    meeting_id          character varying(64) primary key,
    tenant_id           int4 not null,
    meeting_topic       character varying(256) not null,
    meeting_room_id     int4,
    meeting_room_name   character varying(128),
    member_user_ids     int4[] not null default '{}',
    meeting_agenda      text,
    meeting_minutes     text,
    online_url          character varying(1024),
    begin_time          timestamptz not null,
    end_time            timestamptz not null,
    apply_user_id       int4 not null,
    apply_user_name     character varying(128) not null,
    apply_dept_id       int4,
    apply_dept_name     character varying(128),
    apply_time          timestamptz not null default current_timestamp,
    process_instance_id character varying(64),
    process_status      int2 not null default 1,
    meeting_status      int2 not null default 0,
    finish_time         timestamptz,
    cancel_reason       character varying(500),
    update_by           character varying(64),
    update_time         timestamptz
);
create unique index flow_meeting_process_uq
    on flow_meeting(process_instance_id)
    where process_instance_id is not null;
create index flow_meeting_tenant_status
    on flow_meeting(tenant_id, process_status, meeting_status, apply_time desc);
create index flow_meeting_room_time
    on flow_meeting(tenant_id, meeting_room_id, begin_time, end_time)
    where meeting_room_id is not null and meeting_status <> 4;
create index flow_meeting_member
    on flow_meeting using gin(member_user_ids);
comment on table flow_meeting is '会议预约';
comment on column flow_meeting.meeting_id is '会议预约业务id，同时作为流程业务键';
comment on column flow_meeting.tenant_id is '租户id';
comment on column flow_meeting.meeting_topic is '会议主题';
comment on column flow_meeting.meeting_room_id is '会议室id';
comment on column flow_meeting.meeting_room_name is '会议室名称快照';
comment on column flow_meeting.member_user_ids is '参会用户id数组';
comment on column flow_meeting.meeting_agenda is '会议议程';
comment on column flow_meeting.meeting_minutes is '会议纪要';
comment on column flow_meeting.online_url is '线上会议地址';
comment on column flow_meeting.begin_time is '会议开始时间';
comment on column flow_meeting.end_time is '会议结束时间';
comment on column flow_meeting.apply_user_id is '申请人用户id';
comment on column flow_meeting.apply_user_name is '申请人名称快照';
comment on column flow_meeting.apply_dept_id is '申请时所属部门id';
comment on column flow_meeting.apply_dept_name is '申请时所属部门名称快照';
comment on column flow_meeting.apply_time is '申请时间';
comment on column flow_meeting.process_instance_id is '流程实例id';
comment on column flow_meeting.process_status is '审批状态：0草稿 1审批中 2已通过 3已驳回 4已撤销 5已终止';
comment on column flow_meeting.meeting_status is '会议状态：0待审批 1待开始 2进行中 3已结束 4已取消';
comment on column flow_meeting.finish_time is '审批结束时间';
comment on column flow_meeting.cancel_reason is '撤销、终止或取消原因';
comment on column flow_meeting.update_by is '更新人';
comment on column flow_meeting.update_time is '更新时间';

-- 3.采购申请
drop table if exists flow_purchase;
create table flow_purchase
(
    purchase_id         character varying(64) primary key,
    tenant_id           int4 not null,
    purchase_title      character varying(256) not null,
    content             text,
    purchase_items      jsonb not null default '[]'::jsonb,
    total_amount        numeric(18, 2) not null default 0,
    currency_code       character varying(3) not null default 'CNY',
    apply_user_id       int4 not null,
    apply_user_name     character varying(128) not null,
    apply_dept_id       int4,
    apply_dept_name     character varying(128),
    apply_time          timestamptz not null default current_timestamp,
    process_instance_id character varying(64),
    process_status      int2 not null default 1,
    purchase_status     int2 not null default 0,
    finish_time         timestamptz,
    cancel_reason       character varying(500),
    update_by           character varying(64),
    update_time         timestamptz
);
create unique index flow_purchase_process_uq
    on flow_purchase(process_instance_id)
    where process_instance_id is not null;
create index flow_purchase_tenant_status
    on flow_purchase(tenant_id, process_status, purchase_status, apply_time desc);
create index flow_purchase_user
    on flow_purchase(tenant_id, apply_user_id, apply_time desc);
comment on table flow_purchase is '采购申请';
comment on column flow_purchase.purchase_id is '采购申请业务id，同时作为流程业务键';
comment on column flow_purchase.tenant_id is '租户id';
comment on column flow_purchase.purchase_title is '采购主题';
comment on column flow_purchase.content is '采购说明';
comment on column flow_purchase.purchase_items is '采购明细JSON数组';
comment on column flow_purchase.total_amount is '采购总金额';
comment on column flow_purchase.currency_code is 'ISO 4217币种代码';
comment on column flow_purchase.apply_user_id is '申请人用户id';
comment on column flow_purchase.apply_user_name is '申请人名称快照';
comment on column flow_purchase.apply_dept_id is '申请时所属部门id';
comment on column flow_purchase.apply_dept_name is '申请时所属部门名称快照';
comment on column flow_purchase.apply_time is '申请时间';
comment on column flow_purchase.process_instance_id is '流程实例id';
comment on column flow_purchase.process_status is '审批状态：0草稿 1审批中 2已通过 3已驳回 4已撤销 5已终止';
comment on column flow_purchase.purchase_status is '采购状态：0待审批 1待采购 2待付款 3待收货 4已收货 5已取消';
comment on column flow_purchase.finish_time is '审批结束时间';
comment on column flow_purchase.cancel_reason is '撤销、终止或取消原因';
comment on column flow_purchase.update_by is '更新人';
comment on column flow_purchase.update_time is '更新时间';

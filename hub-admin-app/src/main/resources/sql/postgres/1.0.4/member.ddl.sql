-- 会员用户
drop table if exists hub_member;
create table hub_member
(
    member_id      serial primary key,
    tenant_id    character varying(64),
    member_type    character varying(64),
    member_code    character varying(64),
    member_account character varying(64) not null,
    member_name    character varying(64) not null,
    member_avatar  character varying(256),
    member_email   character varying(128),
    member_status  int2 default 1,
    create_by    character varying(64),
    create_time  timestamp,
    update_by    character varying(64),
    update_time  timestamp
);
create unique index hub_member_member_code on hub_member(member_code);
create unique index hub_member_member_account on hub_member(tenant_id, member_type, member_account);
comment on table hub_member is '会员用户';
comment on column hub_member.member_id is '会员id';
comment on column hub_member.tenant_id is '租户id';
comment on column hub_member.member_type is '会员类型（授权服务类型 gitlab/qq/wechat）';
comment on column hub_member.member_code is '会员编码';
comment on column hub_member.member_account is '会员账号';
comment on column hub_member.member_name is '会员名称';
comment on column hub_member.member_avatar is '会员头像';
comment on column hub_member.member_email is '会员邮箱';
comment on column hub_member.member_status is '会员状态';
comment on column hub_member.create_by is '创建人';
comment on column hub_member.create_time is '创建时间';
comment on column hub_member.update_by is '更新人';
comment on column hub_member.update_time is '更新时间';

-- 会员角色
drop table if exists hub_member_role;
create table hub_member_role
(
    member_id int4 not null,
    role_id int4 not null,
    constraint hub_member_role_pkey primary key (member_id, role_id)
);
comment on table hub_member_role is '会员角色';
comment on column hub_member_role.member_id is '会员id';
comment on column hub_member_role.role_id is '角色id';

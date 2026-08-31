-- 1.租户信息
drop table if exists sys_tenant;
create table sys_tenant
(
    tenant_id    serial primary key,
    tenant_code  character varying(64) not null,
    tenant_name  character varying(128),
    tenant_type  character varying(32) not null default 'normal',
    tenant_domain character varying(255),
    user_limit   int4 default 1000,
    user_count   int4 default 0,
    status       int2 default 1,
    expire_time  timestamptz,
    title        character varying(64),
    view_index   character varying(64) default 'index_tenant',
    tenant_user  character varying(128),
    tenant_addr  character varying(256),
    tenant_phone character varying(64),
    tenant_email character varying(128),
    remark       character varying(200),
    create_by    character varying(64),
    create_time  timestamptz,
    update_by    character varying(64),
    update_time  timestamptz
);
create unique index sys_tenant_code_uq on sys_tenant(tenant_code);
comment on table sys_tenant is '租户信息';
comment on column sys_tenant.tenant_id is '租户id';
comment on column sys_tenant.tenant_code is '租户稳定业务编码，系统租户固定使用system';
comment on column sys_tenant.tenant_name is '租户名称';
comment on column sys_tenant.tenant_type is '租户类型：system系统租户、normal普通租户、trial试用租户';
comment on column sys_tenant.tenant_domain is '租户独立域名或访问入口';
comment on column sys_tenant.tenant_user is '租户联系人';
comment on column sys_tenant.tenant_addr is '租户地址';
comment on column sys_tenant.tenant_phone is '租户电话';
comment on column sys_tenant.tenant_email is '租户邮箱';
comment on column sys_tenant.user_count is '用户统计';
comment on column sys_tenant.user_limit is '用户上限';
comment on column sys_tenant.title is '租户标题';
comment on column sys_tenant.status is '租户状态';
comment on column sys_tenant.expire_time is '到期时间';
comment on column sys_tenant.remark is '备注';
comment on column sys_tenant.create_by is '创建人';
comment on column sys_tenant.create_time is '创建时间';
comment on column sys_tenant.update_by is '更新人';
comment on column sys_tenant.update_time is '更新时间';

-- 2.租户用户
drop table if exists sys_tenant_user;
create table sys_tenant_user
(
    tenant_id    int4 not null,
    user_id      int4 not null,
    user_type    character varying(32) not null default 'employee',
    user_code    character varying(64),
    display_name character varying(64),
    user_rank    character varying(64),
    status       int2 not null default 1,
    is_default   int2 not null default 0,
    join_time    timestamptz not null default current_timestamp,
    leave_time   timestamptz,
    remark       character varying(200),
    create_by    character varying(64),
    create_time  timestamptz not null default current_timestamp,
    update_by    character varying(64),
    update_time  timestamptz,
    primary key (tenant_id, user_id)
);
create index sys_tenant_user_user
    on sys_tenant_user(user_id, status);
create index sys_tenant_user_tenant
    on sys_tenant_user(tenant_id, status);
create unique index sys_tenant_user_user_code_uq
    on sys_tenant_user(tenant_id, user_code)
    where user_code is not null and user_code <> '';
create unique index sys_tenant_user_default_uq
    on sys_tenant_user(user_id)
    where is_default = 1 and status = 1;
comment on table sys_tenant_user is '租户用户关系';
comment on column sys_tenant_user.tenant_id is '租户id';
comment on column sys_tenant_user.user_id is '用户id';
comment on column sys_tenant_user.user_type is '租户内用户类型：employee员工、external外部成员、service服务账号';
comment on column sys_tenant_user.user_code is '租户内用户编码';
comment on column sys_tenant_user.display_name is '租户内显示名称';
comment on column sys_tenant_user.user_rank is '租户内职级';
comment on column sys_tenant_user.status is '租户内成员状态 1启用 2停用';
comment on column sys_tenant_user.is_default is '是否默认租户 1是 0否';
comment on column sys_tenant_user.join_time is '加入时间';
comment on column sys_tenant_user.leave_time is '离开租户时间';
comment on column sys_tenant_user.remark is '备注';
comment on column sys_tenant_user.create_by is '创建人';
comment on column sys_tenant_user.create_time is '创建时间';
comment on column sys_tenant_user.update_by is '更新人';
comment on column sys_tenant_user.update_time is '更新时间';

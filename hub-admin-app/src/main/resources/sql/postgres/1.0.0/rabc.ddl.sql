-- 租户信息
drop table if exists sys_tenant;
create table sys_tenant
(
    tenant_id    character varying(64) primary key,
    tenant_name  character varying(128),
    user_limit   int4 default 1000,
    user_count   int4 default 0,
    user_index   int4 default 0,
    status       int2 default 1,
    expire_time  timestamp,
    title        character varying(64),
    logo         text,
    view_index   character varying(64) default 'index_tenant',
    tenant_user  character varying(128),
    tenant_addr  character varying(256),
    tenant_phone character varying(64),
    tenant_email character varying(128),
    remark       character varying(200),
    create_by    character varying(64),
    create_time  timestamp,
    update_by    character varying(64),
    update_time  timestamp
);
comment on table sys_tenant is '租户信息';
comment on column sys_tenant.tenant_id is '租户id';
comment on column sys_tenant.tenant_name is '租户名称';
comment on column sys_tenant.tenant_user is '租户联系人';
comment on column sys_tenant.tenant_addr is '租户地址';
comment on column sys_tenant.tenant_phone is '租户电话';
comment on column sys_tenant.tenant_email is '租户邮箱';
comment on column sys_tenant.user_index is '用户序号';
comment on column sys_tenant.user_count is '用户统计';
comment on column sys_tenant.user_limit is '用户上限';
comment on column sys_tenant.title is '租户标题';
comment on column sys_tenant.logo is '租户图标';
comment on column sys_tenant.status is '租户状态';
comment on column sys_tenant.expire_time is '到期时间';
comment on column sys_tenant.remark is '备注';
comment on column sys_tenant.create_by is '创建人';
comment on column sys_tenant.create_time is '创建时间';
comment on column sys_tenant.update_by is '更新人';
comment on column sys_tenant.update_time is '更新时间';

-- 部门信息
drop table if exists sys_dept;
create table sys_dept(
    dept_id     serial primary key,
    tenant_id   character varying(64),
    dept_code   character varying(64),
    dept_type   character varying(64),
    dept_name   character varying(128),
    dept_short  character varying(64),
    dept_addr   character varying(512),
    dept_phone  character varying(64),
    remark      character varying(200),
    create_by   character varying(64),
    create_time timestamp,
    update_by   character varying(64),
    update_time timestamp

);
create unique index sys_dept_dept_code on sys_dept(tenant_id, dept_code);
comment on table sys_dept is '部门信息';
comment on column sys_dept.dept_id is '部门id';
comment on column sys_dept.tenant_id is '租户id';
comment on column sys_dept.dept_code is '部门编码';
comment on column sys_dept.dept_type is '部门类型';
comment on column sys_dept.dept_name is '部门名称';
comment on column sys_dept.dept_short is '部门简称';
comment on column sys_dept.dept_addr is '部门地址';
comment on column sys_dept.dept_phone is '部门电话';
comment on column sys_dept.remark is '备注';
comment on column sys_dept.create_by is '创建人';
comment on column sys_dept.create_time is '创建时间';
comment on column sys_dept.update_by is '更新人';
comment on column sys_dept.update_time is '更新时间';

-- 部门关系
drop table if exists sys_dept_diagram;
create table sys_dept_diagram(
    parent_id int4 not null,
    dept_id   int4 not null,
    tenant_id character varying(64),
    constraint sys_dept_diagram_pkey primary key (dept_id, parent_id)
);
comment on table sys_dept_diagram is '部门关系';
comment on column sys_dept_diagram.parent_id is '上级部门id';
comment on column sys_dept_diagram.dept_id is '部门id';
comment on column sys_dept_diagram.tenant_id is '租户id';

-- 岗位信息
drop table if exists sys_post;
create table sys_post(
    post_id     serial primary key,
    tenant_id   character varying(64),
    post_code   varchar(64),
    post_name   varchar(64) not null,
    post_level  int2 default 1,
    post_type   varchar(64),
    post_status int2 default 1,
    remark      character varying(200),
    create_by   varchar(64),
    create_time timestamp,
    update_by   varchar(64),
    update_time timestamp
);
comment on table sys_post is '岗位信息';
comment on column sys_post.post_id is '岗位id';
comment on column sys_post.post_code is '岗位编码';
comment on column sys_post.post_name is '岗位名称';
comment on column sys_post.post_level is '岗位级别';
comment on column sys_post.post_type is '岗位类型';
comment on column sys_post.post_status is '岗位状态';
comment on column sys_post.remark is '备注';
comment on column sys_post.create_by is '创建人';
comment on column sys_post.create_time is '创建时间';
comment on column sys_post.update_by is '更新人';
comment on column sys_post.update_time is '更新时间';

-- 岗位关系
drop table if exists sys_post_diagram;
create table sys_post_diagram(
    parent_id int4 not null,
    post_id   int4 not null,
    tenant_id character varying(64),
    constraint sys_post_diagram_pkey primary key (post_id, parent_id)
);
comment on table sys_post_diagram is '岗位关系';
comment on column sys_post_diagram.parent_id is '上级岗位id';
comment on column sys_post_diagram.post_id is '岗位id';
comment on column sys_post_diagram.tenant_id is '租户id';

-- 部门岗位
drop table if exists sys_dept_post;
create table sys_dept_post(
    dept_id   int4 not null,
    post_id   int4 not null,
    is_default int2 default 0,
    constraint sys_dept_post_pkey primary key (dept_id, post_id)
);
comment on table sys_dept_post is '部门岗位';
comment on column sys_dept_post.dept_id is '部门id';
comment on column sys_dept_post.post_id is '岗位id';
comment on column sys_dept_post.is_default is '是否部门默认岗位';

-- 用户信息
drop table if exists sys_user;
create table sys_user
(
    user_id      serial primary key,
    user_code    character varying(64),
    tenant_id    character varying(64),
    user_type    character varying(64),
    user_account character varying(64)  not null,
    user_name    character varying(64)  not null,
    user_passwd  character varying(256),
    user_sex     int2 default 0,
    user_phone   character varying(11),
    user_email   character varying(128),
    user_rank    character varying(64),
    user_status  int2 default 1,
    mfa          character varying(64),
    remark       character varying(200),
    create_by    character varying(64),
    create_time  timestamp,
    update_by    character varying(64),
    update_time  timestamp
);
create unique index sys_user_user_code on sys_user(user_code);
create unique index sys_user_user_account on sys_user(tenant_id, user_type, user_account);
comment on table sys_user is '用户信息';
comment on column sys_user.user_id is '用户id';
comment on column sys_user.tenant_id is '租户id';
comment on column sys_user.user_type is '用户类型';
comment on column sys_user.user_code is '用户编码';
comment on column sys_user.user_name is '用户名称';
comment on column sys_user.user_account is '用户账号';
comment on column sys_user.user_passwd is '用户密码';
comment on column sys_user.user_sex is '用户性别';
comment on column sys_user.user_phone is '用户电话';
comment on column sys_user.user_email is '用户邮箱';
comment on column sys_user.user_status is '用户状态';
comment on column sys_user.user_rank is '职级';
comment on column sys_user.remark is '备注';
comment on column sys_user.create_by is '创建人';
comment on column sys_user.create_time is '创建时间';
comment on column sys_user.update_by is '更新人';
comment on column sys_user.update_time is '更新时间';

-- 用户关系
drop table if exists sys_user_diagram;
create table sys_user_diagram
(
    parent_id int4 not null,
    user_id   int4 not null,
    tenant_id character varying(64),
    constraint sys_user_diagram_pkey primary key (user_id, parent_id)
);
comment on table sys_user_diagram is '用户关系';
comment on column sys_user_diagram.parent_id is '上级用户id';
comment on column sys_user_diagram.user_id is '用户id';
comment on column sys_user_diagram.tenant_id is '租户id';

-- 用户部门
drop table if exists sys_user_dept;
create table sys_user_dept(
    user_id    int4 not null,
    dept_id    int4 not null,
    post_id    int4 default -1,
    is_default int2 default 0,
    is_leader  int2 default 0,
    constraint sys_user_dept_pkey primary key (user_id, dept_id, post_id)
);
comment on table sys_user_dept is '用户部门';
comment on column sys_user_dept.user_id is '用户id';
comment on column sys_user_dept.dept_id is '部门id';
comment on column sys_user_dept.post_id is '岗位id';
comment on column sys_user_dept.is_default is '是否用户默认部门';
comment on column sys_user_dept.is_leader is '是否部门负责人';

-- 角色信息
drop table if exists sys_role;
create table sys_role(
    role_id     serial primary key,
    tenant_id   character varying(64),
    role_code   character varying(100) not null,
    role_name   character varying(64)  not null,
    role_type   character varying(64),
    remark      character varying(200),
    create_by   character varying(64),
    create_time timestamp,
    update_by   character varying(64),
    update_time timestamp
);
create unique index sys_role_role_code on sys_role(tenant_id, role_code);
comment on table sys_role is '角色信息';
comment on column sys_role.role_id is '角色id';
comment on column sys_role.role_code is '角色编码';
comment on column sys_role.role_name is '角色名称';
comment on column sys_role.role_type is '角色类型';
comment on column sys_role.remark is '备注';
comment on column sys_role.create_by is '创建人';
comment on column sys_role.create_time is '创建时间';
comment on column sys_role.update_by is '更新人';
comment on column sys_role.update_time is '更新时间';

-- 用户角色
drop table if exists sys_user_role;
create table sys_user_role(
    user_id int4 not null,
    role_id int4 not null,
    constraint sys_user_role_pkey primary key (user_id, role_id)
);
comment on table sys_user_role is '用户角色';
comment on column sys_user_role.user_id is '用户id';
comment on column sys_user_role.role_id is '角色id';

-- 菜单信息
drop table if exists sys_menu;
create table sys_menu
(
    menu_id      serial primary key,
    parent_id    int4                   default 0,
    tenant_id    character varying(64),
    menu_module  character varying(64),
    menu_name    character varying(64) not null,
    menu_order   integer                default 0,
    menu_permit  character varying(255),
    menu_path    character varying(255) default '#',
    menu_param   character varying(255),
    menu_type    char(1)               not null,
    menu_icon    character varying(100) default '#',
    component    character varying(255),
    menu_status  int2                   default 1,
    is_frame     int2                   DEFAULT 1,
    is_cache     int2                   DEFAULT 1,
    is_visible   int2                   DEFAULT 1,
    is_protected int2                   DEFAULT 1,
    remark       character varying(255),
    create_by    character varying(64),
    create_time  timestamp,
    update_by    character varying(64),
    update_time  timestamp
);
comment on table sys_menu is '菜单信息';
comment on column sys_menu.menu_id is '菜单id';
comment on column sys_menu.parent_id is '父菜单id';
comment on column sys_menu.menu_name is '菜单名称';
comment on column sys_menu.menu_order is '菜单顺序';
comment on column sys_menu.menu_permit is '权限标识';
comment on column sys_menu.menu_path is '菜单路径';
comment on column sys_menu.menu_param is '路径参数';
comment on column sys_menu.menu_type is '菜单类型：M:目录 C:菜单 B:按钮';
comment on column sys_menu.menu_icon is '菜单图标';
comment on column sys_menu.component is '组件路径';
comment on column sys_menu.menu_status is '菜单状态 1启用 2停用';
comment on column sys_menu.is_frame is '是否内部链接 1是 0否';
comment on column sys_menu.is_cache is '是否缓存 1是 0否';
comment on column sys_menu.is_visible is '是否显示 1是 0否';
comment on column sys_menu.is_protected is '是否受保护的菜单 1是 0否';
comment on column sys_menu.remark is '备注';
comment on column sys_menu.create_by is '创建人';
comment on column sys_menu.create_time is '创建时间';
comment on column sys_menu.update_by is '更新人';
comment on column sys_menu.update_time is '更新时间';

-- 角色菜单
drop table if exists sys_role_menu;
create table sys_role_menu
(
    role_id  int4 not null,
    menu_id  int4 not null,
    scope_id int4,
    constraint sys_role_menu_pkey primary key (role_id, menu_id)
);
comment on table sys_role_menu is '角色菜单';
comment on column sys_role_menu.role_id is '角色id';
comment on column sys_role_menu.menu_id is '菜单id';

-- 数据权限
drop table if exists sys_scope;
CREATE TABLE sys_scope
(
    scope_id      serial primary key,
    tenant_id     character varying(64),
    scope_name    character varying(255),
    scope_module  character varying(64),
    scope_status  int2 DEFAULT 1,
    scope_content jsonb default '{}',
    remark        varchar(200),
    create_by     character varying(64),
    create_time   timestamp,
    update_by     character varying(64),
    update_time   timestamp,
    content jsonb
);
comment on table sys_scope is '数据权限';
comment on column sys_scope.scope_id is '权限id';
comment on column sys_scope.scope_name is '权限名称';
comment on column sys_scope.scope_status is '权限状态';
comment on column sys_scope.scope_content is '权限规则';
comment on column sys_scope.remark is '备注';

-- 用户ApiToken
drop table if exists hub_token;
create table hub_token(
    token_id    serial primary key,
    token_name  character varying(128),
    token_value character varying(1024),
    expire      timestamp,
    ip_rule     character varying(128),
    user_code   character varying(64),
    create_time timestamp
);
comment on table hub_token is '用户ApiToken';
comment on column hub_token.token_id is '令牌id';
comment on column hub_token.token_name is '令牌名称';
comment on column hub_token.token_value is '令牌token';
comment on column hub_token.expire is '到期时间';
comment on column hub_token.ip_rule is 'ip限制';
comment on column hub_token.user_code is '用户编码';
comment on column hub_token.create_time is '创建时间';

-- 用户ApiToken权限
drop table if exists hub_token_menu;
create table hub_token_menu(
    token_id int4,
    menu_id int4,
    scope_id int4,
    constraint hub_token_menu_pkey primary key (token_id, menu_id)
);
comment on table hub_token_menu is '用户ApiToken权限';
comment on column hub_token_menu.token_id is '令牌id';
comment on column hub_token_menu.menu_id is '菜单id';

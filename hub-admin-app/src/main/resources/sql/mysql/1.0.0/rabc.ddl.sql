-- 租户信息
drop table if exists hub_tenant;
create table hub_tenant
(
    tenant_id    varchar(64) primary key comment '租户id',
    tenant_name  varchar(128) comment '租户名称',
    user_limit   int default 1000 comment '用户上限',
    user_count   int default 0 comment '用户统计',
    user_index   int default 0 comment '用户序号',
    status       smallint default 1 comment '租户状态',
    expire_time  datetime comment '到期时间',
    title        varchar(64) comment '租户标题',
    logo         text comment '租户图标',
    view_index   varchar(64) default 'index_tenant' comment '视图索引',
    tenant_user  varchar(128) comment '租户联系人',
    tenant_addr  varchar(256) comment '租户地址',
    tenant_phone varchar(64) comment '租户电话',
    tenant_email varchar(128) comment '租户邮箱',
    remark       varchar(200) comment '备注',
    create_by    varchar(64) comment '创建人',
    create_time  datetime comment '创建时间',
    update_by    varchar(64) comment '更新人',
    update_time  datetime comment '更新时间'
) comment='租户信息';

-- 部门信息
drop table if exists hub_dept;
create table hub_dept(
    dept_id     int auto_increment primary key comment '部门id',
    tenant_id   varchar(64) comment '租户id',
    dept_code   varchar(64) comment '部门编码',
    dept_type   varchar(64) comment '部门类型',
    dept_name   varchar(128) comment '部门名称',
    dept_short  varchar(64) comment '部门简称',
    dept_addr   varchar(512) comment '部门地址',
    dept_phone  varchar(64) comment '部门电话',
    remark      varchar(200) comment '备注',
    create_by   varchar(64) comment '创建人',
    create_time datetime comment '创建时间',
    update_by   varchar(64) comment '更新人',
    update_time datetime comment '更新时间'
) comment='部门信息';
create unique index hub_dept_dept_code on hub_dept(tenant_id, dept_code);

-- 部门关系
drop table if exists hub_dept_diagram;
create table hub_dept_diagram(
    parent_id int not null comment '上级部门id',
    dept_id   int not null comment '部门id',
    tenant_id varchar(64) comment '租户id',
    primary key (dept_id, parent_id)
) comment='部门关系';

-- 岗位信息
drop table if exists hub_post;
create table hub_post(
    post_id     int auto_increment primary key comment '岗位id',
    tenant_id   varchar(64) comment '租户id',
    post_code   varchar(64) comment '岗位编码',
    post_name   varchar(64) not null comment '岗位名称',
    post_level  smallint default 1 comment '岗位级别',
    post_type   varchar(64) comment '岗位类型',
    post_status smallint default 1 comment '岗位状态',
    remark      varchar(200) comment '备注',
    create_by   varchar(64) comment '创建人',
    create_time datetime comment '创建时间',
    update_by   varchar(64) comment '更新人',
    update_time datetime comment '更新时间'
) comment='岗位信息';

-- 岗位关系
drop table if exists hub_post_diagram;
create table hub_post_diagram(
    parent_id int not null comment '上级岗位id',
    post_id   int not null comment '岗位id',
    tenant_id varchar(64) comment '租户id',
    primary key (post_id, parent_id)
) comment='岗位关系';

-- 部门岗位
drop table if exists hub_dept_post;
create table hub_dept_post(
    dept_id    int not null comment '部门id',
    post_id    int not null comment '岗位id',
    is_default smallint default 0 comment '是否部门默认岗位',
    primary key (dept_id, post_id)
) comment='部门岗位';

-- 用户信息
drop table if exists hub_user;
create table hub_user
(
    user_id      int auto_increment primary key comment '用户id',
    user_code    varchar(64) comment '用户编码',
    tenant_id    varchar(64) comment '租户id',
    user_type    varchar(64) comment '用户类型',
    user_account varchar(64) not null comment '用户账号',
    user_name    varchar(64) not null comment '用户名称',
    user_passwd  varchar(256) comment '用户密码',
    user_sex     smallint default 0 comment '用户性别',
    user_phone   varchar(11) comment '用户电话',
    user_email   varchar(128) comment '用户邮箱',
    user_rank    varchar(64) comment '职级',
    user_status  smallint default 1 comment '用户状态',
    mfa          varchar(64) comment 'MFA密钥',
    remark       varchar(200) comment '备注',
    create_by    varchar(64) comment '创建人',
    create_time  datetime comment '创建时间',
    update_by    varchar(64) comment '更新人',
    update_time  datetime comment '更新时间'
) comment='用户信息';
create unique index hub_user_user_code on hub_user(user_code);
create unique index hub_user_user_account on hub_user(tenant_id, user_type, user_account);

-- 用户关系
drop table if exists hub_user_diagram;
create table hub_user_diagram
(
    parent_id int not null comment '上级用户id',
    user_id   int not null comment '用户id',
    tenant_id varchar(64) comment '租户id',
    primary key (user_id, parent_id)
) comment='用户关系';

-- 用户部门
drop table if exists hub_user_dept;
create table hub_user_dept(
    user_id    int not null comment '用户id',
    dept_id    int not null comment '部门id',
    post_id    int default -1 comment '岗位id',
    is_default smallint default 0 comment '是否用户默认部门',
    is_leader  smallint default 0 comment '是否部门负责人',
    primary key (user_id, dept_id, post_id)
) comment='用户部门';

-- 角色信息
drop table if exists hub_role;
create table hub_role(
    role_id     int auto_increment primary key comment '角色id',
    tenant_id   varchar(64) comment '租户id',
    role_code   varchar(100) not null comment '角色编码',
    role_name   varchar(64) not null comment '角色名称',
    role_type   varchar(64) comment '角色类型',
    remark      varchar(200) comment '备注',
    create_by   varchar(64) comment '创建人',
    create_time datetime comment '创建时间',
    update_by   varchar(64) comment '更新人',
    update_time datetime comment '更新时间'
) comment='角色信息';
create unique index hub_role_role_code on hub_role(tenant_id, role_code);

-- 用户角色
drop table if exists hub_user_role;
create table hub_user_role(
    user_id int not null comment '用户id',
    role_id int not null comment '角色id',
    primary key (user_id, role_id)
) comment='用户角色';

-- 菜单信息
drop table if exists hub_menu;
create table hub_menu
(
    menu_id      int auto_increment primary key comment '菜单id',
    parent_id    int default 0 comment '父菜单id',
    tenant_id    varchar(64) comment '租户id',
    menu_module  varchar(64) comment '菜单模块',
    menu_name    varchar(64) not null comment '菜单名称',
    menu_order   int default 0 comment '菜单顺序',
    menu_permit  varchar(255) comment '权限标识',
    menu_path    varchar(255) default '#' comment '菜单路径',
    menu_param   varchar(255) comment '路径参数',
    menu_type    char(1) not null comment '菜单类型：M:目录 C:菜单 B:按钮',
    menu_icon    varchar(100) default '#' comment '菜单图标',
    component    varchar(255) comment '组件路径',
    menu_status  smallint default 1 comment '菜单状态 1启用 2停用',
    is_frame     smallint default 1 comment '是否内部链接 1是 0否',
    is_cache     smallint default 1 comment '是否缓存 1是 0否',
    is_visible   smallint default 1 comment '是否显示 1是 0否',
    is_protected smallint default 1 comment '是否受保护的菜单 1是 0否',
    remark       varchar(255) comment '备注',
    create_by    varchar(64) comment '创建人',
    create_time  datetime comment '创建时间',
    update_by    varchar(64) comment '更新人',
    update_time  datetime comment '更新时间'
) comment='菜单信息';

-- 角色菜单
drop table if exists hub_role_menu;
create table hub_role_menu
(
    role_id  int not null comment '角色id',
    menu_id  int not null comment '菜单id',
    scope_id int comment '数据范围id',
    primary key (role_id, menu_id)
) comment='角色菜单';

-- 数据权限
drop table if exists hub_scope;
CREATE TABLE hub_scope
(
    scope_id      int auto_increment primary key comment '权限id',
    tenant_id     varchar(64) comment '租户id',
    scope_name    varchar(255) comment '权限名称',
    scope_module  varchar(64) comment '权限模块',
    scope_status  smallint default 1 comment '权限状态',
    scope_content jsonb default ('{}') comment '权限规则',
    remark        varchar(200) comment '备注',
    create_by     varchar(64) comment '创建人',
    create_time   datetime comment '创建时间',
    update_by     varchar(64) comment '更新人',
    update_time   datetime comment '更新时间'
) comment='数据权限';

-- 用户ApiToken
drop table if exists hub_token;
create table hub_token(
    token_id    int auto_increment primary key comment '令牌id',
    token_name  varchar(128) comment '令牌名称',
    token_value varchar(1024) comment '令牌token',
    expire      datetime comment '到期时间',
    ip_rule     varchar(128) comment 'ip限制',
    user_code   varchar(64) comment '用户编码',
    create_time datetime comment '创建时间'
) comment='用户ApiToken';

-- 用户ApiToken权限
drop table if exists hub_token_menu;
create table hub_token_menu(
    token_id int comment '令牌id',
    menu_id  int comment '菜单id',
    scope_id int comment '数据范围id',
    primary key (token_id, menu_id)
) comment='用户ApiToken权限';

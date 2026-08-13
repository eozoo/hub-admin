-- oauth 授权服务
drop table if exists sys_oauth;
create table sys_oauth
(
    tenant_id     varchar(64) primary key comment '租户id',
    server_type   varchar(64) comment '服务类型',
    app_id        varchar(64) comment '应用id',
    app_secret    varchar(64) comment '应用密钥',
    auth_url      varchar(256) comment '授权服务url',
    redirect_url  varchar(256) comment '应用回调地址',
    grant_type    varchar(64) comment '授权类型',
    response_type varchar(64) comment '响应类型',
    auth_scope    varchar(128) comment '授权范围',
    role_code     varchar(64) comment '用户角色',
    status        smallint default 0 comment '状态 0 关闭 1开启',
    create_by     varchar(64) comment '创建人',
    create_time   datetime comment '创建时间',
    update_by     varchar(64) comment '更新人',
    update_time   datetime comment '更新时间'
) comment='OAuth服务';
create unique index sys_oauth_unique on sys_oauth(tenant_id, server_type);

-- oauth 授权用户
drop table if exists sys_oauth_user;
create table sys_oauth_user
(
    id           bigint auto_increment primary key comment 'id',
    tenant_id    varchar(64) comment '租户id',
    server_type  varchar(64) comment '应用类型',
    user_name    varchar(64) comment '用户名称',
    user_account varchar(64) comment '用户账号',
    user_avatar  varchar(256) comment '用户头像',
    user_email   varchar(64) comment '用户邮箱',
    user_dept    varchar(256) comment '用户部门',
    create_time  datetime comment '创建时间',
    update_time  datetime comment '更新时间'
) comment='授权用户';
create unique index sys_oauth_user_unique on sys_oauth_user(tenant_id, server_type, user_account);

-- oauth 授权应用
drop table if exists hub_app;
create table hub_app
(
    id            int auto_increment primary key comment 'id',
    tenant_id     varchar(64) comment '租户id',
    card_name     varchar(64) comment '卡片名称',
    card_icon     varchar(64) comment '卡片图标',
    client_name   varchar(64) comment '应用名称',
    client_id     varchar(64) comment '应用id',
    client_secret varchar(64) comment '应用密钥',
    grant_type    text comment '授权类型',
    auth_scope    text comment '授权范围',
    redirect_url  varchar(256) comment '重定向地址',
    create_by     varchar(64) comment '创建人',
    create_time   datetime comment '创建时间',
    update_by     varchar(64) comment '更新人',
    update_time   datetime comment '更新时间'
) comment='OAuth应用';

-- OAuth应用菜单信息
drop table if exists hub_app_menu;
create table hub_app_menu
(
    menu_id      int auto_increment primary key comment '菜单id',
    parent_id    int default 0 comment '父菜单id',
    app_id       int comment '应用id',
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
) comment='OAuth应用菜单信息';

-- oauth 角色授权应用
drop table if exists hub_role_app;
create table hub_role_app
(
    role_id int comment '角色id',
    app_id  int comment '应用id'
) comment='角色授权应用';

-- oauth 角色应用菜单
drop table if exists hub_role_app_menu;
create table hub_role_app_menu
(
    role_id int comment '角色id',
    app_id  int comment '应用id',
    menu_id int comment '菜单id'
) comment='角色应用菜单';

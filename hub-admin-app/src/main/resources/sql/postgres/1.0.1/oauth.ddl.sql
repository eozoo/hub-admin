-- oauth 授权服务
drop table if exists hub_oauth;
create table hub_oauth
(
    tenant_id     character varying(64) primary key,
    server_type   character varying(64),
    app_id        character varying(64),
    app_secret    character varying(64),
    auth_url      character varying(256),
    redirect_url  character varying(256),
    grant_type    character varying(64),
    response_type character varying(64),
    auth_scope    character varying(128),
    role_code     character varying(64),
    status        int2 default 0,
    create_by     character varying(64),
    create_time   timestamp,
    update_by     character varying(64),
    update_time   timestamp
);
create unique index hub_oauth_unique on hub_oauth(tenant_id, server_type);
comment on table hub_oauth is 'OAuth服务';
comment on column hub_oauth.status is '状态 0 关闭 1开启';
comment on column hub_oauth.role_code is '用户角色';
comment on column hub_oauth.server_type is '服务类型';
comment on column hub_oauth.app_id is '应用id';
comment on column hub_oauth.app_secret is '应用密钥';
comment on column hub_oauth.auth_url is '授权服务url';
comment on column hub_oauth.redirect_url is '应用回调地址';
comment on column hub_oauth.grant_type is '授权类型';
comment on column hub_oauth.response_type is '响应类型';
comment on column hub_oauth.auth_scope is '授权范围';
comment on column hub_oauth.create_by is '创建人';
comment on column hub_oauth.create_time is '创建时间';
comment on column hub_oauth.update_by is '更新人';
comment on column hub_oauth.update_time is '更新时间';

-- oauth 授权用户
drop table if exists hub_oauth_user;
create table hub_oauth_user
(
    id           bigserial primary key,
    tenant_id    character varying(64),
    server_type  character varying(64),
    user_name    character varying(64),
    user_account character varying(64),
    user_avatar  character varying(256),
    user_email   character varying(64),
    user_dept    character varying(256),
    create_time  timestamp,
    update_time  timestamp
);
create unique index hub_oauth_user_unique on hub_oauth_user(tenant_id, server_type, user_account);
comment on table hub_oauth_user is '授权用户';
comment on column hub_oauth_user.id is 'id';
comment on column hub_oauth_user.server_type is '应用类型';
comment on column hub_oauth_user.user_name is '用户名称';
comment on column hub_oauth_user.user_account is '用户账号';
comment on column hub_oauth_user.user_avatar is '用户头像';
comment on column hub_oauth_user.user_email is '用户邮箱';
comment on column hub_oauth_user.user_dept is '用户部门';
comment on column hub_oauth_user.create_time is '创建时间';
comment on column hub_oauth_user.update_time is '更新时间';

-- oauth 授权应用
drop table if exists hub_oauth_app;
create table hub_oauth_app
(
    id            serial primary key,
    tenant_id     character varying(64),
    card_name     character varying(64),
    card_icon     character varying(64),
    client_name   character varying(64),
    client_id     character varying(64),
    client_secret character varying(64),
    grant_type    varchar(64)[],
    auth_scope    varchar(128)[],
    redirect_url  character varying(256),
    create_by     character varying(64),
    create_time   timestamp,
    update_by     character varying(64),
    update_time   timestamp
);
comment on table hub_oauth_app is 'OAuth应用';
comment on column hub_oauth_app.id is 'id';
comment on column hub_oauth_app.client_name is '应用名称';
comment on column hub_oauth_app.client_id is '应用id';
comment on column hub_oauth_app.client_secret is '应用密钥';
comment on column hub_oauth_app.grant_type is '授权类型';
comment on column hub_oauth_app.auth_scope is '授权范围';
comment on column hub_oauth_app.redirect_url is '重定向地址';
comment on column hub_oauth.create_by is '创建人';
comment on column hub_oauth.create_time is '创建时间';
comment on column hub_oauth.update_by is '更新人';
comment on column hub_oauth.update_time is '更新时间';

-- 菜单信息
drop table if exists hub_oauth_app_menu;
create table hub_oauth_app_menu
(
    menu_id      serial primary key,
    parent_id    int4                   default 0,
    app_id       int4,
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

-- oauth 角色授权应用
drop table if exists hub_role_app;
create table hub_role_app
(
    role_id int4,
    app_id  int4
);

-- oauth 角色应用菜单
drop table if exists hub_role_app_menu;
create table hub_role_app_menu
(
    role_id int4,
    app_id  int4,
    menu_id int4
);

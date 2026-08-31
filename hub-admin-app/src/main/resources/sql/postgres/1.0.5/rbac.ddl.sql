-- 1.部门信息
drop table if exists sys_dept;
create table sys_dept(
    dept_id     serial primary key,
    tenant_id   int4 not null,
    dept_code   character varying(64),
    dept_type   character varying(64),
    dept_status int2 not null default 1,
    dept_order  int4 not null default 0,
    dept_name   character varying(128),
    dept_short  character varying(64),
    dept_addr   character varying(512),
    dept_phone  character varying(64),
    remark      character varying(200),
    create_by   character varying(64),
    create_time timestamptz,
    update_by   character varying(64),
    update_time timestamptz
);
create unique index sys_dept_dept_code on sys_dept(tenant_id, dept_code);
comment on table sys_dept is '部门信息';
comment on column sys_dept.dept_id is '部门id';
comment on column sys_dept.tenant_id is '租户id';
comment on column sys_dept.dept_code is '部门编码';
comment on column sys_dept.dept_type is '部门类型';
comment on column sys_dept.dept_status is '部门状态 1启用 2停用';
comment on column sys_dept.dept_order is '部门排序';
comment on column sys_dept.dept_name is '部门名称';
comment on column sys_dept.dept_short is '部门简称';
comment on column sys_dept.dept_addr is '部门地址';
comment on column sys_dept.dept_phone is '部门电话';
comment on column sys_dept.remark is '备注';
comment on column sys_dept.create_by is '创建人';
comment on column sys_dept.create_time is '创建时间';
comment on column sys_dept.update_by is '更新人';
comment on column sys_dept.update_time is '更新时间';

-- 2.部门关系
drop table if exists sys_dept_diagram;
create table sys_dept_diagram(
    parent_id int4 not null,
    dept_id   int4 not null,
    tenant_id int4 not null,
    relation_type character varying(32) not null default 'administrative',
    constraint sys_dept_diagram_pkey primary key (dept_id, parent_id, relation_type)
);
comment on table sys_dept_diagram is '部门关系';
comment on column sys_dept_diagram.parent_id is '上级部门id';
comment on column sys_dept_diagram.dept_id is '部门id';
comment on column sys_dept_diagram.tenant_id is '租户id';
comment on column sys_dept_diagram.relation_type is '部门关系类型，默认行政隶属';

-- 3.岗位信息
drop table if exists sys_post;
create table sys_post(
    post_id     serial primary key,
    tenant_id   int4 not null,
    post_code   varchar(64),
    post_name   varchar(64) not null,
    post_level  int2 default 1,
    post_type   varchar(64),
    post_status int2 default 1,
    remark      character varying(200),
    create_by   varchar(64),
    create_time timestamptz,
    update_by   varchar(64),
    update_time timestamptz
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

-- 4.岗位关系
drop table if exists sys_post_diagram;
create table sys_post_diagram(
    parent_id int4 not null,
    post_id   int4 not null,
    tenant_id int4 not null,
    relation_type character varying(32) not null default 'hierarchy',
    constraint sys_post_diagram_pkey primary key (post_id, parent_id, relation_type)
);
comment on table sys_post_diagram is '岗位关系';
comment on column sys_post_diagram.parent_id is '上级岗位id';
comment on column sys_post_diagram.post_id is '岗位id';
comment on column sys_post_diagram.tenant_id is '租户id';
comment on column sys_post_diagram.relation_type is '岗位关系类型，默认层级关系';

-- 5.部门岗位
drop table if exists sys_dept_post;
create table sys_dept_post(
    tenant_id int4 not null,
    dept_id   int4 not null,
    post_id   int4 not null,
    is_default int2 default 0,
    constraint sys_dept_post_pkey primary key (dept_id, post_id)
);
create index sys_dept_post_tenant on sys_dept_post(tenant_id);
comment on table sys_dept_post is '部门岗位';
comment on column sys_dept_post.tenant_id is '租户id';
comment on column sys_dept_post.dept_id is '部门id';
comment on column sys_dept_post.post_id is '岗位id';
comment on column sys_dept_post.is_default is '是否部门默认岗位';

-- 6.用户信息
drop table if exists sys_user;
create table sys_user
(
    user_id      serial primary key,
    user_account character varying(64)  not null,
    user_name    character varying(64)  not null,
    user_alias   character varying(64),
    user_sex     int2 default 0,
    user_phone   character varying(11),
    user_email   character varying(128),
    user_avatar  character varying(1024),
    user_sign    character varying(512),
    user_status  int2 default 1,
    mfa          character varying(64),
    remark       character varying(200),
    create_by    character varying(64),
    create_time  timestamptz,
    update_by    character varying(64),
    update_time  timestamptz
);
create unique index sys_user_user_account on sys_user(user_account);
comment on table sys_user is '用户信息，仅允许停用或注销，不物理删除';
comment on column sys_user.user_id is '用户id';
comment on column sys_user.user_name is '用户名称';
comment on column sys_user.user_alias is '用户花名';
comment on column sys_user.user_account is '用户账号';
comment on column sys_user.user_sex is '用户性别';
comment on column sys_user.user_phone is '用户电话';
comment on column sys_user.user_email is '用户邮箱';
comment on column sys_user.user_avatar is '用户统一头像地址';
comment on column sys_user.user_sign is '用户个性签名';
comment on column sys_user.user_status is '用户状态';
comment on column sys_user.remark is '备注';
comment on column sys_user.create_by is '创建人';
comment on column sys_user.create_time is '创建时间';
comment on column sys_user.update_by is '更新人';
comment on column sys_user.update_time is '更新时间';

-- 7.用户密码
drop table if exists sys_user_passwd;
create table sys_user_passwd
(
    passwd_id          bigserial primary key,
    user_id            int4 not null,
    passwd_hash        character varying(512) not null,
    passwd_algo        character varying(32) not null default 'bcrypt',
    is_current         int2 not null default 1,
    need_change        int2 not null default 0,
    effective_time     timestamptz not null default current_timestamp,
    expire_time        timestamptz,
    invalid_time       timestamptz,
    change_source      character varying(32) not null default 'self',
    create_by          character varying(64),
    create_time        timestamptz not null default current_timestamp,
    update_by          character varying(64),
    update_time        timestamptz
);
create unique index sys_user_passwd_current_uq
    on sys_user_passwd(user_id)
    where is_current = 1;
create index sys_user_passwd_history
    on sys_user_passwd(user_id, create_time desc);
create index sys_user_passwd_expire
    on sys_user_passwd(expire_time)
    where is_current = 1 and expire_time is not null;
comment on table sys_user_passwd is '用户本地密码凭据及历史记录';
comment on column sys_user_passwd.passwd_id is '密码记录id';
comment on column sys_user_passwd.user_id is '全局用户id';
comment on column sys_user_passwd.passwd_hash is '密码哈希值，不保存明文密码';
comment on column sys_user_passwd.passwd_algo is '密码哈希算法，如bcrypt、argon2';
comment on column sys_user_passwd.is_current is '是否当前有效密码 1是 0否';
comment on column sys_user_passwd.need_change is '是否需要修改密码 1是 0否，首次创建或管理员重置时置1';
comment on column sys_user_passwd.effective_time is '密码生效时间';
comment on column sys_user_passwd.expire_time is '密码到期时间，空表示不过期';
comment on column sys_user_passwd.invalid_time is '密码失效时间';
comment on column sys_user_passwd.change_source is '密码来源：initial首次创建、self用户修改、admin管理员重置、recovery找回密码';
comment on column sys_user_passwd.create_by is '创建人';
comment on column sys_user_passwd.create_time is '创建时间';
comment on column sys_user_passwd.update_by is '更新人';
comment on column sys_user_passwd.update_time is '更新时间';

-- 8.用户关系
drop table if exists sys_user_diagram;
create table sys_user_diagram
(
    parent_id int4 not null,
    user_id   int4 not null,
    tenant_id int4 not null,
    relation_type character varying(32) not null default 'direct',
    constraint sys_user_diagram_pkey primary key (tenant_id, user_id, parent_id, relation_type)
);
comment on table sys_user_diagram is '用户关系';
comment on column sys_user_diagram.parent_id is '上级用户id';
comment on column sys_user_diagram.user_id is '用户id';
comment on column sys_user_diagram.tenant_id is '租户id';
comment on column sys_user_diagram.relation_type is '用户上下级关系类型，默认直属关系';

-- 9.用户部门
drop table if exists sys_user_dept;
create table sys_user_dept(
    tenant_id  int4 not null,
    user_id    int4 not null,
    dept_id    int4 not null,
    post_id    int4 default -1,
    is_primary int2 default 0,
    is_leader  int2 default 0,
    constraint sys_user_dept_pkey primary key (tenant_id, user_id, dept_id, post_id)
);
comment on table sys_user_dept is '用户部门';
comment on column sys_user_dept.tenant_id is '租户id';
comment on column sys_user_dept.user_id is '用户id';
comment on column sys_user_dept.dept_id is '部门id';
comment on column sys_user_dept.post_id is '岗位id';
comment on column sys_user_dept.is_primary is '是否用户主部门';
comment on column sys_user_dept.is_leader is '是否部门负责人';

-- 10.角色信息
drop table if exists sys_role;
create table sys_role(
    role_id     serial primary key,
    tenant_id   int4 not null,
    role_code   character varying(100) not null,
    role_name   character varying(64)  not null,
    role_type   character varying(64),
    role_status int2 not null default 1,
    is_template int2 not null default 0,
    remark      character varying(200),
    create_by   character varying(64),
    create_time timestamptz,
    update_by   character varying(64),
    update_time timestamptz
);
create unique index sys_role_role_code on sys_role(tenant_id, role_code);
comment on table sys_role is '角色信息';
comment on column sys_role.role_id is '角色id';
comment on column sys_role.tenant_id is '租户id';
comment on column sys_role.role_code is '角色编码';
comment on column sys_role.role_name is '角色名称';
comment on column sys_role.role_type is '角色类型';
comment on column sys_role.role_status is '角色状态 1启用 2停用';
comment on column sys_role.is_template is '是否新租户模板数据 1是 0否，仅system租户有效，复制后置0';
comment on column sys_role.remark is '备注';
comment on column sys_role.create_by is '创建人';
comment on column sys_role.create_time is '创建时间';
comment on column sys_role.update_by is '更新人';
comment on column sys_role.update_time is '更新时间';

-- 11.用户角色
drop table if exists sys_user_role;
create table sys_user_role(
    tenant_id   int4 not null,
    user_id     int4 not null,
    role_id     int4 not null,
    grant_type  character varying(32) not null default 'direct',
    granted_by  character varying(64),
    granted_time timestamptz not null default current_timestamp,
    constraint sys_user_role_pkey primary key (tenant_id, user_id, role_id)
);
comment on table sys_user_role is '用户角色';
comment on column sys_user_role.tenant_id is '租户id';
comment on column sys_user_role.user_id is '用户id';
comment on column sys_user_role.role_id is '角色id';
comment on column sys_user_role.grant_type is '授权类型，默认直接授权';
comment on column sys_user_role.granted_by is '授权人';
comment on column sys_user_role.granted_time is '授权时间';

-- 12.菜单信息
drop table if exists sys_menu;
create table sys_menu
(
    menu_id      serial primary key,
    parent_id    int4                   default 0,
    tenant_id    int4                  not null,
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
    is_frame     int2                   default 1,
    is_cache     int2                   default 1,
    is_visible   int2                   default 1,
    is_protected int2                   default 1,
    is_template  int2                  not null default 0,
    remark       character varying(255),
    create_by    character varying(64),
    create_time  timestamptz,
    update_by    character varying(64),
    update_time  timestamptz
);
comment on table sys_menu is '菜单信息';
comment on column sys_menu.menu_id is '菜单id';
comment on column sys_menu.parent_id is '父菜单id';
comment on column sys_menu.tenant_id is '租户id';
comment on column sys_menu.menu_module is '所属模块编码';
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
comment on column sys_menu.is_template is '是否新租户模板数据 1是 0否，仅system租户有效，复制后置0';
comment on column sys_menu.remark is '备注';
comment on column sys_menu.create_by is '创建人';
comment on column sys_menu.create_time is '创建时间';
comment on column sys_menu.update_by is '更新人';
comment on column sys_menu.update_time is '更新时间';

-- 13.角色菜单
drop table if exists sys_role_menu;
create table sys_role_menu
(
    tenant_id int4 not null,
    role_id   int4 not null,
    menu_id   int4 not null,
    scope_id  int4,
    constraint sys_role_menu_pkey primary key (role_id, menu_id)
);
create index sys_role_menu_tenant on sys_role_menu(tenant_id, role_id);
comment on table sys_role_menu is '角色菜单';
comment on column sys_role_menu.tenant_id is '租户id';
comment on column sys_role_menu.role_id is '角色id';
comment on column sys_role_menu.menu_id is '菜单id';
comment on column sys_role_menu.scope_id is '数据权限id，模板复制时未复制数据权限则置空';

-- 14.数据权限
drop table if exists sys_scope;
create table sys_scope
(
    scope_id      serial primary key,
    tenant_id     int4 not null,
    scope_name    character varying(255),
    scope_module  character varying(64),
    scope_status  int2 default 1,
    scope_content jsonb default '{}',
    remark        varchar(200),
    create_by     character varying(64),
    create_time   timestamptz,
    update_by     character varying(64),
    update_time   timestamptz
);
comment on table sys_scope is '数据权限';
comment on column sys_scope.scope_id is '权限id';
comment on column sys_scope.tenant_id is '租户id';
comment on column sys_scope.scope_name is '权限名称';
comment on column sys_scope.scope_module is '权限模块';
comment on column sys_scope.scope_status is '权限状态';
comment on column sys_scope.scope_content is '权限规则';
comment on column sys_scope.remark is '备注';

-- 15.LDAP配置
drop table if exists sys_auth_ldap;
create table sys_auth_ldap
(
    ldap_id          serial primary key,
    ldap_status      int2 not null default 0,
    ldap_url         character varying(256),
    ldap_user        character varying(128),
    ldap_passwd      character varying(128),
    base_dn          character varying(256),
    readonly         int2 not null default 0,
    user_dn          character varying(256),
    user_class       character varying(64),
    account_property character varying(64),
    name_property    character varying(64),
    email_property   character varying(64),
    phone_property   character varying(64),
    post_property    character varying(64),
    dept_property    character varying(64),
    leader_property  character varying(64),
    info_property    character varying(64),
    environment      jsonb not null default '{}'::jsonb,
    create_by        character varying(64),
    create_time      timestamptz,
    update_by        character varying(64),
    update_time      timestamptz
);
comment on table sys_auth_ldap is 'LDAP配置';
comment on column sys_auth_ldap.ldap_id is 'LDAP配置id';
comment on column sys_auth_ldap.ldap_status is 'LDAP状态 0关闭 1开启';
comment on column sys_auth_ldap.ldap_url is 'LDAP地址';
comment on column sys_auth_ldap.ldap_user is 'LDAP绑定用户';
comment on column sys_auth_ldap.ldap_passwd is 'LDAP绑定密码';
comment on column sys_auth_ldap.base_dn is '基础搜索DN';
comment on column sys_auth_ldap.readonly is '是否只读连接 0否 1是';
comment on column sys_auth_ldap.user_dn is '用户搜索DN';
comment on column sys_auth_ldap.user_class is '用户对象类';
comment on column sys_auth_ldap.account_property is '用户名属性';
comment on column sys_auth_ldap.name_property is '姓名属性';
comment on column sys_auth_ldap.email_property is '邮箱属性';
comment on column sys_auth_ldap.phone_property is '电话属性';
comment on column sys_auth_ldap.post_property is '岗位属性';
comment on column sys_auth_ldap.dept_property is '部门属性';
comment on column sys_auth_ldap.leader_property is '上级用户属性';
comment on column sys_auth_ldap.info_property is '用户信息属性';
comment on column sys_auth_ldap.environment is 'LDAP环境属性';
comment on column sys_auth_ldap.create_by is '创建人';
comment on column sys_auth_ldap.create_time is '创建时间';
comment on column sys_auth_ldap.update_by is '更新人';
comment on column sys_auth_ldap.update_time is '更新时间';

-- 16.认证提供方
drop table if exists sys_auth_provider;
create table sys_auth_provider
(
    provider_id     serial primary key,
    tenant_id       int4 not null,
    provider_code   character varying(64) not null,
    provider_type   character varying(32) not null default 'oauth',
    provider_name   character varying(64) not null,
    provider_icon   character varying(512),
    provider_tip    character varying(256),
    provider_sort   int4 not null default 0,
    link_url        character varying(512),
    client_id       character varying(128),
    client_secret   character varying(512),
    auth_url        character varying(512),
    redirect_url    character varying(512),
    grant_type      character varying(64),
    response_type   character varying(64),
    auth_scope      character varying(256),
    provider_config jsonb not null default '{}'::jsonb,
    status          int2 not null default 1,
    create_by       character varying(64),
    create_time     timestamptz not null default current_timestamp,
    update_by       character varying(64),
    update_time     timestamptz
);
create unique index sys_auth_provider_code_uq
    on sys_auth_provider(tenant_id, provider_code);
create index sys_auth_provider_entry
    on sys_auth_provider(tenant_id, status, provider_sort);
comment on table sys_auth_provider is '租户认证及登录入口提供方，OAuth仅为其中一种类型';
comment on column sys_auth_provider.provider_id is '认证提供方id';
comment on column sys_auth_provider.tenant_id is '租户id';
comment on column sys_auth_provider.provider_code is '租户内唯一提供方编码，如cowave、gitlab、wechat';
comment on column sys_auth_provider.provider_type is '提供方类型，如oauth、oidc、saml、link';
comment on column sys_auth_provider.provider_name is '登录入口名称';
comment on column sys_auth_provider.provider_icon is '登录入口图标';
comment on column sys_auth_provider.provider_tip is '登录入口提示';
comment on column sys_auth_provider.provider_sort is '登录入口排序';
comment on column sys_auth_provider.link_url is '直接跳转地址，link类型使用';
comment on column sys_auth_provider.client_id is 'OAuth客户端id';
comment on column sys_auth_provider.client_secret is 'OAuth客户端密钥';
comment on column sys_auth_provider.auth_url is 'OAuth授权服务地址';
comment on column sys_auth_provider.redirect_url is 'OAuth应用回调地址';
comment on column sys_auth_provider.grant_type is 'OAuth授权类型';
comment on column sys_auth_provider.response_type is 'OAuth响应类型';
comment on column sys_auth_provider.auth_scope is 'OAuth授权范围';
comment on column sys_auth_provider.provider_config is '不同认证类型的扩展配置';
comment on column sys_auth_provider.status is '状态 0关闭 1开启';
comment on column sys_auth_provider.create_by is '创建人';
comment on column sys_auth_provider.create_time is '创建时间';
comment on column sys_auth_provider.update_by is '更新人';
comment on column sys_auth_provider.update_time is '更新时间';

-- 17.用户外部身份
drop table if exists sys_auth_user;
create table sys_auth_user
(
    id               bigserial primary key,
    user_id          int4                   not null,
    identity_type    character varying(32)  not null,
    ldap_id          int4,
    provider_id      int4,
    external_subject character varying(256) not null,
    user_account     character varying(128) not null,
    user_passwd      character varying(256),
    user_name        character varying(128),
    user_avatar      character varying(1024),
    user_phone       character varying(64),
    user_email       character varying(128),
    user_post        character varying(128),
    user_dept        character varying(256),
    user_leader      character varying(128),
    identity_info    jsonb                  not null default '{}'::jsonb,
    auth_status      int2                   not null default 1,
    last_login_time  timestamptz,
    last_sync_time   timestamptz,
    create_time      timestamptz            not null default current_timestamp,
    update_time      timestamptz
);
create unique index sys_auth_user_ldap_subject_uq
    on sys_auth_user(ldap_id, external_subject)
    where ldap_id is not null;
create unique index sys_auth_user_provider_subject_uq
    on sys_auth_user(provider_id, external_subject)
    where provider_id is not null;
create index sys_auth_user_user
    on sys_auth_user(user_id, auth_status);
comment on table sys_auth_user is '用户外部身份，统一保存LDAP和认证提供方身份';
comment on column sys_auth_user.id is '外部身份id';
comment on column sys_auth_user.user_id is '绑定的全局用户id';
comment on column sys_auth_user.identity_type is '身份类型：ldap、oauth';
comment on column sys_auth_user.ldap_id is 'LDAP配置id，LDAP身份使用';
comment on column sys_auth_user.provider_id is '认证提供方id，外部认证身份使用';
comment on column sys_auth_user.external_subject is '外部系统稳定用户标识，如entryUUID或OAuth subject';
comment on column sys_auth_user.user_account is '外部系统用户账号';
comment on column sys_auth_user.user_passwd is '外部身份密码，LDAP同步场景可正常保存';
comment on column sys_auth_user.user_name is '外部系统用户名称';
comment on column sys_auth_user.user_avatar is '外部系统头像地址';
comment on column sys_auth_user.user_phone is '外部系统用户电话';
comment on column sys_auth_user.user_email is '外部系统用户邮箱';
comment on column sys_auth_user.user_post is '外部系统岗位信息';
comment on column sys_auth_user.user_dept is '外部系统部门信息';
comment on column sys_auth_user.user_leader is '外部系统上级用户标识';
comment on column sys_auth_user.identity_info is '外部身份扩展原始信息';
comment on column sys_auth_user.auth_status is '身份状态 1启用 2停用';
comment on column sys_auth_user.last_login_time is '最近登录时间';
comment on column sys_auth_user.last_sync_time is '最近同步时间';
comment on column sys_auth_user.create_time is '创建时间';
comment on column sys_auth_user.update_time is '更新时间';

-- 18.认证应用
drop table if exists sys_auth_app;
create table sys_auth_app
(
    app_id        serial primary key,
    tenant_id     int4 not null,
    app_name      character varying(64) not null,
    app_type      character varying(32) not null default 'oauth',
    app_visible   character varying(32) not null default 'sys',
    app_status    int2 not null default 1,
    app_sort      int4 not null default 0,
    card_name     character varying(64),
    card_icon     character varying(128),
    link_url      character varying(512),
    client_id     character varying(128),
    client_secret character varying(512),
    grant_type    character varying(64)[],
    auth_scope    character varying(128)[],
    redirect_url  character varying(512),
    create_by     character varying(64),
    create_time   timestamptz,
    update_by     character varying(64),
    update_time   timestamptz
);
comment on table sys_auth_app is '认证应用';
comment on column sys_auth_app.app_id is '应用id';
comment on column sys_auth_app.tenant_id is '租户id';
comment on column sys_auth_app.app_name is '应用名称';
comment on column sys_auth_app.app_type is '应用类型 oauth/link';
comment on column sys_auth_app.app_visible is '可见性 public/all/sys';
comment on column sys_auth_app.app_status is '状态 1启用 2停用';
comment on column sys_auth_app.app_sort is '排序';
comment on column sys_auth_app.card_name is '卡片名称';
comment on column sys_auth_app.card_icon is '卡片图标';
comment on column sys_auth_app.link_url is '跳转地址';
comment on column sys_auth_app.client_id is 'OAuth客户端id';
comment on column sys_auth_app.client_secret is 'OAuth客户端密钥';
comment on column sys_auth_app.grant_type is '授权类型';
comment on column sys_auth_app.auth_scope is '授权范围';
comment on column sys_auth_app.redirect_url is '重定向地址';
comment on column sys_auth_app.create_by is '创建人';
comment on column sys_auth_app.create_time is '创建时间';
comment on column sys_auth_app.update_by is '更新人';
comment on column sys_auth_app.update_time is '更新时间';

-- 19.认证应用菜单
drop table if exists sys_auth_app_menu;
create table sys_auth_app_menu
(
    menu_id      serial primary key,
    parent_id    int4 default 0,
    tenant_id    int4 not null,
    app_id       int4 not null,
    menu_module  character varying(64),
    menu_name    character varying(64) not null,
    menu_order   int4 not null default 0,
    menu_permit  character varying(255),
    menu_path    character varying(255) default '#',
    menu_param   character varying(255),
    menu_type    char(1) not null,
    menu_icon    character varying(100) default '#',
    component    character varying(255),
    menu_status  int2 not null default 1,
    is_frame     int2 not null default 1,
    is_cache     int2 not null default 1,
    is_visible   int2 not null default 1,
    is_protected int2 not null default 1,
    remark       character varying(255),
    create_by    character varying(64),
    create_time  timestamptz,
    update_by    character varying(64),
    update_time  timestamptz
);
comment on table sys_auth_app_menu is '认证应用菜单';
comment on column sys_auth_app_menu.menu_id is '菜单id';
comment on column sys_auth_app_menu.parent_id is '父菜单id';
comment on column sys_auth_app_menu.tenant_id is '租户id';
comment on column sys_auth_app_menu.app_id is '应用id';
comment on column sys_auth_app_menu.menu_module is '菜单模块';
comment on column sys_auth_app_menu.menu_name is '菜单名称';
comment on column sys_auth_app_menu.menu_order is '菜单顺序';
comment on column sys_auth_app_menu.menu_permit is '权限标识';
comment on column sys_auth_app_menu.menu_path is '菜单路径';
comment on column sys_auth_app_menu.menu_param is '路由参数';
comment on column sys_auth_app_menu.menu_type is '菜单类型：M目录、C菜单、B按钮';
comment on column sys_auth_app_menu.menu_icon is '菜单图标';
comment on column sys_auth_app_menu.component is '组件路径';
comment on column sys_auth_app_menu.menu_status is '菜单状态 1启用 2停用';
comment on column sys_auth_app_menu.is_frame is '是否内部链接 1是 0否';
comment on column sys_auth_app_menu.is_cache is '是否缓存 1是 0否';
comment on column sys_auth_app_menu.is_visible is '是否显示 1是 0否';
comment on column sys_auth_app_menu.is_protected is '是否受保护 1是 0否';
comment on column sys_auth_app_menu.remark is '备注';
comment on column sys_auth_app_menu.create_by is '创建人';
comment on column sys_auth_app_menu.create_time is '创建时间';
comment on column sys_auth_app_menu.update_by is '更新人';
comment on column sys_auth_app_menu.update_time is '更新时间';

-- 20.角色授权应用
drop table if exists sys_role_app;
create table sys_role_app
(
    tenant_id int4 not null,
    role_id   int4 not null,
    app_id    int4 not null,
    constraint sys_role_app_pkey primary key (tenant_id, role_id, app_id)
);
comment on table sys_role_app is '角色授权应用';
comment on column sys_role_app.tenant_id is '租户id';
comment on column sys_role_app.role_id is '角色id';
comment on column sys_role_app.app_id is '应用id';

-- 21.角色授权应用菜单
drop table if exists sys_role_app_menu;
create table sys_role_app_menu
(
    tenant_id int4 not null,
    role_id   int4 not null,
    app_id    int4 not null,
    menu_id   int4 not null,
    constraint sys_role_app_menu_pkey primary key (tenant_id, role_id, app_id, menu_id)
);
comment on table sys_role_app_menu is '角色授权应用菜单';
comment on column sys_role_app_menu.tenant_id is '租户id';
comment on column sys_role_app_menu.role_id is '角色id';
comment on column sys_role_app_menu.app_id is '应用id';
comment on column sys_role_app_menu.menu_id is '菜单id';

-- 22.用户ApiToken
drop table if exists sys_user_token;
create table sys_user_token
(
    token_id     serial primary key,
    tenant_id    int4 not null,
    user_id      int4 not null,
    token_name   character varying(128) not null,
    token_value  character varying(1024) not null,
    token_status int2 not null default 1,
    expire_time  timestamptz,
    ip_rule      character varying(512),
    create_by    character varying(64),
    create_time  timestamptz not null default current_timestamp,
    update_by    character varying(64),
    update_time  timestamptz
);
create unique index sys_user_token_value_uq on sys_user_token(token_value);
comment on table sys_user_token is '用户API Token';
comment on column sys_user_token.token_id is '令牌id';
comment on column sys_user_token.tenant_id is '租户id';
comment on column sys_user_token.user_id is '用户id';
comment on column sys_user_token.token_name is '令牌名称';
comment on column sys_user_token.token_value is '令牌值';
comment on column sys_user_token.token_status is '令牌状态 1启用 2停用';
comment on column sys_user_token.expire_time is '到期时间';
comment on column sys_user_token.ip_rule is 'IP访问限制，支持多个CIDR';
comment on column sys_user_token.create_by is '创建人';
comment on column sys_user_token.create_time is '创建时间';
comment on column sys_user_token.update_by is '更新人';
comment on column sys_user_token.update_time is '更新时间';

-- 23.用户ApiToken权限
drop table if exists sys_user_token_menu;
create table sys_user_token_menu
(
    tenant_id int4 not null,
    token_id int4 not null,
    permit   character varying(255) not null,
    scope_id int4,
    constraint sys_user_token_menu_pkey primary key (tenant_id, token_id, permit)
);
comment on table sys_user_token_menu is '用户API Token权限';
comment on column sys_user_token_menu.tenant_id is '租户id';
comment on column sys_user_token_menu.token_id is '令牌id';
comment on column sys_user_token_menu.permit is '权限符';
comment on column sys_user_token_menu.scope_id is '数据权限id';

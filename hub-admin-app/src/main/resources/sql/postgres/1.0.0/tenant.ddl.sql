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
comment on column sys_tenant.status is '租户状态';
comment on column sys_tenant.expire_time is '到期时间';
comment on column sys_tenant.remark is '备注';
comment on column sys_tenant.create_by is '创建人';
comment on column sys_tenant.create_time is '创建时间';
comment on column sys_tenant.update_by is '更新人';
comment on column sys_tenant.update_time is '更新时间';

-- 配置模板
drop table if exists sys_tenant_config;
CREATE TABLE sys_tenant_config
(
    config_id    serial primary key,
    config_name  varchar(100) DEFAULT '',
    config_key   varchar(100) DEFAULT '',
    config_value varchar(500) DEFAULT '',
    value_type   varchar(64),
    value_parser varchar(100),
    is_default   int2         DEFAULT 0,
    remark       varchar(500) DEFAULT NULL,
    create_by    varchar(64),
    create_time  timestamp,
    update_by    varchar(64),
    update_time  timestamp
);
create unique index sys_tenant_config_key on sys_tenant_config(config_key);
comment on table sys_tenant_config is '系统配置';
comment on column sys_tenant_config.config_id is '参数id';
comment on column sys_tenant_config.config_name is '参数名称';
comment on column sys_tenant_config.config_key is '参数键';
comment on column sys_tenant_config.config_value is '参数值';
comment on column sys_tenant_config.value_parser is '值转换器';
comment on column sys_tenant_config.value_type is '值类型';
comment on column sys_tenant_config.is_default is '是否默认 1是 0否';
comment on column sys_tenant_config.remark is '备注';
comment on column sys_tenant_config.create_by is '创建人';
comment on column sys_tenant_config.create_time is '创建时间';
comment on column sys_tenant_config.update_by is '更新人';
comment on column sys_tenant_config.update_time is '更新时间';

-- 角色模板
drop table if exists sys_tenant_role;
create table sys_tenant_role(
    role_id     serial primary key,
    role_code   character varying(100) not null,
    role_name   character varying(64)  not null,
    role_type   character varying(64),
    remark      character varying(200),
    create_by   character varying(64),
    create_time timestamp,
    update_by   character varying(64),
    update_time timestamp
);
create unique index sys_tenant_role_code on sys_tenant_role(role_code);
comment on table sys_tenant_role is '角色信息';
comment on column sys_tenant_role.role_id is '角色id';
comment on column sys_tenant_role.role_code is '角色编码';
comment on column sys_tenant_role.role_name is '角色名称';
comment on column sys_tenant_role.role_type is '角色类型';
comment on column sys_tenant_role.remark is '备注';
comment on column sys_tenant_role.create_by is '创建人';
comment on column sys_tenant_role.create_time is '创建时间';
comment on column sys_tenant_role.update_by is '更新人';
comment on column sys_tenant_role.update_time is '更新时间';

-- 菜单模板
drop table if exists sys_tenant_menu;
create table sys_tenant_menu
(
    menu_id      serial primary key,
    parent_id    int4                   default 0,
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
comment on table sys_tenant_menu is '菜单信息';
comment on column sys_tenant_menu.menu_id is '菜单id';
comment on column sys_tenant_menu.parent_id is '父菜单id';
comment on column sys_tenant_menu.menu_name is '菜单名称';
comment on column sys_tenant_menu.menu_order is '菜单顺序';
comment on column sys_tenant_menu.menu_permit is '权限标识';
comment on column sys_tenant_menu.menu_path is '菜单路径';
comment on column sys_tenant_menu.menu_param is '路径参数';
comment on column sys_tenant_menu.menu_type is '菜单类型：M:目录 C:菜单 B:按钮';
comment on column sys_tenant_menu.menu_icon is '菜单图标';
comment on column sys_tenant_menu.component is '组件路径';
comment on column sys_tenant_menu.menu_status is '菜单状态 1启用 2停用';
comment on column sys_tenant_menu.is_frame is '是否内部链接 1是 0否';
comment on column sys_tenant_menu.is_cache is '是否缓存 1是 0否';
comment on column sys_tenant_menu.is_visible is '是否显示 1是 0否';
comment on column sys_tenant_menu.is_protected is '是否受保护的菜单 1是 0否';
comment on column sys_tenant_menu.remark is '备注';
comment on column sys_tenant_menu.create_by is '创建人';
comment on column sys_tenant_menu.create_time is '创建时间';
comment on column sys_tenant_menu.update_by is '更新人';
comment on column sys_tenant_menu.update_time is '更新时间';

-- 领域模块模板
drop table if exists sys_tenant_module;
create table sys_tenant_module
(
    module_id    serial primary key,
    parent_code  character varying(100),
    module_code  character varying(100) not null,
    module_name  character varying(100),
    module_order int4 default 0,
    status       int2 default 1,
    remark       character varying(200),
    create_by    character varying(64),
    create_time  timestamp,
    update_by    character varying(64),
    update_time  timestamp
);
create unique index sys_tenant_module_code on sys_tenant_module(module_code);
comment on table sys_tenant_module is '领域模块模板';
comment on column sys_tenant_module.module_id is '模块id';
comment on column sys_tenant_module.parent_code is '上级模块编码';
comment on column sys_tenant_module.module_code is '模块编码';
comment on column sys_tenant_module.module_name is '模块名称';
comment on column sys_tenant_module.module_order is '模块排序';
comment on column sys_tenant_module.status is '状态';
comment on column sys_tenant_module.remark is '备注';
comment on column sys_tenant_module.create_by is '创建人';
comment on column sys_tenant_module.create_time is '创建时间';
comment on column sys_tenant_module.update_by is '更新人';
comment on column sys_tenant_module.update_time is '更新时间';

-- 字典类型模板
drop table if exists sys_tenant_dict_type;
create table sys_tenant_dict_type
(
    type_id     serial primary key,
    module_code character varying(100),
    type_code   character varying(100) not null,
    type_name   character varying(100),
    status      int2 default 1,
    remark      character varying(200),
    create_by   character varying(64),
    create_time timestamp,
    update_by   character varying(64),
    update_time timestamp
);
create unique index sys_tenant_dict_type_code on sys_tenant_dict_type(type_code);
comment on table sys_tenant_dict_type is '字典类型模板';
comment on column sys_tenant_dict_type.type_id is '类型id';
comment on column sys_tenant_dict_type.module_code is '所属模块编码';
comment on column sys_tenant_dict_type.type_code is '类型编码';
comment on column sys_tenant_dict_type.type_name is '类型名称';
comment on column sys_tenant_dict_type.status is '状态';
comment on column sys_tenant_dict_type.remark is '备注';
comment on column sys_tenant_dict_type.create_by is '创建人';
comment on column sys_tenant_dict_type.create_time is '创建时间';
comment on column sys_tenant_dict_type.update_by is '更新人';
comment on column sys_tenant_dict_type.update_time is '更新时间';

-- 字典项模板
drop table if exists sys_tenant_dict;
create table sys_tenant_dict
(
    id           bigserial primary key,
    type_code    character varying(100),
    dict_code    character varying(100) not null,
    dict_name    character varying(100),
    dict_value   character varying(100),
    value_type   character varying(64),
    value_parser character varying(100),
    dict_order   int4 default 0,
    is_default   int2 default 0,
    css          character varying(100),
    status       int2 default 1,
    remark       character varying(200),
    create_by    character varying(64),
    create_time  timestamp,
    update_by    character varying(64),
    update_time  timestamp
);
create unique index sys_tenant_dict_code on sys_tenant_dict(dict_code);
comment on table sys_tenant_dict is '字典项模板';
comment on column sys_tenant_dict.id is '字典项id';
comment on column sys_tenant_dict.type_code is '所属类型编码';
comment on column sys_tenant_dict.dict_code is '字典项编码';
comment on column sys_tenant_dict.dict_name is '字典项名称';
comment on column sys_tenant_dict.dict_value is '字典项值';
comment on column sys_tenant_dict.value_type is '值类型';
comment on column sys_tenant_dict.value_parser is '值转换器';
comment on column sys_tenant_dict.dict_order is '字典项排序';
comment on column sys_tenant_dict.is_default is '是否默认 1是 0否';
comment on column sys_tenant_dict.css is '展示样式';
comment on column sys_tenant_dict.status is '状态';
comment on column sys_tenant_dict.remark is '备注';
comment on column sys_tenant_dict.create_by is '创建人';
comment on column sys_tenant_dict.create_time is '创建时间';
comment on column sys_tenant_dict.update_by is '更新人';
comment on column sys_tenant_dict.update_time is '更新时间';

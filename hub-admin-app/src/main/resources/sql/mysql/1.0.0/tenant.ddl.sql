-- 租户信息
drop table if exists sys_tenant;
create table sys_tenant
(
    tenant_id    varchar(64) primary key comment '租户id',
    tenant_name  varchar(128) comment '租户名称',
    user_limit   int default 1000 comment '用户上限',
    user_count   int default 0 comment '用户统计',
    user_index   int default 0 comment '用户序号',
    status       smallint default 1 comment '租户状态',
    expire_time  datetime comment '到期时间',
    title        varchar(64) comment '租户标题',
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

-- 配置模板
drop table if exists sys_tenant_config;
create table sys_tenant_config
(
    config_id    int auto_increment primary key comment '参数id',
    config_name  varchar(100) default '' comment '参数名称',
    config_key   varchar(100) default '' comment '参数键',
    config_value varchar(500) default '' comment '参数值',
    value_type   varchar(64) comment '值类型',
    value_parser varchar(100) comment '值转换器',
    is_default   smallint default 0 comment '是否默认 1是 0否',
    remark       varchar(500) default null comment '备注',
    create_by    varchar(64) comment '创建人',
    create_time  datetime comment '创建时间',
    update_by    varchar(64) comment '更新人',
    update_time  datetime comment '更新时间'
) comment='配置模板';
create unique index sys_tenant_config_key on sys_tenant_config(config_key);

-- 角色模板
drop table if exists sys_tenant_role;
create table sys_tenant_role
(
    role_id     int auto_increment primary key comment '角色id',
    role_code   varchar(100) not null comment '角色编码',
    role_name   varchar(64)  not null comment '角色名称',
    role_type   varchar(64) comment '角色类型',
    remark      varchar(200) comment '备注',
    create_by   varchar(64) comment '创建人',
    create_time datetime comment '创建时间',
    update_by   varchar(64) comment '更新人',
    update_time datetime comment '更新时间'
) comment='角色模板';
create unique index sys_tenant_role_code on sys_tenant_role(role_code);

-- 菜单模板
drop table if exists sys_tenant_menu;
create table sys_tenant_menu
(
    menu_id      int auto_increment primary key comment '菜单id',
    parent_id    int default 0 comment '父菜单id',
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
) comment='菜单模板';

-- 领域模块模板
drop table if exists sys_tenant_module;
create table sys_tenant_module
(
    module_id    int auto_increment primary key comment '模块id',
    parent_code  varchar(100) comment '上级模块编码',
    module_code  varchar(100) not null comment '模块编码',
    module_name  varchar(100) comment '模块名称',
    module_order int default 0 comment '模块排序',
    status       smallint default 1 comment '状态',
    remark       varchar(200) comment '备注',
    create_by    varchar(64) comment '创建人',
    create_time  datetime comment '创建时间',
    update_by    varchar(64) comment '更新人',
    update_time  datetime comment '更新时间'
) comment='领域模块模板';
create unique index sys_tenant_module_code on sys_tenant_module(module_code);

-- 字典类型模板
drop table if exists sys_tenant_dict_type;
create table sys_tenant_dict_type
(
    type_id     int auto_increment primary key comment '类型id',
    module_code varchar(100) comment '所属模块编码',
    type_code   varchar(100) not null comment '类型编码',
    type_name   varchar(100) comment '类型名称',
    status      smallint default 1 comment '状态',
    remark      varchar(200) comment '备注',
    create_by   varchar(64) comment '创建人',
    create_time datetime comment '创建时间',
    update_by   varchar(64) comment '更新人',
    update_time datetime comment '更新时间'
) comment='字典类型模板';
create unique index sys_tenant_dict_type_code on sys_tenant_dict_type(type_code);

-- 字典项模板
drop table if exists sys_tenant_dict;
create table sys_tenant_dict
(
    id           bigint auto_increment primary key comment '字典项id',
    type_code    varchar(100) comment '所属类型编码',
    dict_code    varchar(100) not null comment '字典项编码',
    dict_name    varchar(100) comment '字典项名称',
    dict_value   varchar(100) comment '字典项值',
    value_type   varchar(64) comment '值类型',
    value_parser varchar(100) comment '值转换器',
    dict_order   int default 0 comment '字典项排序',
    is_default   smallint default 0 comment '是否默认 1是 0否',
    css          varchar(100) comment '展示样式',
    status       smallint default 1 comment '状态',
    remark       varchar(200) comment '备注',
    create_by    varchar(64) comment '创建人',
    create_time  datetime comment '创建时间',
    update_by    varchar(64) comment '更新人',
    update_time  datetime comment '更新时间'
) comment='字典项模板';
create unique index sys_tenant_dict_code on sys_tenant_dict(dict_code);

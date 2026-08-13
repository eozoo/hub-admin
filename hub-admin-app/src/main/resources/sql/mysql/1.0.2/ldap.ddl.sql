-- ldap 配置
drop table if exists sys_ldap;
create table sys_ldap
(
    tenant_id        varchar(64) primary key comment '租户id',
    ldap_status      smallint default 0 comment 'Ldap状态',
    ldap_url         varchar(128) comment 'Ldap地址',
    ldap_user        varchar(128) comment 'Ldap用户',
    ldap_passwd      varchar(128) comment 'Ldap密码',
    base_dn          varchar(128) comment '基本搜索DN',
    readonly         smallint default 0 comment '是否匿名进行只读连接',
    user_dn          varchar(64) comment '用户搜索DN',
    user_class       varchar(64) comment '用户对象类',
    account_property varchar(64) comment '用户名属性',
    name_property    varchar(64) comment '姓名属性',
    email_property   varchar(64) comment '邮箱属性',
    phone_property   varchar(64) comment '电话属性',
    post_property    varchar(64) comment '岗位属性',
    dept_property    varchar(64) comment '部门属性',
    leader_property  varchar(64) comment '上级用户属性',
    info_property    varchar(64) comment '用户信息属性',
    environment      jsonb comment 'LDAP环境属性',
    role_code        varchar(64) comment '用户默认角色',
    create_by        varchar(64) comment '创建人',
    create_time      datetime comment '创建时间',
    update_by        varchar(64) comment '更新人',
    update_time      datetime comment '更新时间'
) comment='ldap配置';

-- ldap 用户
drop table if exists sys_ldap_user;
create table sys_ldap_user
(
    id           bigint auto_increment primary key comment 'id',
    tenant_id    varchar(64) comment '租户id',
    user_account varchar(64) comment '用户账号',
    user_passwd  varchar(64) comment '用户密码',
    user_name    varchar(64) comment '用户名称',
    user_phone   varchar(64) comment '用户电话',
    user_email   varchar(128) comment '用户邮箱',
    user_post    varchar(128) comment '用户岗位',
    user_dept    varchar(128) comment '用户部门',
    user_leader  varchar(64) comment '上级用户',
    user_info    varchar(128) comment '用户信息',
    create_time  datetime comment '创建时间',
    update_time  datetime comment '更新时间'
) comment='ldap用户';
create unique index sys_ldap_user_unique on sys_ldap_user(tenant_id, user_account);

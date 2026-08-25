-- 会员三方授权入口
drop table if exists hub_oauth;
create table hub_oauth
(
    oauth_id      serial primary key,
    tenant_id     character varying(64),
    oauth_provider character varying(64),
    oauth_type    character varying(64),
    oauth_name    character varying(64),
    oauth_icon    character varying(256),
    oauth_tip     character varying(256),
    link_url      character varying(256),
    oauth_sort    int4 default 0,
    app_id        character varying(64),
    app_secret    character varying(64),
    auth_url      character varying(256),
    redirect_url  character varying(256),
    grant_type    character varying(64) default 'authorization_code',
    response_type character varying(64) default 'code',
    auth_scope    character varying(128),
    role_code     character varying(64),
    status        int2 default 1,
    create_by     character varying(64),
    create_time   timestamp,
    update_by     character varying(64),
    update_time   timestamp
);
create unique index hub_oauth_unique on hub_oauth(tenant_id, oauth_provider);
comment on table hub_oauth is '会员三方授权入口';
comment on column hub_oauth.oauth_id is 'id';
comment on column hub_oauth.tenant_id is '租户id';
comment on column hub_oauth.oauth_provider is '授权提供方 cowave/gitlab/qq/wechat';
comment on column hub_oauth.oauth_type is '入口类型 oauth/link';
comment on column hub_oauth.oauth_name is '入口名称';
comment on column hub_oauth.oauth_icon is '入口图标';
comment on column hub_oauth.oauth_tip is '悬停提示';
comment on column hub_oauth.link_url is '跳转地址';
comment on column hub_oauth.oauth_sort is '排序';
comment on column hub_oauth.app_id is '应用id';
comment on column hub_oauth.app_secret is '应用密钥';
comment on column hub_oauth.auth_url is '授权服务url';
comment on column hub_oauth.redirect_url is '应用回调地址';
comment on column hub_oauth.grant_type is '授权类型';
comment on column hub_oauth.response_type is '响应类型';
comment on column hub_oauth.auth_scope is '授权范围';
comment on column hub_oauth.role_code is '默认角色';
comment on column hub_oauth.status is '状态 0 关闭 1开启';
comment on column hub_oauth.create_by is '创建人';
comment on column hub_oauth.create_time is '创建时间';
comment on column hub_oauth.update_by is '更新人';
comment on column hub_oauth.update_time is '更新时间';

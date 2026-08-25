-- 会员三方授权入口
drop table if exists hub_oauth;
create table hub_oauth
(
    oauth_id      int auto_increment primary key comment 'id',
    tenant_id     varchar(64) comment '租户id',
    oauth_provider varchar(64) comment '授权提供方 cowave/gitlab/qq/wechat',
    oauth_type    varchar(64) comment '入口类型 oauth/link',
    oauth_name    varchar(64) comment '入口名称',
    oauth_icon    varchar(256) comment '入口图标',
    oauth_tip     varchar(256) comment '悬停提示',
    link_url      varchar(256) comment '跳转地址',
    oauth_sort    int default 0 comment '排序',
    app_id        varchar(64) comment '应用id',
    app_secret    varchar(64) comment '应用密钥',
    auth_url      varchar(256) comment '授权服务url',
    redirect_url  varchar(256) comment '应用回调地址',
    grant_type    varchar(64) default 'authorization_code' comment '授权类型',
    response_type varchar(64) default 'code' comment '响应类型',
    auth_scope    varchar(128) comment '授权范围',
    role_code     varchar(64) comment '默认角色',
    status        smallint default 1 comment '状态 0 关闭 1开启',
    create_by     varchar(64) comment '创建人',
    create_time   datetime comment '创建时间',
    update_by     varchar(64) comment '更新人',
    update_time   datetime comment '更新时间',
    unique key hub_oauth_unique (tenant_id, oauth_provider)
) comment='会员三方授权入口';

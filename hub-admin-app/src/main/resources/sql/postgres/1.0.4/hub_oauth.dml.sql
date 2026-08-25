-- hub-home-ui 左侧入口（普通链接 + 授权入口）
-- 普通链接（link）：oauth_provider / app_id 等授权字段为空，图标先填前端静态路径兜底
-- 授权入口（oauth）：gitlab 三方授权（app_id/app_secret 占位待回填）+ cowave 系统用户（PKCE）
INSERT INTO hub_oauth ("tenant_id", "oauth_provider", "oauth_type", "oauth_name", "oauth_icon", "oauth_tip", "link_url", "oauth_sort", "app_id", "app_secret", "auth_url", "redirect_url", "grant_type", "response_type", "auth_scope", "role_code", "status", "create_by", "create_time", "update_by", "update_time")
VALUES ('cowave', NULL, 'link', 'Github', '/images/icon/github.png', '去 Github 看看', 'https://github.com/imsyy', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

INSERT INTO hub_oauth ("tenant_id", "oauth_provider", "oauth_type", "oauth_name", "oauth_icon", "oauth_tip", "link_url", "oauth_sort", "app_id", "app_secret", "auth_url", "redirect_url", "grant_type", "response_type", "auth_scope", "role_code", "status", "create_by", "create_time", "update_by", "update_time")
VALUES ('cowave', NULL, 'link', 'BiliBili', '/images/icon/bilibili.png', '(゜-゜)つロ 干杯 ~', 'https://space.bilibili.com/98544142', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

INSERT INTO hub_oauth ("tenant_id", "oauth_provider", "oauth_type", "oauth_name", "oauth_icon", "oauth_tip", "link_url", "oauth_sort", "app_id", "app_secret", "auth_url", "redirect_url", "grant_type", "response_type", "auth_scope", "role_code", "status", "create_by", "create_time", "update_by", "update_time")
VALUES ('cowave', NULL, 'link', 'QQ', '/images/icon/qq.png', '有什么事吗', 'https://res.abeim.cn/api/qq/?qq=1539250352', 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

INSERT INTO hub_oauth ("tenant_id", "oauth_provider", "oauth_type", "oauth_name", "oauth_icon", "oauth_tip", "link_url", "oauth_sort", "app_id", "app_secret", "auth_url", "redirect_url", "grant_type", "response_type", "auth_scope", "role_code", "status", "create_by", "create_time", "update_by", "update_time")
VALUES ('cowave', NULL, 'link', 'Email', '/images/icon/email.png', '去留言 ~', '/blog/comments', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- gitlab 三方授权入口（app_id/app_secret 占位值，需在 gitlab 平台新建独立应用后回填）
INSERT INTO hub_oauth ("tenant_id", "oauth_provider", "oauth_type", "oauth_name", "oauth_icon", "oauth_tip", "link_url", "oauth_sort", "app_id", "app_secret", "auth_url", "redirect_url", "grant_type", "response_type", "auth_scope", "role_code", "status", "create_by", "create_time", "update_by", "update_time")
VALUES ('cowave', 'gitlab', 'oauth', 'Gitlab', '/images/icon/gitlab.png', 'Gitlab用户', NULL, 3, 'replace_with_member_gitlab_app_id', 'replace_with_member_gitlab_app_secret', 'https://gitlab.cowave.com', 'http://localhost:3000/oauth/callback?provider=gitlab', 'authorization_code', 'code', 'read_user', 'role-readonly', 1, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- hub-admin 系统用户入口（cowave，走 PKCE）
INSERT INTO hub_oauth ("tenant_id", "oauth_provider", "oauth_type", "oauth_name", "oauth_icon", "oauth_tip", "link_url", "oauth_sort", "app_id", "app_secret", "auth_url", "redirect_url", "grant_type", "response_type", "auth_scope", "role_code", "status", "create_by", "create_time", "update_by", "update_time")
VALUES ('cowave', 'cowave', 'oauth', 'Hub Admin', '/images/icon/cowave.png', '控维系统用户', NULL, 5, '6ac6519451ed4ef09431aacccbcb1f5f', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

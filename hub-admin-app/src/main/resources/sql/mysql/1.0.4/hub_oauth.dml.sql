-- 会员 gitlab 三方授权入口
-- 注意：app_id / app_secret 为占位值，需在 gitlab 平台为会员线新建独立应用后回填
-- redirect_url 需指向 hub-home-ui 的会员回调地址（dev 为 http://localhost:3000）
INSERT INTO hub_oauth (tenant_id, server_type, oauth_name, oauth_icon, oauth_tip, oauth_sort, app_id, app_secret, auth_url, redirect_url, grant_type, response_type, auth_scope, role_code, status, create_by, create_time, update_by, update_time)
VALUES ('cowave', 'gitlab', 'Gitlab', '/images/icon/gitlab.png', '会员登录', 0, 'replace_with_member_gitlab_app_id', 'replace_with_member_gitlab_app_secret', 'https://gitlab.cowave.com', 'http://localhost:3000/oauth/callback?provider=gitlab', 'authorization_code', 'code', 'read_user', 'role-readonly', 1, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

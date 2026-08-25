-- 配置
INSERT INTO sys_oauth (tenant_id, status, role_code, server_type, app_id, app_secret, auth_url, redirect_url, grant_type, response_type, auth_scope, create_by, create_time, update_by, update_time)
VALUES ('cowave', 1, 'role-readonly', 'gitlab', '155cbf79f0e8dc8c6b912b2ac033e741e9efb9ecffe661571693d392c279bc5d', '5eb6f1172bb175145f29d772f8ebacde5e37be4743cebbf917da2e8c68d999ec', 'https://gitlab.cowave.com', 'http://10.64.4.74:81/oauth/gitlab', 'authorization_code', 'code', 'read_user', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

INSERT INTO hub_app (tenant_id, client_name, card_name, card_icon, app_type, app_visible, link_url, app_sort, status, client_id, client_secret, grant_type, auth_scope, redirect_url, create_by, create_time, update_by, update_time)
VALUES ('cowave', 'hub-home', 'Hub论坛', 'CompactDisc', 'link', 'public', '/blog', 0, 1, '6ac6519451ed4ef09431aacccbcb1f5f', '4a2e671fbd074f238e80c7f5566f8f7a', '{authorization_code}', '{read_user}', 'http://localhost:3000/oauth/callback?provider=cowave', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- hub-admin 系统管理入口（link 直接跳转，仅系统用户可见）
INSERT INTO hub_app (tenant_id, client_name, card_name, card_icon, app_type, app_visible, link_url, app_sort, status, client_id, client_secret, grant_type, auth_scope, redirect_url, create_by, create_time, update_by, update_time)
VALUES ('cowave', NULL, 'Hub系统管理', 'Cog', 'link', 'sys', 'http://localhost:1024', 1, 1, NULL, NULL, NULL, NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- 在线工具（link 直接跳转，公开）
INSERT INTO hub_app (tenant_id, client_name, card_name, card_icon, app_type, app_visible, link_url, app_sort, status, client_id, client_secret, grant_type, auth_scope, redirect_url, create_by, create_time, update_by, update_time)
VALUES ('cowave', NULL, '在线工具', 'Fire', 'link', 'public', 'https://www.jyshare.com', 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- 今日资讯（link 直接跳转，公开）
INSERT INTO hub_app (tenant_id, client_name, card_name, card_icon, app_type, app_visible, link_url, app_sort, status, client_id, client_secret, grant_type, auth_scope, redirect_url, create_by, create_time, update_by, update_time)
VALUES ('cowave', NULL, '今日资讯', 'LaptopCode', 'link', 'public', 'https://hot.imsyy.top', 3, 1, NULL, NULL, NULL, NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- 租户信息
INSERT INTO sys_tenant (tenant_id, tenant_name, title, view_index, tenant_user, tenant_addr, tenant_phone, tenant_email, user_index, user_count, status, expire_time, remark, create_by, create_time, update_by, update_time) VALUES
('system', 'system', 'tenant.title.system', 'index_system', NULL, '华清园6栋', NULL, NULL, 1, 1, 1, NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('cowave', '控维通信', 'tenant.title.cowave', 'index_cowave', NULL, '华清园6栋', NULL, NULL, 9, 9, 1, NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('open', 'Open Hub', 'tenant.title.open', 'index_open', NULL, '华清园6栋', NULL, NULL, 1, 1, 1, NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- 配置数据
INSERT INTO sys_tenant_config (config_name, config_key, config_value, value_parser, value_type, is_default, remark, create_by, create_time, update_by, update_time) VALUES
('账号管理-初始密码', 'hub.initPassword', '123456', NULL, NULL, 1, '初始密码', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('账号自助-开启用户注册', 'hub.registerOnOff', 'true', NULL, 'bool', 1, '开启注册用户功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('账号自助-验证码开关', 'hub.captchaOnOff', 'true', NULL, 'bool', 1, '开启验证码功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('账号自助-验证码类型', 'hub.captchaType', 'math', NULL, NULL, 1, '验证码类型 math/char', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- 角色数据
INSERT INTO sys_tenant_role (role_code, role_name, role_type, remark, create_by, create_time, update_by, update_time) VALUES
('sysAdmin', '系统管理员', NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- 菜单数据 cowave
INSERT INTO sys_tenant_menu (menu_id, parent_id, menu_module, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, create_by, create_time, update_by, update_time) VALUES
(4, 0, NULL, 'commons.menu.cowave', 100, NULL, 'https://www.cowave.com', NULL, 'C', 'guide', NULL, 1, 0, 1, 1, 0, '控维官网', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 系统管理
(1, 0, NULL, 'commons.menu.sys.root', 7, NULL, 'system', NULL, 'M', 'system', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 租户管理
(173, 1, NULL, 'commons.menu.sys.tenant', 1, NULL, 'tenant', NULL, 'C', 'tenant', 'system/tenant/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(174, 173, 'module_tenant', 'commons.button.query', 1, 'sys:tenant:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(175, 173, 'module_tenant', 'commons.button.create', 2, 'sys:tenant:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(176, 173, 'module_tenant', 'commons.button.edit', 3, 'sys:tenant:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(177, 173, 'module_tenant', 'commons.button.status', 4, 'sys:tenant:status', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(190, 173, 'module_tenant', 'tenant.button.manager', 5, 'sys:tenant:manager:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(191, 173, 'module_tenant', 'tenant.button.manager_add', 6, 'sys:tenant:manager:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(192, 173, 'module_tenant', 'tenant.button.manager_remove', 7, 'sys:tenant:manager:remove', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 身份权限目录
(220, 1, NULL, 'commons.menu.sys.identity', 2, NULL, 'identity', NULL, 'M', 'auth', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 用户管理
(5, 220, NULL, 'commons.menu.sys.user', 1, NULL, 'user', NULL, 'C', 'user', 'system/user/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(22, 5, 'module_user', 'commons.button.query', 1, 'sys:user:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(23, 5, 'module_user', 'commons.button.create', 2, 'sys:user:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(24, 5, 'module_user', 'commons.button.edit', 3, 'sys:user:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(25, 5, 'module_user', 'commons.button.delete', 4, 'sys:user:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(26, 5, 'module_user', 'commons.button.export', 5, 'sys:user:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(27, 5, 'module_user', 'commons.button.import', 6, 'sys:user:import', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(88, 5, 'module_user', 'commons.button.diagram', 7, 'sys:user:diagram', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(85, 5, 'module_user', 'user.button.grant', 9, 'sys:user:grant', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(28, 5, 'module_user', 'user.button.passwd', 10, 'sys:user:passwd', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(112, 5, 'module_user', 'commons.button.status', 11, 'sys:user:status', '#', NULL, 'B', '#', NULL, 1, 1, 1, 0, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 角色管理
(6, 220, NULL, 'commons.menu.sys.role', 2, NULL, 'role', NULL, 'C', 'identity', 'system/role/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(29, 6, 'module_role', 'commons.button.query', 1, 'sys:role:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(30, 6, 'module_role', 'commons.button.create', 2, 'sys:role:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(31, 6, 'module_role', 'commons.button.edit', 3, 'sys:role:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(32, 6, 'module_role', 'commons.button.delete', 4, 'sys:role:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(33, 6, 'module_role', 'commons.button.export', 5, 'sys:role:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(97, 6, 'module_role', 'role.button.menus', 6, 'sys:role:menus', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(99, 6, 'module_role', 'role.button.members', 8, 'sys:role:members:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(178, 6, 'module_role', 'role.button.members_grant', 9, 'sys:role:members:grant', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(179, 6, 'module_role', 'role.button.members_cancel', 10, 'sys:role:members:cancle', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 部门管理
(8, 220, NULL, 'commons.menu.sys.dept', 3, NULL, 'dept', NULL, 'C', 'dept', 'system/dept/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(39, 8, 'module_dept', 'commons.button.query', 1, 'sys:dept:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(40, 8, 'module_dept', 'commons.button.create', 2, 'sys:dept:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(41, 8, 'module_dept', 'commons.button.edit', 3, 'sys:dept:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(42, 8, 'module_dept', 'commons.button.delete', 4, 'sys:dept:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(90, 8, 'module_dept', 'commons.button.export', 5, 'sys:dept:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(91, 8, 'module_dept', 'commons.button.diagram', 6, 'sys:dept:diagram', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(180, 8, 'module_dept', 'dept.button.members', 9, 'sys:dept:members:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(181, 8, 'module_dept', 'dept.button.members_add', 10, 'sys:dept:members:add', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(182, 8, 'module_dept', 'dept.button.members_remove', 11, 'sys:dept:members:remove', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(183, 8, 'module_dept', 'dept.button.positions', 12, 'sys:dept:positions:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(184, 8, 'module_dept', 'dept.button.positions_add', 13, 'sys:dept:positions:add', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(185, 8, 'module_dept', 'dept.button.positions_remove', 14, 'sys:dept:positions:remove', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 岗位管理
(9, 220, NULL, 'commons.menu.sys.post', 4, NULL, 'post', NULL, 'C', 'post', 'system/post/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(43, 9, 'module_post', 'commons.button.query', 1, 'sys:post:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(44, 9, 'module_post', 'commons.button.create', 2, 'sys:post:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(45, 9, 'module_post', 'commons.button.edit', 3, 'sys:post:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(46, 9, 'module_post', 'commons.button.delete', 4, 'sys:post:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(47, 9, 'module_post', 'commons.button.export', 5, 'sys:post:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(95, 9, 'module_post', 'commons.button.diagram', 6, 'sys:post:diagram', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 菜单管理 system
(7, 1, NULL, 'commons.menu.sys.menu', 6, NULL, 'menu', NULL, 'C', 'form', 'system/menu/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(34, 7, 'module_menu', 'commons.button.query', 1, 'sys:menu:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(35, 7, 'module_menu', 'commons.button.create', 2, 'sys:menu:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(36, 7, 'module_menu', 'commons.button.edit', 3, 'sys:menu:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(37, 7, 'module_menu', 'commons.button.delete', 4, 'sys:menu:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(100, 7, 'module_menu', 'commons.button.export', 5, 'sys:menu:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 数据权限
(195, 1,  NULL, 'commons.menu.sys.scope', 9, NULL, 'scope', NULL, 'C', 'vscope', 'system/scope/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(196, 195,  'module_scope', 'commons.button.query', 1, 'sys:scope:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(197, 195,  'module_scope', 'commons.button.delete', 2, 'sys:scope:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(198, 195,  'module_scope', 'commons.button.create', 3, 'sys:scope:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(199, 195,  'module_scope', 'commons.button.edit', 4, 'sys:scope:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 在线用户
(14, 1,  NULL, 'commons.menu.monitor.online', 3, NULL, 'online', NULL, 'C', 'online', 'monitor/online/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(64, 14,  'module_online', 'commons.button.query', 1, 'monitor:online:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(65, 14,  'module_online', 'commons.button.quit', 2, 'monitor:online:force', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 操作日志
(13, 1,  NULL, 'commons.menu.monitor.log', 4, NULL, 'log', NULL, 'C', 'log', 'monitor/operlog/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(79, 13,  'module_oplog', 'commons.button.query', 1, 'monitor:log:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(80, 13,  'module_oplog', 'commons.button.delete', 2, 'monitor:log:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(81, 13,  'module_oplog', 'commons.button.export', 3, 'monitor:log:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(82, 13,  'module_oplog', 'commons.button.clean', 4, 'monitor:log:clean', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 系统配置目录
(221, 1,  NULL, 'commons.menu.sys.config', 5, NULL, 'config', NULL, 'M', 'param', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 系统参数
(11, 221,  NULL, 'commons.menu.sys.params', 1, NULL, 'params', NULL, 'C', 'tree-table', 'system/config/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(53, 11,  'module_config', 'commons.button.query', 1, 'sys:config:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(54, 11,  'module_config', 'commons.button.create', 2, 'sys:config:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(55, 11,  'module_config', 'commons.button.edit', 3, 'sys:config:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(56, 11,  'module_config', 'commons.button.delete', 4, 'sys:config:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(57, 11,  'module_config', 'commons.button.export', 5, 'sys:config:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(102, 11,  'module_config', 'config.button.reset', 6, 'sys:config:reset', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 字典管理 system
(10, 221, NULL, 'commons.menu.sys.dict', 2, NULL, 'dict', NULL, 'C', 'dict', 'system/dict/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(48, 10, 'module_dict', 'commons.button.query', 1, 'sys:dict:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(49, 10, 'module_dict', 'commons.button.create', 2, 'sys:dict:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(50, 10, 'module_dict', 'commons.button.edit', 3, 'sys:dict:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(51, 10, 'module_dict', 'commons.button.delete', 4, 'sys:dict:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(52, 10, 'module_dict', 'commons.button.export', 5, 'sys:dict:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 定时任务
(15, 221,  NULL, 'commons.menu.sys.schedule.root', 3, NULL, 'job', NULL, 'C', 'job', 'system/job/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2023-08-10 06:41:40.275'),
(67, 15,  'module_task', 'commons.button.query', 1, 'sys:job:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(68, 15,  'module_task', 'commons.button.create', 2, 'sys:job:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(69, 15,  'module_task', 'commons.button.edit', 3, 'sys:job:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(70, 15,  'module_task', 'commons.button.delete', 4, 'sys:job:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(72, 15,  'module_task', 'commons.button.export', 5, 'sys:job:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(117, 15,  'module_task', 'commons.button.exec', 6, 'sys:job:exec', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(71, 15,  'module_task', 'commons.button.status', 7, 'sys:job:status', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(118, 15,  'module_task', 'commons.menu.sys.schedule.refresh', 8, 'sys:job:refresh', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(138, 15,  'module_task', 'commons.menu.sys.schedule.logQuery', 9, 'sys:job:log:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(140, 15,  'module_task', 'commons.menu.sys.schedule.logExport', 9, 'sys:job:log:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(139, 15,  'module_task', 'commons.menu.sys.schedule.logDelete', 9, 'sys:job:log:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 文件管理 system
(200, 221, NULL, 'commons.menu.sys.attach', 4, NULL, 'attach', NULL, 'C', 'attach', 'system/attach/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(201, 200, 'module_attach', 'commons.button.query', 1, 'sys:attach:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(202, 200, 'module_attach', 'commons.button.delete', 2, 'sys:attach:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(203, 200, 'module_attach', 'commons.button.preview', 3, 'sys:attach:preview', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(204, 200, 'module_attach', 'commons.button.download', 4, 'sys:attach:download', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 授权认证目录
(222, 1,  NULL, 'commons.menu.sys.auth', 7, NULL, 'authorization', NULL, 'M', 'param', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- Ldap认证
(148, 222,  NULL, 'commons.menu.sys.ldap', 1, NULL, 'ldap', NULL, 'C', 'ldap', 'system/ldap/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(149, 148,  'module_ldap', 'commons.button.query', 1, 'sys:ldap:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(150, 148,  'module_ldap', 'commons.button.create', 2, 'sys:ldap:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(151, 148,  'module_ldap', 'commons.button.edit', 3, 'sys:ldap:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(152, 148,  'module_ldap', 'commons.button.delete', 4, 'sys:ldap:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(153, 148,  'module_ldap', 'commons.button.test', 5, 'sys:ldap:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(154, 148,  'module_ldap', 'commons.button.status', 6, 'sys:ldap:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- Gitlab认证
(142, 222, NULL, 'commons.menu.sys.oauth2.gitlab', 2, NULL, 'gitlab', NULL, 'C', 'gitlab', 'system/oauth/gitlab', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(143, 142, 'module_oauth', 'commons.button.query', 1, 'oauth:provider:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(144, 142, 'module_oauth', 'commons.button.config', 2, 'oauth:provider:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(145, 142, 'module_oauth', 'commons.menu.sys.oauth2.userQuery', 3, 'oauth:provider:user:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(146, 142, 'module_oauth', 'commons.menu.sys.oauth2.userEdit', 4, 'oauth:gitlab:user:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(147, 142, 'module_oauth', 'commons.menu.sys.oauth2.userDelete', 5, 'oauth:gitlab:user:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 开发者文档 system
(21, 1, NULL, 'commons.menu.sys.doc.api', 12, NULL, 'doc', NULL, 'M', 'develop', '', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(115, 21, NULL, 'commons.menu.sys.doc.admin', 1, NULL, 'admin', NULL, 'C', 'api', 'system/doc/admin', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(116, 21, NULL, 'commons.menu.sys.doc.job', 2, NULL, 'job', NULL, 'C', 'api', 'system/doc/job', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(126, 21, NULL, 'commons.menu.sys.doc.meter', 3, NULL, 'meter', NULL, 'C', 'api', 'system/doc/meter', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 流程配置
(155, 1, NULL, 'commons.menu.flow.manage', 8, NULL, 'manage', NULL, 'M', 'cascader', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(157, 155, NULL, 'commons.menu.flow.model', 1, 'flow:modeler', 'modeler', NULL, 'C', 'component', 'flow/modeler', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(158, 155, NULL, 'commons.menu.flow.deploy', 2, 'flow:deploy', 'deploy', NULL, 'C', 'deploy', 'flow/deploy', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(156, 155, NULL, 'commons.menu.flow.instance', 3, 'flow:instance', 'instance', NULL, 'C', 'flowinstance', 'flow/instance', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 系统监控
(2, 1, NULL, 'commons.menu.monitor.root', 9, NULL, 'monitor', NULL, 'M', 'monitor', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 监控页面
(165, 2, NULL, 'commons.menu.monitor.nacos', 3, NULL, 'monitor-nacos', NULL, 'C', 'nacos', 'monitor/nacos/index', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(164, 2, NULL, 'commons.menu.monitor.actuator', 4, NULL, 'monitor-actuator', NULL, 'C', 'health', 'monitor/actuator/index', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(130, 2, NULL, 'commons.menu.monitor.alert', 5, NULL, 'monitor-alert', NULL, 'C', 'alert', 'monitor/alert/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(166, 2, NULL, 'commons.menu.monitor.grafana', 6, NULL, 'monitor-grafana', NULL, 'C', 'grafana', 'monitor/grafana/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(167, 2, NULL, 'commons.menu.monitor.prometheus', 7, NULL, 'monitor-prometheus', NULL, 'C', 'prometheus', 'monitor/prometheus/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 工作台
(124, 0, NULL, 'commons.menu.meter.workspace', 1, NULL, 'meter', NULL, 'M', 'workspace', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

(160, 124, NULL, 'commons.menu.flow.owner.task', 1, 'flow:task', 'task', NULL, 'C', 'task', 'flow/workbench/task', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(161, 124, NULL, 'commons.menu.flow.owner.leave', 2, 'flow:leave', 'leave', NULL, 'C', 'leave', 'flow/workbench/leave', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(162, 124, NULL, 'commons.menu.flow.owner.meeting', 3, 'flow:meeting', 'meeting', NULL, 'C', 'meeting', 'flow/workbench/meeting', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(163, 124, NULL, 'commons.menu.flow.owner.purchase', 4, 'flow:purchase', 'purchase', NULL, 'C', 'purchase', 'flow/workbench/purchase', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 构建管理
(250, 124, NULL, 'commons.menu.meter.build.root', 5, NULL, 'build', NULL, 'C', 'compile', 'meter/build/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 测试管理
(300, 124, NULL, 'commons.menu.meter.test.root', 6, NULL, 'test', NULL, 'M', 'test', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(301, 300, NULL, 'commons.menu.meter.test.ui', 8, NULL, 'ui', NULL, 'C', 'meter_ui', 'meter/test/ui/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 版本管理
(350, 124, NULL, 'commons.menu.meter.archive.root', 7, NULL, 'archive', NULL, 'M', 'archive', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 环境资源
(210, 124, NULL, 'commons.menu.meter.env.root', 8, NULL, 'env', NULL, 'M', 'env', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(211, 210, NULL, 'commons.menu.meter.env.credential', 1, NULL, 'credential', NULL, 'C', 'credential', 'meter/env/credential/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 开发设计
(132, 124, NULL, 'commons.menu.meter.develop.root', 9, NULL, 'template', NULL, 'M', 'code', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(19, 132, NULL, 'commons.menu.meter.develop.form', 1, NULL, 'form', NULL, 'C', 'form', 'meter/develop/form/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(134, 132, NULL, 'commons.menu.meter.develop.application', 2, NULL, 'application', NULL, 'C', 'app', 'meter/develop/application', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(135, 132, NULL, 'commons.menu.meter.develop.model', 3, NULL, 'model', NULL, 'C', 'model', 'meter/develop/model', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(136, 132, NULL, 'commons.menu.meter.develop.database', 4, NULL, 'db', NULL, 'C', 'db', 'meter/develop/db', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(137, 132, NULL, 'commons.menu.meter.develop.table', 5, NULL, 'table', NULL, 'C', 'table', 'meter/develop/table', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- Home门户
(400, 1, NULL, 'commons.menu.home.root', 6, NULL, 'home', NULL, 'M', 'home', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 授权服务
(401, 400, NULL, 'commons.menu.home.service', 1, NULL, 'client', NULL, 'C', 'oauth', 'system/oauth/client', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 授权用户
(411, 400, NULL, 'commons.menu.home.user', 2, NULL, 'client', NULL, 'C', 'peoples', 'system/oauth/client', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 应用导航
(421, 400, NULL, 'commons.menu.home.app', 3, NULL, 'client', NULL, 'C', 'app', 'system/oauth/client', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(422, 421, 'module_oauth', 'commons.button.query', 1, 'oauth:app:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(423, 421, 'module_oauth', 'commons.button.create', 2, 'oauth:app:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(424, 421, 'module_oauth', 'commons.button.delete', 3, 'oauth:app:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- 领域模块数据
INSERT INTO sys_tenant_module (parent_code, module_code, module_name, module_order, status, remark, create_by, create_time, update_by, update_time) VALUES
(NULL, 'domain_system', 'dict.name.domain_system', 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(NULL, 'domain_monitor', 'dict.name.domain_monitor', 2, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(NULL, 'domain_flow', 'dict.name.domain_flow', 3, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(NULL, 'domain_meter', 'dict.name.domain_meter', 4, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_auth', 'dict.name.module_auth', 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_tenant', 'dict.name.module_tenant', 2, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_user', 'dict.name.module_user', 3, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_role', 'dict.name.module_role', 4, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_dept', 'dict.name.module_dept', 5, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_post', 'dict.name.module_post', 6, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_menu', 'dict.name.module_menu', 7, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_scope', 'dict.name.module_scope', 8, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_dict', 'dict.name.module_dict', 9, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_config', 'dict.name.module_config', 10, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_task', 'dict.name.module_task', 11, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_ldap', 'dict.name.module_ldap', 12, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_oauth', 'dict.name.module_oauth', 13, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_notice', 'dict.name.module_notice', 14, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_attach', 'dict.name.module_attach', 15, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_monitor', 'module_online', 'dict.name.module_online', 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_monitor', 'module_oplog', 'dict.name.module_oplog', 2, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- 字典类型数据
INSERT INTO sys_tenant_dict_type (module_code, type_code, type_name, status, remark, create_by, create_time, update_by, update_time) VALUES
('module_oplog', 'op_action', 'dict.name.op_action', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('module_attach', 'attach_type', 'dict.name.attach_type', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_flow', 'leave', 'dict.name.leave', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('module_post', 'post_type', 'dict.name.post_type', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('module_post', 'post_level', 'dict.name.post_level', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('module_task', 'job_task', '任务类型', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('module_task', 'job_route', '路由策略', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('module_task', 'job_block', '阻塞策略', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('module_task', 'job_misfire', '过期策略', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- 字典项数据
INSERT INTO sys_tenant_dict (type_code, dict_code, dict_name, dict_value, value_type, value_parser, dict_order, is_default, css, status, remark, create_by, create_time, update_by, update_time) VALUES
('op_action', 'op_create', 'dict.name.op_create', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_delete', 'dict.name.op_delete', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_edit', 'dict.name.op_edit', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_status', 'dict.name.op_status', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_grant', 'dict.name.op_grant', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_passwd', 'dict.name.op_passwd', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_login', 'dict.name.op_login', NULL, NULL, NULL, 6, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_login_oauth', 'dict.name.op_login_oauth', NULL, NULL, NULL, 7, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_logout', 'dict.name.op_logout', NULL, NULL, NULL, 8, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_logout_force', 'dict.name.op_logout_force', NULL, NULL, NULL, 9, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('attach_type', 'image', 'dict.name.image', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('attach_type', 'avatar', 'dict.name.avatar', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('attach_type', 'logo', 'dict.name.logo', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('leave', 'annual', 'dict.name.annual', '1', 'int32', NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('leave', 'personal', 'dict.name.personal', '2', 'int32', NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('leave', 'sick', 'dict.name.sick', '3', 'int32', NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('leave', 'bereavement', 'dict.name.bereavement', '4', 'int32', NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('leave', 'maternity', 'dict.name.maternity', '5', 'int32', NULL, 5, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_type', 'M', 'dict.name.M', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_type', 'T', 'dict.name.T', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_type', 'A', 'dict.name.A', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_type', 'S', 'dict.name.S', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_type', 'F', 'dict.name.F', NULL, NULL, NULL, 5, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T0', 'dict.name.T0', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T1', 'dict.name.T1', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T2', 'dict.name.T2', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T3', 'dict.name.T3', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T4', 'dict.name.T4', NULL, NULL, NULL, 5, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T5', 'dict.name.T5', NULL, NULL, NULL, 6, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T6', 'dict.name.T6', NULL, NULL, NULL, 7, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T7', 'dict.name.T7', NULL, NULL, NULL, 8, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M0', 'dict.name.M0', NULL, NULL, NULL, 9, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M1', 'dict.name.M1', NULL, NULL, NULL, 10, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M2', 'dict.name.M2', NULL, NULL, NULL, 11, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M3', 'dict.name.M3', NULL, NULL, NULL, 12, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M4', 'dict.name.M4', NULL, NULL, NULL, 13, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M5', 'dict.name.M5', NULL, NULL, NULL, 14, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M6', 'dict.name.M6', NULL, NULL, NULL, 15, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M7', 'dict.name.M7', NULL, NULL, NULL, 16, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_task', 'job_task_bean', 'Java实例', 'BEAN', NULL, NULL, 1, 1, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_task', 'job_task_groovy', 'Groovy脚本', 'GROOVY', NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_task', 'job_task_python', 'Python脚本', 'PYTHON', NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_task', 'job_task_php', 'Php脚本', 'PHP', NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_task', 'job_task_nodejs', 'Nodejs脚本', 'NODEJS', NULL, NULL, 5, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_task', 'job_task_shell', 'Shell脚本', 'SHELL', NULL, NULL, 6, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_round', '轮询', 'ROUND', NULL, NULL, 1, 1, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_random', '随机', 'RANDOM', NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_hash', 'Hash散列', 'CONSISTENT_HASH', NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_fail', '故障转移', 'FAIL_OVER', NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_busy', '忙碌转移', 'BUSY_OVER', NULL, NULL, 5, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_shard', '分片广播', 'SHARDING_BROADCAST', NULL, NULL, 6, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_lfu', '最不经常使用', 'LEAST_FREQUENTLY_USED', NULL, NULL, 7, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_lru', '最久未使用', 'LEAST_RECENTLY_USED', NULL, NULL, 8, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_first', '取第一个', 'FIRST', NULL, NULL, 9, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_last', '取最后一个', 'LAST', NULL, NULL, 10, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_block', 'job_block_serial', '单机串行', 'SERIAL_EXECUTION', NULL, NULL, 1, 1, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_block', 'job_block_discard', '丢弃后续任务', 'DISCARD_LATER', NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_block', 'job_block_cover', '覆盖之前任务', 'COVER_EARLY', NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_misfire', 'job_misfire_ignore', '忽略', 'IGNORE', NULL, NULL, 1, 1, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_misfire', 'job_misfire_fire', '立即执行', 'FIRE_NOW', NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

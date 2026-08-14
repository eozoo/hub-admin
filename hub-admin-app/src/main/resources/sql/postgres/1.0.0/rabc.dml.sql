-- 租户信息
INSERT INTO "sys_tenant" ("tenant_id", "tenant_name", "title", "view_index","tenant_user", "tenant_addr", "tenant_phone", "tenant_email", "user_index", "user_count","status", "expire_time", "remark", "create_by", "create_time", "update_by", "update_time", "logo") VALUES
('system', 'system', 'tenant.title.system', 'index_system', NULL, '华清园6栋', NULL, NULL, 1, 1, 1, NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00', NULL),
('cowave', '控维通信', 'tenant.title.cowave', 'index_cowave',NULL, '华清园6栋', NULL, NULL, 9, 9, 1, NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00', NULL),
('open', '在线Hub', 'tenant.title.open', 'index_open',NULL, '华清园6栋', NULL, NULL, 1, 1, 1, NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00', NULL);

-- 部门数据
INSERT INTO "sys_dept" ("dept_id", "tenant_id", "dept_type", "dept_code", "dept_name", "dept_short", "dept_addr", "dept_phone", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
(1, 'cowave', 'HD', NULL, '南京总公司', NULL, NULL, '15888888888', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(2, 'cowave', 'BR', NULL, '北京分公司', NULL, NULL, '15888888888', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(3, 'cowave', 'RND', NULL, '研发部门', NULL, NULL, '15888888888', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(5, 'cowave', 'FIN', 'FD', '财务部门', NULL, NULL, '15888888888', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(7, 'cowave', 'ADM', NULL, '行政部门', NULL, NULL, '15888888888', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(9, 'cowave', 'MKT', NULL, '市场部门', NULL, NULL, '15888888888', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(4, 'cowave', 'SAL', NULL, '销售部门', NULL, NULL, '15888888888', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(6, 'cowave', 'HR', 'HR', '人事部', NULL, NULL, '15888888888', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(8, 'cowave', 'OPS', NULL, '运营部门', NULL, NULL, '15888888888', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(10, 'cowave', 'SAL', NULL, '销售部门[北京]', NULL, NULL, '15888888888', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(11, 'cowave', 'SAL', NULL, '市场部门[北京]', NULL, NULL, '15888888888', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(12, 'cowave', 'PUB', NULL, '公共部门', NULL, NULL, '15888888888', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');
SELECT setval('sys_dept_dept_id_seq', (SELECT max(dept_id) FROM sys_dept));

-- 岗位数据
INSERT INTO "sys_post" ("post_id", "tenant_id", "post_code", "post_name", "post_level", "post_type", "post_status", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
(17, 'cowave', 'AC', '出纳员', 1, 'F', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(18, 'cowave', 'ACCT', '会计师', 1, 'F', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(1, 'cowave', 'GM', '总经理', 1, 'M', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(2, 'cowave', 'CTO', 'CTO技术总监', 1, 'M', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(3, 'cowave', 'CEO', 'CEO行政总监', 1, 'M', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(4, 'cowave', 'CFO', 'CFO财务总监', 1, 'M', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(5, 'cowave', 'COS', 'COS销售总监', 1, 'M', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(6, 'cowave', 'COO', 'COO运营总监', 1, 'M', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(7, 'cowave', 'CHO', 'CHO人力资源总监', 1, 'M', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(19, 'cowave', NULL, '研发主管', 1, 'M', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(20, 'cowave', NULL, '产品经理', 1, 'M', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(21, 'cowave', NULL, '项目经理', 1, 'M', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(22, 'cowave', NULL, '系统架构师', 1, 'T', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(23, 'cowave', NULL, '硬件工程师', 1, 'T', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(24, 'cowave', NULL, '运维工程师', 1, 'T', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(25, 'cowave', NULL, '测试工程师', 1, 'T', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(26, 'cowave', NULL, '嵌入式工程师', 1, 'T', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(27, 'cowave', NULL, 'UI设计师', 1, 'T', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(28, 'cowave', NULL, '前端工程师', 1, 'T', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(29, 'cowave', NULL, 'Python工程师', 1, 'T', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(30, 'cowave', NULL, 'Java工程师', 1, 'T', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(8, 'cowave', NULL, '前台接待', 1, 'A', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(9, 'cowave', NULL, '行政专员', 1, 'A', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(10, 'cowave', NULL, '行政主管', 1, 'A', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(13, 'cowave', NULL, '销售专员', 1, 'S', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(14, 'cowave', NULL, '销售经理', 1, 'S', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(15, 'cowave', NULL, '招聘专员', 1, 'A', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(16, 'cowave', NULL, '招聘主管', 1, 'A', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(11, 'cowave', NULL, '市场专员', 1, 'A', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(12, 'cowave', NULL, '运营经理', 1, 'A', 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');
SELECT setval('sys_post_post_id_seq', (SELECT max(post_id) FROM sys_post));

-- 部门岗位
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 1);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 2);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 3);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 4);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 5);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 6);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 7);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (7, 8);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (7, 9);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (7, 10);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (8, 12);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 13);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 14);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (6, 15);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (6, 16);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (5, 17);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (5, 18);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (3, 19);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (3, 20);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (3, 21);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (3, 22);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (3, 23);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (3, 24);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (3, 25);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (3, 26);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (3, 27);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (3, 28);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (3, 29);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (3, 30);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (9, 11);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (11, 11);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (10, 13);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (10, 14);

-- 部门关系
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (1, 0, 'cowave');
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (2, 0, 'cowave');
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (3, 1, 'cowave');
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (4, 1, 'cowave');
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (5, 1, 'cowave');
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (6, 1, 'cowave');
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (7, 1, 'cowave');
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (8, 1, 'cowave');
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (9, 1, 'cowave');
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (12, 1, 'cowave');
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (10, 2, 'cowave');
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (11, 2, 'cowave');
INSERT INTO "sys_dept_diagram" ("dept_id", "parent_id", "tenant_id") VALUES (12, 2, 'cowave');

-- 岗位关系
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (2, 1, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (3, 1, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (4, 1, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (5, 1, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (6, 1, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (7, 1, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (10, 3, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (12, 6, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (14, 5, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (16, 7, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (17, 4, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (18, 4, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (19, 2, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (1, 0, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (15, 16, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (11, 12, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (13, 14, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (8, 10, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (9, 10, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (27, 21, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (28, 21, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (29, 21, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (30, 21, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (21, 19, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (20, 19, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (25, 21, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (22, 19, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (23, 21, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (24, 21, 'cowave');
INSERT INTO "sys_post_diagram" ("post_id", "parent_id", "tenant_id") VALUES (26, 21, 'cowave');

-- 用户数据
INSERT INTO "sys_user" ("user_id", "tenant_id", "user_type", "user_code", "user_name", "user_account", "user_passwd", "user_sex", "user_phone", "user_email", "user_status", "user_rank", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
(1, 'system', 'sys', 'system-sys-sysAdmin', '系统管理员', 'sysAdmin', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, null, null, 1, NULL, NULL,  NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(2, 'cowave', 'sys', 'cowave-sys-liubei', '刘备', 'liubei', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'liubei@cowave.com', 1, 'M7', NULL,  NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(3, 'cowave', 'sys', 'cowave-sys-zhugeliang', '诸葛亮', 'zhugeliang', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'zhugeliang@cowave.com', 1, 'M7', NULL,  NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(4, 'cowave', 'sys', 'cowave-sys-guanyu', '关羽', 'guanyu', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'guanyu@cowave.com', 1, 'M7', NULL,  NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(5, 'cowave', 'sys', 'cowave-sys-zhangfei', '张飞', 'zhangfei', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'zhangfei@cowave.com', 1, 'M7', NULL,  NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(6, 'cowave', 'sys', 'cowave-sys-machao', '马超', 'machao', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'machao@cowave.com', 1, 'M7', NULL,  NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(7, 'cowave', 'sys', 'cowave-sys-zhaoyun', '赵云', 'zhaoyun', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'zhaoyun@cowave.com', 1, 'M7', NULL,  NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(8, 'cowave', 'sys', 'cowave-sys-huangzhong', '黄忠', 'huangzhong', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'huangzhong@cowave.com', 1, 'M7', NULL,  NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(9, 'cowave', 'sys', 'cowave-sys-daqiao', '大乔', 'daqiao', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 1, '13288888888', 'daqiao@cowave.com', 1, 'M2', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(10, 'cowave', 'sys', 'cowave-sys-xiaoqiao', '小乔', 'xiaoqiao', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 1, '13288888888', 'xiaoqiao@cowave.com', 1, 'M2', NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(11, 'open', 'sys', 'open-sys-mia', '米娅', 'mia', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, null, null, 1, NULL, NULL,  NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');
SELECT setval('sys_user_user_id_seq', (SELECT max(user_id) FROM sys_user));

-- 角色数据
INSERT INTO "sys_role" ("role_id", "tenant_id", "role_code", "role_name", "role_type", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
(1, '#', 'sysAdmin', '系统管理员', NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(2, 'cowave', 'flowAdmin', '流程管理员', NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(3, 'cowave', 'role-readonly', '只读用户', NULL, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');
SELECT setval('sys_role_role_id_seq', (SELECT max(role_id) FROM sys_role));

-- 菜单数据
INSERT INTO "sys_menu" ("menu_id", "parent_id", "tenant_id", "menu_module", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
(4, 0, 'cowave', NULL, 'commons.menu.cowave', 100, NULL, 'https://www.cowave.com', NULL, 'C', 'guide', NULL, 1, 0, 1, 1, 0, '控维官网', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 系统管理
(1, 0, '#', NULL, 'commons.menu.sys.root', 7, NULL, 'system', NULL, 'M', 'system', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 租户管理
(173, 1, 'system', NULL, 'commons.menu.sys.tenant', 1, NULL, 'tenant', NULL, 'C', 'tenant', 'system/tenant/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(174, 173, 'system', 'module_tenant', 'commons.button.query', 1, 'sys:tenant:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(175, 173, 'system', 'module_tenant', 'commons.button.create', 2, 'sys:tenant:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(176, 173, 'system', 'module_tenant', 'commons.button.edit', 3, 'sys:tenant:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(177, 173, 'system', 'module_tenant', 'commons.button.status', 4, 'sys:tenant:status', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(190, 173, 'system', 'module_tenant', 'tenant.button.manager', 5, 'sys:tenant:manager:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(191, 173, 'system', 'module_tenant', 'tenant.button.manager_add', 6, 'sys:tenant:manager:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(192, 173, 'system', 'module_tenant', 'tenant.button.manager_remove', 7, 'sys:tenant:manager:remove', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 用户权限目录
(220, 1, '#', NULL, 'commons.menu.sys.identity', 2, NULL, 'identity', NULL, 'M', 'peoples', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 用户管理
(5, 220, '#', NULL, 'commons.menu.sys.user', 1, NULL, 'user', NULL, 'C', 'user', 'system/user/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(22, 5, '#', 'module_user', 'commons.button.query', 1, 'sys:user:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(23, 5, '#', 'module_user', 'commons.button.create', 2, 'sys:user:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(24, 5, '#', 'module_user', 'commons.button.edit', 3, 'sys:user:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(25, 5, '#', 'module_user', 'commons.button.delete', 4, 'sys:user:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(26, 5, '#', 'module_user', 'commons.button.export', 5, 'sys:user:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(27, 5, '#', 'module_user', 'commons.button.import', 6, 'sys:user:import', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(88, 5, '#', 'module_user', 'commons.button.diagram', 7, 'sys:user:diagram', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(85, 5, '#', 'module_user', 'user.button.grant', 9, 'sys:user:grant', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(28, 5, '#', 'module_user', 'user.button.passwd', 10, 'sys:user:passwd', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(112, 5, '#', 'module_user', 'commons.button.status', 11, 'sys:user:status', '#', NULL, 'B', '#', NULL, 1, 1, 1, 0, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 角色管理
(6, 220, '#', NULL, 'commons.menu.sys.role', 2, NULL, 'role', NULL, 'C', 'identity', 'system/role/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(29, 6, '#', 'module_role', 'commons.button.query', 1, 'sys:role:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(30, 6, '#', 'module_role', 'commons.button.create', 2, 'sys:role:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(31, 6, '#', 'module_role', 'commons.button.edit', 3, 'sys:role:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(32, 6, '#', 'module_role', 'commons.button.delete', 4, 'sys:role:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(33, 6, '#', 'module_role', 'commons.button.export', 5, 'sys:role:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(97, 6, '#', 'module_role', 'role.button.menus', 6, 'sys:role:menus', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(99, 6, '#', 'module_role', 'role.button.members', 8, 'sys:role:members:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(178, 6, '#', 'module_role', 'role.button.members_grant', 9, 'sys:role:members:grant', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(179, 6, '#', 'module_role', 'role.button.members_cancel', 10, 'sys:role:members:cancle', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 部门管理
(8, 220, '#', NULL, 'commons.menu.sys.dept', 3, NULL, 'dept', NULL, 'C', 'dept', 'system/dept/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(39, 8, '#', 'module_dept', 'commons.button.query', 1, 'sys:dept:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(40, 8, '#', 'module_dept', 'commons.button.create', 2, 'sys:dept:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(41, 8, '#', 'module_dept', 'commons.button.edit', 3, 'sys:dept:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(42, 8, '#', 'module_dept', 'commons.button.delete', 4, 'sys:dept:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(90, 8, '#', 'module_dept', 'commons.button.export', 5, 'sys:dept:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(91, 8, '#', 'module_dept', 'commons.button.diagram', 6, 'sys:dept:diagram', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(180, 8, '#', 'module_dept', 'dept.button.members', 9, 'sys:dept:members:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(181, 8, '#', 'module_dept', 'dept.button.members_add', 10, 'sys:dept:members:add', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(182, 8, '#', 'module_dept', 'dept.button.members_remove', 11, 'sys:dept:members:remove', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(183, 8, '#', 'module_dept', 'dept.button.positions', 12, 'sys:dept:positions:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(184, 8, '#', 'module_dept', 'dept.button.positions_add', 13, 'sys:dept:positions:add', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(185, 8, '#', 'module_dept', 'dept.button.positions_remove', 14, 'sys:dept:positions:remove', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 岗位管理
(9, 220, '#', NULL, 'commons.menu.sys.post', 4, NULL, 'post', NULL, 'C', 'post', 'system/post/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(43, 9, '#', 'module_post', 'commons.button.query', 1, 'sys:post:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(44, 9, '#', 'module_post', 'commons.button.create', 2, 'sys:post:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(45, 9, '#', 'module_post', 'commons.button.edit', 3, 'sys:post:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(46, 9, '#', 'module_post', 'commons.button.delete', 4, 'sys:post:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(47, 9, '#', 'module_post', 'commons.button.export', 5, 'sys:post:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(95, 9, '#', 'module_post', 'commons.button.diagram', 6, 'sys:post:diagram', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 菜单管理
(7, 220, 'system', NULL, 'commons.menu.sys.menu', 5, NULL, 'menu', NULL, 'C', 'form', 'system/menu/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(34, 7, 'system', 'module_menu', 'commons.button.query', 1, 'sys:menu:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(35, 7, 'system', 'module_menu', 'commons.button.create', 2, 'sys:menu:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(36, 7, 'system', 'module_menu', 'commons.button.edit', 3, 'sys:menu:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(37, 7, 'system', 'module_menu', 'commons.button.delete', 4, 'sys:menu:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(100, 7, 'system', 'module_menu', 'commons.button.export', 5, 'sys:menu:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 数据权限
(195, 220, '#', NULL, 'commons.menu.sys.scope', 6, NULL, 'scope', NULL, 'C', 'vscope', 'system/scope/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(196, 195, '#', 'module_scope', 'commons.button.query', 1, 'sys:scope:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(197, 195, '#', 'module_scope', 'commons.button.delete', 2, 'sys:scope:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(198, 195, '#', 'module_scope', 'commons.button.create', 3, 'sys:scope:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(199, 195, '#', 'module_scope', 'commons.button.edit', 4, 'sys:scope:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 在线用户
(14, 1, '#', NULL, 'commons.menu.monitor.online', 3, NULL, 'online', NULL, 'C', 'online', 'monitor/online/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(64, 14, '#', 'module_online', 'commons.button.query', 1, 'monitor:online:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(65, 14, '#', 'module_online', 'commons.button.quit', 2, 'monitor:online:force', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 操作日志
(13, 1, '#', NULL, 'commons.menu.monitor.log', 4, NULL, 'log', NULL, 'C', 'log', 'monitor/operlog/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(79, 13, '#', 'module_oplog', 'commons.button.query', 1, 'monitor:log:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(80, 13, '#', 'module_oplog', 'commons.button.delete', 2, 'monitor:log:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(81, 13, '#', 'module_oplog', 'commons.button.export', 3, 'monitor:log:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(82, 13, '#', 'module_oplog', 'commons.button.clean', 4, 'monitor:log:clean', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 系统配置目录
(221, 1, '#', NULL, 'commons.menu.sys.config', 5, NULL, 'config', NULL, 'M', 'param', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 系统参数
(11, 221, '#', NULL, 'commons.menu.sys.params', 1, NULL, 'params', NULL, 'C', 'tree-table', 'system/config/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(53, 11, '#', 'module_config', 'commons.button.query', 1, 'sys:config:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(54, 11, '#', 'module_config', 'commons.button.create', 2, 'sys:config:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(55, 11, '#', 'module_config', 'commons.button.edit', 3, 'sys:config:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(56, 11, '#', 'module_config', 'commons.button.delete', 4, 'sys:config:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(57, 11, '#', 'module_config', 'commons.button.export', 5, 'sys:config:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(102, 11, '#', 'module_config', 'config.button.reset', 6, 'sys:config:reset', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 字典管理
(10, 221, 'system', NULL, 'commons.menu.sys.dict', 2, NULL, 'dict', NULL, 'C', 'dict', 'system/dict/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(48, 10, 'system', 'module_dict', 'commons.button.query', 1, 'sys:dict:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(49, 10, 'system', 'module_dict', 'commons.button.create', 2, 'sys:dict:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(50, 10, 'system', 'module_dict', 'commons.button.edit', 3, 'sys:dict:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(51, 10, 'system', 'module_dict', 'commons.button.delete', 4, 'sys:dict:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(52, 10, 'system', 'module_dict', 'commons.button.export', 5, 'sys:dict:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 定时任务
(15, 221, '#', NULL, 'commons.menu.sys.schedule.root', 3, NULL, 'job', NULL, 'C', 'job', 'system/job/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2023-08-10 06:41:40.275'),
(67, 15, '#', 'module_task', 'commons.button.query', 1, 'sys:job:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(68, 15, '#', 'module_task', 'commons.button.create', 2, 'sys:job:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(69, 15, '#', 'module_task', 'commons.button.edit', 3, 'sys:job:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(70, 15, '#', 'module_task', 'commons.button.delete', 4, 'sys:job:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(72, 15, '#', 'module_task', 'commons.button.export', 5, 'sys:job:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(117, 15, '#', 'module_task', 'commons.button.exec', 6, 'sys:job:exec', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(71, 15, '#', 'module_task', 'commons.button.status', 7, 'sys:job:status', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(118, 15, '#', 'module_task', 'commons.menu.sys.schedule.refresh', 8, 'sys:job:refresh', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(138, 15, '#', 'module_task', 'commons.menu.sys.schedule.logQuery', 9, 'sys:job:log:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(140, 15, '#', 'module_task', 'commons.menu.sys.schedule.logExport', 9, 'sys:job:log:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(139, 15, '#', 'module_task', 'commons.menu.sys.schedule.logDelete', 9, 'sys:job:log:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 文件管理
(200, 221, 'system', NULL, 'commons.menu.sys.attach', 4, NULL, 'attach', NULL, 'C', 'attach', 'system/attach/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(201, 200, 'system', 'module_attach', 'commons.button.query', 1, 'sys:attach:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(202, 200, 'system', 'module_attach', 'commons.button.delete', 2, 'sys:attach:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(203, 200, 'system', 'module_attach', 'commons.button.preview', 3, 'sys:attach:preview', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(204, 200, 'system', 'module_attach', 'commons.button.download', 4, 'sys:attach:download', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 授权认证目录
(222, 1, '#', NULL, 'commons.menu.sys.auth', 6, NULL, 'auth', NULL, 'M', 'authorization', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- Ldap认证
(148, 222, '#', NULL, 'commons.menu.sys.ldap', 1, NULL, 'ldap', NULL, 'C', 'ldap', 'system/ldap/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(149, 148, '#', 'module_ldap', 'commons.button.query', 1, 'sys:ldap:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(150, 148, '#', 'module_ldap', 'commons.button.create', 2, 'sys:ldap:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(151, 148, '#', 'module_ldap', 'commons.button.edit', 3, 'sys:ldap:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(152, 148, '#', 'module_ldap', 'commons.button.delete', 4, 'sys:ldap:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(153, 148, '#', 'module_ldap', 'commons.button.test', 5, 'sys:ldap:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(154, 148, '#', 'module_ldap', 'commons.button.status', 6, 'sys:ldap:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- Gitlab认证
(142, 222, '#', NULL, 'commons.menu.sys.oauth2.gitlab', 2, NULL, 'gitlab', NULL, 'C', 'gitlab', 'system/oauth/gitlab', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(143, 142, '#', 'module_oauth', 'commons.button.query', 1, 'oauth:gitlab:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(144, 142, '#', 'module_oauth', 'commons.button.config', 2, 'oauth:gitlab:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(145, 142, '#', 'module_oauth', 'commons.menu.sys.oauth2.userQuery', 3, 'oauth:gitlab:user:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(146, 142, '#', 'module_oauth', 'commons.menu.sys.oauth2.userEdit', 4, 'oauth:gitlab:user:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(147, 142, '#', 'module_oauth', 'commons.menu.sys.oauth2.userDelete', 5, 'oauth:gitlab:user:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 应用授权
(169, 222, '#', NULL, 'commons.menu.sys.oauth2.client', 3, NULL, 'client', NULL, 'C', 'app', 'system/oauth/client', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(170, 169, '#', 'module_oauth', 'commons.button.query', 1, 'oauth:app:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(171, 169, '#', 'module_oauth', 'commons.button.create', 2, 'oauth:app:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(172, 169, '#', 'module_oauth', 'commons.button.delete', 3, 'oauth:app:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 开发者文档
(21, 1, 'system', NULL, 'commons.menu.sys.doc.api', 12, NULL, 'doc', NULL, 'M', 'develop', '', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(115, 21, 'system', NULL, 'commons.menu.sys.doc.admin', 1, NULL, 'admin', NULL, 'C', 'api', 'system/doc/admin', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(116, 21, 'system', NULL, 'commons.menu.sys.doc.job', 2, NULL, 'job', NULL, 'C', 'api', 'system/doc/job', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(126, 21, 'system', NULL, 'commons.menu.sys.doc.meter', 3, NULL, 'meter', NULL, 'C', 'api', 'system/doc/meter', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 流程配置
(155, 1, '#', NULL, 'commons.menu.flow.manage', 7, NULL, 'manage', NULL, 'M', 'cascader', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(157, 155, '#', NULL, 'commons.menu.flow.model', 1, 'flow:modeler', 'modeler', NULL, 'C', 'component', 'flow/modeler', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(158, 155, '#', NULL, 'commons.menu.flow.deploy', 2, 'flow:deploy', 'deploy', NULL, 'C', 'deploy', 'flow/deploy', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(156, 155, '#', NULL, 'commons.menu.flow.instance', 3, 'flow:instance', 'instance', NULL, 'C', 'flowinstance', 'flow/instance', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 系统监控
(2, 1, '#', NULL, 'commons.menu.monitor.root', 8, NULL, 'monitor', NULL, 'M', 'monitor', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 监控页面
(165, 2, '#', NULL, 'commons.menu.monitor.nacos', 3, NULL, 'monitor-nacos', NULL, 'C', 'nacos', 'monitor/nacos/index', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(164, 2, '#', NULL, 'commons.menu.monitor.actuator', 4, NULL, 'monitor-actuator', NULL, 'C', 'health', 'monitor/actuator/index', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(130, 2, '#', NULL, 'commons.menu.monitor.alert', 5, NULL, 'monitor-alert', NULL, 'C', 'alert', 'monitor/alert/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(166, 2, '#', NULL, 'commons.menu.monitor.grafana', 6, NULL, 'monitor-grafana', NULL, 'C', 'grafana', 'monitor/grafana/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(167, 2, '#', NULL, 'commons.menu.monitor.prometheus', 7, NULL, 'monitor-prometheus', NULL, 'C', 'prometheus', 'monitor/prometheus/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 工作台
(124, 0, '#', NULL, 'commons.menu.meter.workspace', 1, NULL, 'meter', NULL, 'M', 'workspace', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

(160, 124, '#', NULL, 'commons.menu.flow.owner.task', 1, 'flow:task', 'task', NULL, 'C', 'task', 'flow/workbench/task', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(161, 124, '#', NULL, 'commons.menu.flow.owner.leave', 2, 'flow:leave', 'leave', NULL, 'C', 'leave', 'flow/workbench/leave', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(162, 124, '#', NULL, 'commons.menu.flow.owner.meeting', 3, 'flow:meeting', 'meeting', NULL, 'C', 'meeting', 'flow/workbench/meeting', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(163, 124, '#', NULL, 'commons.menu.flow.owner.purchase', 4, 'flow:purchase', 'purchase', NULL, 'C', 'purchase', 'flow/workbench/purchase', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 构建管理
(250, 124, '#', NULL, 'commons.menu.meter.build.root', 5, NULL, 'build', NULL, 'C', 'compile', 'meter/build/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 测试管理
(300, 124, '#', NULL, 'commons.menu.meter.test.root', 6, NULL, 'test', NULL, 'M', 'test', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(301, 300, '#', NULL, 'commons.menu.meter.test.ui', 8, NULL, 'ui', NULL, 'C', 'meter_ui', 'meter/test/ui/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 版本管理
(350, 124, '#', NULL, 'commons.menu.meter.archive.root', 7, NULL, 'archive', NULL, 'M', 'archive', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 环境资源
(210, 124, '#', NULL, 'commons.menu.meter.env.root', 8, NULL, 'env', NULL, 'M', 'env', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(211, 210, '#', NULL, 'commons.menu.meter.env.credential', 1, NULL, 'credential', NULL, 'C', 'credential', 'meter/env/credential/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

-- 开发设计
(132, 124, '#', NULL, 'commons.menu.meter.develop.root', 9, NULL, 'template', NULL, 'M', 'code', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(19, 132, '#', NULL, 'commons.menu.meter.develop.form', 1, NULL, 'form', NULL, 'C', 'form', 'meter/develop/form/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(134, 132, '#', NULL, 'commons.menu.meter.develop.application', 2, NULL, 'application', NULL, 'C', 'app', 'meter/develop/application', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(135, 132, '#', NULL, 'commons.menu.meter.develop.model', 3, NULL, 'model', NULL, 'C', 'model', 'meter/develop/model', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(136, 132, '#', NULL, 'commons.menu.meter.develop.database', 4, NULL, 'db', NULL, 'C', 'db', 'meter/develop/db', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(137, 132, '#', NULL, 'commons.menu.meter.develop.table', 5, NULL, 'table', NULL, 'C', 'table', 'meter/develop/table', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

SELECT setval('sys_menu_menu_id_seq', (SELECT max(menu_id) FROM sys_menu));

-- 用户部门岗位
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (2, 1, 1, 1, 1);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (3, 1, 2, 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (4, 1, 3, 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (5, 1, 6, 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (7, 1, 4, 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (6, 1, 7, 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (8, 1, 5, 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (3, 3, 19, 0, 1);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (4, 7, 10, 0, 1);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (5, 8, 12, 0, 1);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (7, 5, 18, 0, 1);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (6, 6, 16, 0, 1);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (8, 4, 14, 0, 1);

-- 用户角色
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (1, 1);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (11, 1);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (2, 1);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (3, 3);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (4, 3);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (5, 3);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (6, 3);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (7, 3);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (8, 3);

-- 用户关系
INSERT INTO "sys_user_diagram" ("user_id", "parent_id", "tenant_id") VALUES (1, 0, 'system');
INSERT INTO "sys_user_diagram" ("user_id", "parent_id", "tenant_id") VALUES (2, 0, 'cowave');
INSERT INTO "sys_user_diagram" ("user_id", "parent_id", "tenant_id") VALUES (3, 2, 'cowave');
INSERT INTO "sys_user_diagram" ("user_id", "parent_id", "tenant_id") VALUES (4, 2, 'cowave');
INSERT INTO "sys_user_diagram" ("user_id", "parent_id", "tenant_id") VALUES (5, 2, 'cowave');
INSERT INTO "sys_user_diagram" ("user_id", "parent_id", "tenant_id") VALUES (6, 2, 'cowave');
INSERT INTO "sys_user_diagram" ("user_id", "parent_id", "tenant_id") VALUES (7, 2, 'cowave');
INSERT INTO "sys_user_diagram" ("user_id", "parent_id", "tenant_id") VALUES (8, 2, 'cowave');

-- 只读用户菜单
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 1);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 5);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 8);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 9);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 6);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 11);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 141);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 142);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 148);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 169);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 170);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 195);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 196);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 2);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 14);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 13);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 15);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 22);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 88);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 39);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 91);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 180);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 183);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 99);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 43);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 95);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 29);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 53);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 129);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 143);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 145);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 149);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 18);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 64);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 79);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 3);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 21);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 115);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 116);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 126);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 19);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 132);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 133);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 134);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 135);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 136);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 137);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 67);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 117);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 71);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 138);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 124);

-- 数据权限
INSERT INTO "sys_scope" ("tenant_id", "scope_name", "scope_module", "scope_status", "scope_content", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
('cowave', '仅本人数据', 'module_oplog', 1, '{"scope":"personal"}', NULL, NULL, '2023-08-25 01:58:50.097', NULL, '2023-08-29 02:07:02.87'),
('cowave', '本部门数据', 'module_oplog', 1, '{"scope":"dept"}', NULL, NULL, '2023-08-25 01:58:50.097', NULL, '2023-08-29 02:07:02.87'),
('cowave', '全部数据', 'module_oplog', 1, '{"scope":"all"}', NULL, NULL, '2023-08-25 01:58:50.097', NULL, '2023-08-29 02:07:02.87');

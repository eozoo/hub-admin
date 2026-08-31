-- 系统配置
INSERT INTO sys_config (config_id, tenant_id, config_name, config_key, config_value, value_parser, value_type, is_default, remark, create_by, create_time, update_by, update_time) VALUES
(1, '#', '账号管理-初始密码', 'hub.initPassword', '123456', NULL, NULL, 1, '初始密码', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(2, '#', '账号自助-开启用户注册', 'hub.registerOnOff', 'true', NULL, 'bool', 1, '开启注册用户功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(3, '#', '账号自助-验证码开关', 'hub.captchaOnOff', 'true', NULL, 'bool', 1, '开启验证码功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(4, '#', '账号自助-验证码类型', 'hub.captchaType', 'math', NULL, NULL, 1, '验证码类型 math/char', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(5, 'system', '账号管理-初始密码', 'hub.initPassword', '123456', NULL, NULL, 1, '初始密码', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(6, 'system', '账号自助-开启用户注册', 'hub.registerOnOff', 'true', NULL, 'bool', 1, '开启注册用户功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(7, 'system', '账号自助-验证码开关', 'hub.captchaOnOff', 'true', NULL, 'bool', 1, '开启验证码功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(8, 'system', '账号自助-验证码类型', 'hub.captchaType', 'math', NULL, NULL, 1, '验证码类型 math/char', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(9, 'cowave', '账号管理-初始密码', 'hub.initPassword', '123456', NULL, NULL, 1, '初始密码', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(10, 'cowave', '账号自助-开启用户注册', 'hub.registerOnOff', 'true', NULL, 'bool', 1, '开启注册用户功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(11, 'cowave', '账号自助-验证码开关', 'hub.captchaOnOff', 'true', NULL, 'bool', 1, '开启验证码功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(12, 'cowave', '账号自助-验证码类型', 'hub.captchaType', 'math', NULL, NULL, 1, '验证码类型 math/char', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');

-- 复制租户system数据
INSERT INTO sys_module (tenant_id, parent_code, module_code, module_name, module_order, status, remark, create_by, create_time, update_by, update_time)
SELECT 'system', m.parent_code, m.module_code, m.module_name, m.module_order, m.status, m.remark, m.create_by, now(), m.update_by, now()
FROM sys_tenant_module m;

INSERT INTO sys_dict_type (tenant_id, module_code, type_code, type_name, status, remark, create_by, create_time, update_by, update_time)
SELECT 'system', d.module_code, d.type_code, d.type_name, d.status, d.remark, d.create_by, now(), d.update_by, now()
FROM sys_tenant_dict_type d;

INSERT INTO sys_dict (tenant_id, type_code, dict_code, dict_name, dict_value, value_type, value_parser, dict_order, is_default, css, status, remark, create_by, create_time, update_by, update_time)
SELECT 'system', d.type_code, d.dict_code, d.dict_name, d.dict_value, d.value_type, d.value_parser, d.dict_order, d.is_default, d.css, d.status, d.remark, d.create_by, now(), d.update_by, now()
FROM sys_tenant_dict d;

-- 复制租户cowave数据
INSERT INTO sys_module (tenant_id, parent_code, module_code, module_name, module_order, status, remark, create_by, create_time, update_by, update_time)
SELECT 'cowave', m.parent_code, m.module_code, m.module_name, m.module_order, m.status, m.remark, m.create_by, now(), m.update_by, now()
FROM sys_tenant_module m;

INSERT INTO sys_dict_type (tenant_id, module_code, type_code, type_name, status, remark, create_by, create_time, update_by, update_time)
SELECT 'cowave', d.module_code, d.type_code, d.type_name, d.status, d.remark, d.create_by, now(), d.update_by, now()
FROM sys_tenant_dict_type d;

INSERT INTO sys_dict (tenant_id, type_code, dict_code, dict_name, dict_value, value_type, value_parser, dict_order, is_default, css, status, remark, create_by, create_time, update_by, update_time)
SELECT 'cowave', d.type_code, d.dict_code, d.dict_name, d.dict_value, d.value_type, d.value_parser, d.dict_order, d.is_default, d.css, d.status, d.remark, d.create_by, now(), d.update_by, now()
FROM sys_tenant_dict d;

-- 复制租户open数据
INSERT INTO sys_module (tenant_id, parent_code, module_code, module_name, module_order, status, remark, create_by, create_time, update_by, update_time)
SELECT 'open', m.parent_code, m.module_code, m.module_name, m.module_order, m.status, m.remark, m.create_by, now(), m.update_by, now()
FROM sys_tenant_module m;

INSERT INTO sys_dict_type (tenant_id, module_code, type_code, type_name, status, remark, create_by, create_time, update_by, update_time)
SELECT 'open', d.module_code, d.type_code, d.type_name, d.status, d.remark, d.create_by, now(), d.update_by, now()
FROM sys_tenant_dict_type d;

INSERT INTO sys_dict (tenant_id, type_code, dict_code, dict_name, dict_value, value_type, value_parser, dict_order, is_default, css, status, remark, create_by, create_time, update_by, update_time)
SELECT 'open', d.type_code, d.dict_code, d.dict_name, d.dict_value, d.value_type, d.value_parser, d.dict_order, d.is_default, d.css, d.status, d.remark, d.create_by, now(), d.update_by, now()
FROM sys_tenant_dict d;

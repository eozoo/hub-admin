

-- 角色权限
INSERT INTO hub_role_menu (role_id, menu_id)
VALUES (3, (select menu_id from hub_menu where menu_type = 'M' and menu_name = 'commons.menu.flow.manage' limit 1));

INSERT INTO hub_role_menu (role_id, menu_id)
VALUES (3, (select menu_id from hub_menu where menu_type = 'C' and component = 'flow/instance' limit 1));

INSERT INTO hub_role_menu (role_id, menu_id)
VALUES (3, (select menu_id from hub_menu where menu_type = 'C' and component = 'flow/modeler' limit 1));

INSERT INTO hub_role_menu (role_id, menu_id)
VALUES (3, (select menu_id from hub_menu where menu_type = 'C' and component = 'flow/deploy' limit 1));

-- 用户部门
INSERT INTO hub_user_dept (user_id, dept_id, post_id, is_default, is_leader)
VALUES ((select user_id from hub_user where user_account = 'daqiao' limit 1),
        (select dept_id from hub_dept where dept_code = 'FD' limit 1),
        (select post_id from hub_post where post_code = 'AC' limit 1), 1, 0);

INSERT INTO hub_user_dept (user_id, dept_id, post_id, is_default, is_leader)
VALUES ((select user_id from hub_user where user_account = 'xiaoqiao' limit 1),
        (select dept_id from hub_dept where dept_code = 'FD' limit 1),
        (select post_id from hub_post where post_code = 'ACCT' limit 1), 1, 0);

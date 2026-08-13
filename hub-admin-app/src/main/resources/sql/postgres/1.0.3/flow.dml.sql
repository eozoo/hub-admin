
-- 角色权限
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, (select menu_id from sys_menu where menu_type = 'M' and menu_name = 'commons.menu.flow.manage'));
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, (select menu_id from sys_menu where menu_type = 'C' and component = 'flow/instance'));
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, (select menu_id from sys_menu where menu_type = 'C' and component = 'flow/modeler'));
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, (select menu_id from sys_menu where menu_type = 'C' and component = 'flow/deploy'));

INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader")
VALUES ((select user_id from sys_user where user_account = 'daqiao'), (select dept_id from sys_dept where dept_code = 'FD'), (select post_id from sys_post where post_code = 'AC'), 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader")
VALUES ((select user_id from sys_user where user_account = 'xiaoqiao'), (select dept_id from sys_dept where dept_code = 'FD'), (select post_id from sys_post where post_code = 'ACCT'), 1, 0);

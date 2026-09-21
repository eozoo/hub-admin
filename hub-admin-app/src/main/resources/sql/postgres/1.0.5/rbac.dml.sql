-- 部门数据
insert into sys_dept (dept_id, tenant_id, dept_type, dept_code, dept_name, dept_short, dept_addr, dept_phone, remark, create_by, create_time, update_by, update_time) values
(1, 2, 'HD', null, '南京总公司', null, null, '15888888888', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 2, 'BR', null, '北京分公司', null, null, '15888888888', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(3, 2, 'RND', null, '研发部门', null, null, '15888888888', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(5, 2, 'FIN', 'FD', '财务部门', null, null, '15888888888', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(7, 2, 'ADM', null, '行政部门', null, null, '15888888888', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(9, 2, 'MKT', null, '市场部门', null, null, '15888888888', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(4, 2, 'SAL', null, '销售部门', null, null, '15888888888', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(6, 2, 'HR', 'HR', '人事部', null, null, '15888888888', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(8, 2, 'OPS', null, '运营部门', null, null, '15888888888', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(10, 2, 'SAL', null, '销售部门[北京]', null, null, '15888888888', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(11, 2, 'SAL', null, '市场部门[北京]', null, null, '15888888888', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(12, 2, 'PUB', null, '公共部门', null, null, '15888888888', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
select setval(pg_get_serial_sequence('sys_dept', 'dept_id'), (select max(dept_id) from sys_dept));

-- 岗位数据
insert into sys_post (post_id, tenant_id, post_code, post_name, post_level, post_type, post_status, remark, create_by, create_time, update_by, update_time) values
(17, 2, 'AC', '出纳员', 1, 'F', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(18, 2, 'ACCT', '会计师', 1, 'F', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(1, 2, 'GM', '总经理', 1, 'M', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 2, 'CTO', 'CTO技术总监', 1, 'M', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(3, 2, 'CEO', 'CEO行政总监', 1, 'M', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(4, 2, 'CFO', 'CFO财务总监', 1, 'M', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(5, 2, 'COS', 'COS销售总监', 1, 'M', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(6, 2, 'COO', 'COO运营总监', 1, 'M', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(7, 2, 'CHO', 'CHO人力资源总监', 1, 'M', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(19, 2, null, '研发主管', 1, 'M', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(20, 2, null, '产品经理', 1, 'M', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(21, 2, null, '项目经理', 1, 'M', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(22, 2, null, '系统架构师', 1, 'T', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(23, 2, null, '硬件工程师', 1, 'T', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(24, 2, null, '运维工程师', 1, 'T', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(25, 2, null, '测试工程师', 1, 'T', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(26, 2, null, '嵌入式工程师', 1, 'T', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(27, 2, null, 'UI设计师', 1, 'T', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(28, 2, null, '前端工程师', 1, 'T', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(29, 2, null, 'Python工程师', 1, 'T', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(30, 2, null, 'Java工程师', 1, 'T', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(8, 2, null, '前台接待', 1, 'A', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(9, 2, null, '行政专员', 1, 'A', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(10, 2, null, '行政主管', 1, 'A', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(13, 2, null, '销售专员', 1, 'S', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(14, 2, null, '销售经理', 1, 'S', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(15, 2, null, '招聘专员', 1, 'A', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(16, 2, null, '招聘主管', 1, 'A', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(11, 2, null, '市场专员', 1, 'A', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(12, 2, null, '运营经理', 1, 'A', 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
select setval(pg_get_serial_sequence('sys_post', 'post_id'), (select max(post_id) from sys_post));

-- 部门岗位
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 1, 1, 1);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 1, 2, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 1, 3, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 1, 4, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 1, 5, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 1, 6, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 1, 7, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 7, 8, 1);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 7, 9, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 7, 10, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 8, 12, 1);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 4, 13, 1);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 4, 14, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 6, 15, 1);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 6, 16, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 5, 17, 1);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 5, 18, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 3, 19, 1);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 3, 20, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 3, 21, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 3, 22, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 3, 23, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 3, 24, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 3, 25, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 3, 26, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 3, 27, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 3, 28, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 3, 29, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 3, 30, 0);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 9, 11, 1);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 11, 11, 1);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 10, 13, 1);
insert into sys_dept_post (tenant_id, dept_id, post_id, is_default) values (2, 10, 14, 0);

-- 部门关系
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (1, 0, 2);
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (2, 0, 2);
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (3, 1, 2);
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (4, 1, 2);
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (5, 1, 2);
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (6, 1, 2);
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (7, 1, 2);
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (8, 1, 2);
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (9, 1, 2);
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (12, 1, 2);
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (10, 2, 2);
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (11, 2, 2);
insert into sys_dept_diagram (dept_id, parent_id, tenant_id) values (12, 2, 2);

-- 岗位关系
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (2, 1, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (3, 1, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (4, 1, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (5, 1, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (6, 1, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (7, 1, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (10, 3, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (12, 6, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (14, 5, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (16, 7, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (17, 4, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (18, 4, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (19, 2, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (1, 0, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (15, 16, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (11, 12, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (13, 14, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (8, 10, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (9, 10, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (27, 21, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (28, 21, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (29, 21, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (30, 21, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (21, 19, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (20, 19, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (25, 21, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (22, 19, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (23, 21, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (24, 21, 2);
insert into sys_post_diagram (post_id, parent_id, tenant_id) values (26, 21, 2);

-- 用户数据
insert into sys_user (user_id, user_account, user_name, user_nick, user_sex, user_phone, user_email, user_avatar, user_sign, user_status, mfa, remark, create_by, create_time, update_by, update_time) values
(1, 'sysAdmin', '系统管理员', null, 0, null, null, null, null, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 'liubei', '刘备', null, 0, '13288888888', 'liubei@cowave.com', null, null, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(3, 'zhugeliang', '诸葛亮', null, 0, '13288888888', 'zhugeliang@cowave.com', null, null, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(4, 'guanyu', '关羽', null, 0, '13288888888', 'guanyu@cowave.com', null, null, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(5, 'zhangfei', '张飞', null, 0, '13288888888', 'zhangfei@cowave.com', null, null, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(6, 'machao', '马超', null, 0, '13288888888', null, null, null, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(7, 'zhaoyun', '赵云', null, 0, '13288888888', null, null, null, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(8, 'huangzhong', '黄忠', null, 0, '13288888888', null, null, null, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(9, 'daqiao', '大乔', null, 1, '13288888888', 'daqiao@cowave.com', null, null, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(10, 'xiaoqiao', '小乔', null, 1, '13288888888', 'xiaoqiao@cowave.com', null, null, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(11, 'mia', '米娅', null, 0, null, null, null, null, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
select setval(pg_get_serial_sequence('sys_user', 'user_id'), (select max(user_id) from sys_user));

-- 租户用户
insert into sys_tenant_user (tenant_id, user_id, user_type, user_code, display_name, user_rank, status, is_default, join_time, leave_time, remark, create_by, create_time, update_by, update_time) values
(1, 1, 'sys', 'system-sys-sysAdmin', '系统管理员', null, 1, 1, '2022-04-25 09:00:00+08', null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 2, 'sys', 'cowave-sys-liubei', '刘备', 'M7', 1, 1, '2022-04-25 09:00:00+08', null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 3, 'sys', 'cowave-sys-zhugeliang', '诸葛亮', 'M7', 1, 1, '2022-04-25 09:00:00+08', null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 4, 'sys', 'cowave-sys-guanyu', '关羽', 'M7', 1, 1, '2022-04-25 09:00:00+08', null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 5, 'sys', 'cowave-sys-zhangfei', '张飞', 'M7', 1, 1, '2022-04-25 09:00:00+08', null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 6, 'sys', 'cowave-sys-machao', '马超', 'M7', 1, 1, '2022-04-25 09:00:00+08', null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 7, 'sys', 'cowave-sys-zhaoyun', '赵云', 'M7', 1, 1, '2022-04-25 09:00:00+08', null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 8, 'sys', 'cowave-sys-huangzhong', '黄忠', 'M7', 1, 1, '2022-04-25 09:00:00+08', null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 9, 'sys', 'cowave-sys-daqiao', '大乔', 'M2', 1, 1, '2022-04-25 09:00:00+08', null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 10, 'sys', 'cowave-sys-xiaoqiao', '小乔', 'M2', 1, 1, '2022-04-25 09:00:00+08', null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(3, 11, 'sys', 'open-sys-mia', '米娅', null, 1, 1, '2022-04-25 09:00:00+08', null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');

-- Open Hub 是永久公共访客租户，所有用户均保留一条启用成员关系；已有正式默认租户的用户不将其设为默认。
insert into sys_tenant_user
    (tenant_id, user_id, user_type, user_code, display_name, status, is_default, join_time, create_by, create_time)
select 3,
       u.user_id,
       'external',
       'open-visitor-' || u.user_id,
       u.user_name,
       1,
       0,
       '2022-04-25 09:00:00+08',
       null,
       '2022-04-25 09:00:00+08'
from sys_user u
where not exists (
    select 1
    from sys_tenant_user tu
    where tu.tenant_id = 3
      and tu.user_id = u.user_id
);

-- 用户密码
insert into sys_auth_passwd (passwd_id, user_id, passwd_hash, passwd_algo, is_current, need_change, effective_time, expire_time, invalid_time, change_source, create_by, create_time, update_by, update_time) values
(1, 1, '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 'bcrypt', 1, 1, '2022-04-25 09:00:00+08', null, null, 'initial', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 2, '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 'bcrypt', 1, 1, '2022-04-25 09:00:00+08', null, null, 'initial', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(3, 3, '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 'bcrypt', 1, 1, '2022-04-25 09:00:00+08', null, null, 'initial', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(4, 4, '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 'bcrypt', 1, 1, '2022-04-25 09:00:00+08', null, null, 'initial', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(5, 5, '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 'bcrypt', 1, 1, '2022-04-25 09:00:00+08', null, null, 'initial', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(6, 6, '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 'bcrypt', 1, 1, '2022-04-25 09:00:00+08', null, null, 'initial', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(7, 7, '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 'bcrypt', 1, 1, '2022-04-25 09:00:00+08', null, null, 'initial', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(8, 8, '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 'bcrypt', 1, 1, '2022-04-25 09:00:00+08', null, null, 'initial', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(9, 9, '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 'bcrypt', 1, 1, '2022-04-25 09:00:00+08', null, null, 'initial', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(10, 10, '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 'bcrypt', 1, 1, '2022-04-25 09:00:00+08', null, null, 'initial', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(11, 11, '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 'bcrypt', 1, 1, '2022-04-25 09:00:00+08', null, null, 'initial', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
select setval(pg_get_serial_sequence('sys_auth_passwd', 'passwd_id'), (select max(passwd_id) from sys_auth_passwd));

-- 角色数据：system租户保存模板，cowave和open租户复制模板
insert into sys_role (role_id, tenant_id, role_code, role_name, role_type, role_status, is_template, remark, create_by, create_time, update_by, update_time) values
(1, 1, 'sysAdmin', '系统管理员', 'system', 1, 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 1, 'flowAdmin', '流程管理员', 'system', 1, 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(3, 1, 'visitor', '访客', 'system', 1, 1, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(4, 2, 'sysAdmin', '系统管理员', 'system', 1, 0, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(5, 2, 'flowAdmin', '流程管理员', 'system', 1, 0, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(6, 2, 'visitor', '访客', 'system', 1, 0, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(7, 3, 'sysAdmin', '系统管理员', 'system', 1, 0, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(8, 3, 'flowAdmin', '流程管理员', 'system', 1, 0, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(9, 3, 'visitor', '访客', 'system', 1, 0, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');

-- 菜单数据
INSERT INTO "sys_menu" ("menu_id", "parent_id", "tenant_id", "menu_module", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
(4, 0, 2, NULL, 'commons.menu.cowave', 100, NULL, 'https://www.cowave.com', NULL, 'C', 'guide', NULL, 1, 0, 1, 1, 0, '控维官网', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 系统管理
(1, 0, 1, NULL, 'commons.menu.sys.root', 7, NULL, 'system', NULL, 'M', 'system', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 租户管理
(173, 1, 1, NULL, 'commons.menu.sys.tenant', 1, NULL, 'tenant', NULL, 'C', 'tenant', 'system/tenant/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(174, 173, 1, 'module_tenant', 'commons.button.query', 1, 'sys:tenant:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(175, 173, 1, 'module_tenant', 'commons.button.create', 2, 'sys:tenant:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(176, 173, 1, 'module_tenant', 'commons.button.edit', 3, 'sys:tenant:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(177, 173, 1, 'module_tenant', 'commons.button.status', 4, 'sys:tenant:status', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(190, 173, 1, 'module_tenant', 'tenant.button.manager', 5, 'sys:tenant:manager:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(191, 173, 1, 'module_tenant', 'tenant.button.manager_add', 6, 'sys:tenant:manager:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(192, 173, 1, 'module_tenant', 'tenant.button.manager_remove', 7, 'sys:tenant:manager:remove', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 身份权限目录
(220, 1, 1, NULL, 'commons.menu.sys.identity', 2, NULL, 'identity', NULL, 'M', 'auth', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 用户管理
(5, 220, 1, NULL, 'commons.menu.sys.user', 1, NULL, 'user', NULL, 'C', 'user', 'system/user/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(22, 5, 1, 'module_user', 'commons.button.query', 1, 'sys:user:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(23, 5, 1, 'module_user', 'commons.button.create', 2, 'sys:user:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(24, 5, 1, 'module_user', 'commons.button.edit', 3, 'sys:user:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(25, 5, 1, 'module_user', 'commons.button.delete', 4, 'sys:user:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(26, 5, 1, 'module_user', 'commons.button.export', 5, 'sys:user:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(27, 5, 1, 'module_user', 'commons.button.import', 6, 'sys:user:import', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(88, 5, 1, 'module_user', 'commons.button.diagram', 7, 'sys:user:diagram', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(85, 5, 1, 'module_user', 'user.button.grant', 9, 'sys:user:grant', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(28, 5, 1, 'module_user', 'user.button.passwd', 10, 'sys:user:passwd', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(112, 5, 1, 'module_user', 'commons.button.status', 11, 'sys:user:status', '#', NULL, 'B', '#', NULL, 1, 1, 1, 0, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 角色管理
(6, 220, 1, NULL, 'commons.menu.sys.role', 2, NULL, 'role', NULL, 'C', 'identity', 'system/role/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(29, 6, 1, 'module_role', 'commons.button.query', 1, 'sys:role:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(30, 6, 1, 'module_role', 'commons.button.create', 2, 'sys:role:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(31, 6, 1, 'module_role', 'commons.button.edit', 3, 'sys:role:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(32, 6, 1, 'module_role', 'commons.button.delete', 4, 'sys:role:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(33, 6, 1, 'module_role', 'commons.button.export', 5, 'sys:role:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(97, 6, 1, 'module_role', 'role.button.menus', 6, 'sys:role:menus', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(99, 6, 1, 'module_role', 'role.button.members', 8, 'sys:role:members:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(178, 6, 1, 'module_role', 'role.button.members_grant', 9, 'sys:role:members:grant', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(179, 6, 1, 'module_role', 'role.button.members_cancel', 10, 'sys:role:members:cancle', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 部门管理
(8, 220, 1, NULL, 'commons.menu.sys.dept', 3, NULL, 'dept', NULL, 'C', 'dept', 'system/dept/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(39, 8, 1, 'module_dept', 'commons.button.query', 1, 'sys:dept:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(40, 8, 1, 'module_dept', 'commons.button.create', 2, 'sys:dept:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(41, 8, 1, 'module_dept', 'commons.button.edit', 3, 'sys:dept:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(42, 8, 1, 'module_dept', 'commons.button.delete', 4, 'sys:dept:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(90, 8, 1, 'module_dept', 'commons.button.export', 5, 'sys:dept:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(91, 8, 1, 'module_dept', 'commons.button.diagram', 6, 'sys:dept:diagram', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(180, 8, 1, 'module_dept', 'dept.button.members', 9, 'sys:dept:members:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(181, 8, 1, 'module_dept', 'dept.button.members_add', 10, 'sys:dept:members:add', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(182, 8, 1, 'module_dept', 'dept.button.members_remove', 11, 'sys:dept:members:remove', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(183, 8, 1, 'module_dept', 'dept.button.positions', 12, 'sys:dept:positions:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(184, 8, 1, 'module_dept', 'dept.button.positions_add', 13, 'sys:dept:positions:add', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(185, 8, 1, 'module_dept', 'dept.button.positions_remove', 14, 'sys:dept:positions:remove', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 岗位管理
(9, 220, 1, NULL, 'commons.menu.sys.post', 4, NULL, 'post', NULL, 'C', 'post', 'system/post/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(43, 9, 1, 'module_post', 'commons.button.query', 1, 'sys:post:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(44, 9, 1, 'module_post', 'commons.button.create', 2, 'sys:post:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(45, 9, 1, 'module_post', 'commons.button.edit', 3, 'sys:post:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(46, 9, 1, 'module_post', 'commons.button.delete', 4, 'sys:post:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(47, 9, 1, 'module_post', 'commons.button.export', 5, 'sys:post:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(95, 9, 1, 'module_post', 'commons.button.diagram', 6, 'sys:post:diagram', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 菜单管理
(7, 220, 1, NULL, 'commons.menu.sys.menu', 5, NULL, 'menu', NULL, 'C', 'form', 'system/menu/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(34, 7, 1, 'module_menu', 'commons.button.query', 1, 'sys:menu:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(35, 7, 1, 'module_menu', 'commons.button.create', 2, 'sys:menu:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(36, 7, 1, 'module_menu', 'commons.button.edit', 3, 'sys:menu:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(37, 7, 1, 'module_menu', 'commons.button.delete', 4, 'sys:menu:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(100, 7, 1, 'module_menu', 'commons.button.export', 5, 'sys:menu:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 数据权限
(195, 220, 1, NULL, 'commons.menu.sys.scope', 6, NULL, 'scope', NULL, 'C', 'vscope', 'system/scope/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(196, 195, 1, 'module_scope', 'commons.button.query', 1, 'sys:scope:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(197, 195, 1, 'module_scope', 'commons.button.delete', 2, 'sys:scope:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(198, 195, 1, 'module_scope', 'commons.button.create', 3, 'sys:scope:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(199, 195, 1, 'module_scope', 'commons.button.edit', 4, 'sys:scope:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 在线用户
(14, 1, 1, NULL, 'commons.menu.monitor.online', 3, NULL, 'online', NULL, 'C', 'online', 'monitor/online/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(64, 14, 1, 'module_online', 'commons.button.query', 1, 'monitor:online:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(65, 14, 1, 'module_online', 'commons.button.quit', 2, 'monitor:online:force', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 操作日志
(13, 1, 1, NULL, 'commons.menu.monitor.log', 4, NULL, 'log', NULL, 'C', 'log', 'monitor/operlog/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(79, 13, 1, 'module_oplog', 'commons.button.query', 1, 'monitor:log:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(80, 13, 1, 'module_oplog', 'commons.button.delete', 2, 'monitor:log:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(81, 13, 1, 'module_oplog', 'commons.button.export', 3, 'monitor:log:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(82, 13, 1, 'module_oplog', 'commons.button.clean', 4, 'monitor:log:clean', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 系统配置目录
(221, 1, 1, NULL, 'commons.menu.sys.config', 5, NULL, 'config', NULL, 'M', 'param', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 系统参数
(11, 221, 1, NULL, 'commons.menu.sys.params', 1, NULL, 'params', NULL, 'C', 'tree-table', 'system/config/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(53, 11, 1, 'module_config', 'commons.button.query', 1, 'sys:config:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(54, 11, 1, 'module_config', 'commons.button.create', 2, 'sys:config:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(55, 11, 1, 'module_config', 'commons.button.edit', 3, 'sys:config:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(56, 11, 1, 'module_config', 'commons.button.delete', 4, 'sys:config:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(57, 11, 1, 'module_config', 'commons.button.export', 5, 'sys:config:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(102, 11, 1, 'module_config', 'config.button.reset', 6, 'sys:config:reset', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 字典管理
(10, 221, 1, NULL, 'commons.menu.sys.dict', 2, NULL, 'dict', NULL, 'C', 'dict', 'system/dict/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(48, 10, 1, 'module_dict', 'commons.button.query', 1, 'sys:dict:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(49, 10, 1, 'module_dict', 'commons.button.create', 2, 'sys:dict:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(50, 10, 1, 'module_dict', 'commons.button.edit', 3, 'sys:dict:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(51, 10, 1, 'module_dict', 'commons.button.delete', 4, 'sys:dict:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(52, 10, 1, 'module_dict', 'commons.button.export', 5, 'sys:dict:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 定时任务
(15, 221, 1, NULL, 'commons.menu.sys.schedule.root', 3, NULL, 'job', NULL, 'C', 'job', 'system/job/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(67, 15, 1, 'module_task', 'commons.button.query', 1, 'sys:job:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(68, 15, 1, 'module_task', 'commons.button.create', 2, 'sys:job:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(69, 15, 1, 'module_task', 'commons.button.edit', 3, 'sys:job:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(70, 15, 1, 'module_task', 'commons.button.delete', 4, 'sys:job:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(72, 15, 1, 'module_task', 'commons.button.export', 5, 'sys:job:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(117, 15, 1, 'module_task', 'commons.button.exec', 6, 'sys:job:exec', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(71, 15, 1, 'module_task', 'commons.button.status', 7, 'sys:job:status', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(118, 15, 1, 'module_task', 'commons.menu.sys.schedule.refresh', 8, 'sys:job:refresh', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(138, 15, 1, 'module_task', 'commons.menu.sys.schedule.logQuery', 9, 'sys:job:log:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(140, 15, 1, 'module_task', 'commons.menu.sys.schedule.logExport', 9, 'sys:job:log:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(139, 15, 1, 'module_task', 'commons.menu.sys.schedule.logDelete', 9, 'sys:job:log:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 文件管理
(200, 221, 1, NULL, 'commons.menu.sys.attach', 4, NULL, 'attach', NULL, 'C', 'attach', 'system/attach/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(201, 200, 1, 'module_attach', 'commons.button.query', 1, 'sys:attach:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(202, 200, 1, 'module_attach', 'commons.button.delete', 2, 'sys:attach:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(203, 200, 1, 'module_attach', 'commons.button.preview', 3, 'sys:attach:preview', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(204, 200, 1, 'module_attach', 'commons.button.download', 4, 'sys:attach:download', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 授权认证目录
(222, 1, 1, NULL, 'commons.menu.sys.auth', 7, NULL, 'auth', NULL, 'M', 'authorization', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- Ldap认证
(148, 222, 1, NULL, 'commons.menu.sys.ldap', 1, NULL, 'ldap', NULL, 'C', 'ldap', 'system/ldap/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(149, 148, 1, 'module_ldap', 'commons.button.query', 1, 'sys:ldap:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(150, 148, 1, 'module_ldap', 'commons.button.create', 2, 'sys:ldap:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(151, 148, 1, 'module_ldap', 'commons.button.edit', 3, 'sys:ldap:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(152, 148, 1, 'module_ldap', 'commons.button.delete', 4, 'sys:ldap:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(153, 148, 1, 'module_ldap', 'commons.button.test', 5, 'sys:ldap:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(154, 148, 1, 'module_ldap', 'commons.button.status', 6, 'sys:ldap:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- OAuth2认证
(142, 222, 1, NULL, 'commons.menu.sys.oauth2.root', 2, NULL, 'oauth2', NULL, 'C', 'oauth', 'system/oauth/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(143, 142, 1, 'module_oauth', 'commons.menu.sys.oauth2.configQuery', 1, 'oauth:provider:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(144, 142, 1, 'module_oauth', 'commons.menu.sys.oauth2.configEdit', 2, 'oauth:provider:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(145, 142, 1, 'module_oauth', 'commons.menu.sys.oauth2.userQuery', 3, 'oauth:provider:user:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(146, 142, 1, 'module_oauth', 'commons.menu.sys.oauth2.userEdit', 4, 'oauth:provider:user:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(147, 142, 1, 'module_oauth', 'commons.menu.sys.oauth2.userDelete', 5, 'oauth:provider:user:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 开发者文档
(21, 1, 1, NULL, 'commons.menu.sys.doc.api', 12, NULL, 'doc', NULL, 'M', 'develop', '', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(115, 21, 1, NULL, 'commons.menu.sys.doc.admin', 1, NULL, 'admin', NULL, 'C', 'api', 'system/doc/admin', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(116, 21, 1, NULL, 'commons.menu.sys.doc.job', 2, NULL, 'job', NULL, 'C', 'api', 'system/doc/job', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(126, 21, 1, NULL, 'commons.menu.sys.doc.meter', 3, NULL, 'meter', NULL, 'C', 'api', 'system/doc/meter', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 流程配置
(155, 1, 1, NULL, 'commons.menu.flow.manage', 8, NULL, 'manage', NULL, 'M', 'cascader', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(157, 155, 1, NULL, 'commons.menu.flow.model', 1, 'flow:modeler', 'modeler', NULL, 'C', 'component', 'flow/modeler', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(158, 155, 1, NULL, 'commons.menu.flow.deploy', 2, 'flow:deploy', 'deploy', NULL, 'C', 'deploy', 'flow/deploy', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(156, 155, 1, NULL, 'commons.menu.flow.instance', 3, 'flow:instance', 'instance', NULL, 'C', 'flowinstance', 'flow/instance', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 系统监控
(2, 1, 1, NULL, 'commons.menu.monitor.root', 9, NULL, 'monitor', NULL, 'M', 'monitor', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 监控页面
(165, 2, 1, NULL, 'commons.menu.monitor.nacos', 3, NULL, 'monitor-nacos', NULL, 'C', 'nacos', 'monitor/nacos/index', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(164, 2, 1, NULL, 'commons.menu.monitor.actuator', 4, NULL, 'monitor-actuator', NULL, 'C', 'health', 'monitor/actuator/index', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(130, 2, 1, NULL, 'commons.menu.monitor.alert', 5, NULL, 'monitor-alert', NULL, 'C', 'alert', 'monitor/alert/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(166, 2, 1, NULL, 'commons.menu.monitor.grafana', 6, NULL, 'monitor-grafana', NULL, 'C', 'grafana', 'monitor/grafana/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(167, 2, 1, NULL, 'commons.menu.monitor.prometheus', 7, NULL, 'monitor-prometheus', NULL, 'C', 'prometheus', 'monitor/prometheus/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 工作台
(124, 0, 1, NULL, 'commons.menu.meter.workspace', 1, NULL, 'meter', NULL, 'M', 'workspace', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

(160, 124, 1, NULL, 'commons.menu.flow.owner.task', 1, 'flow:task', 'task', NULL, 'C', 'task', 'flow/workbench/task', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(161, 124, 1, NULL, 'commons.menu.flow.owner.leave', 2, 'flow:leave', 'leave', NULL, 'C', 'leave', 'flow/workbench/leave', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(162, 124, 1, NULL, 'commons.menu.flow.owner.meeting', 3, 'flow:meeting', 'meeting', NULL, 'C', 'meeting', 'flow/workbench/meeting', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(163, 124, 1, NULL, 'commons.menu.flow.owner.purchase', 4, 'flow:purchase', 'purchase', NULL, 'C', 'purchase', 'flow/workbench/purchase', 1, 1, 1, 1, 0, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 构建管理
(250, 124, 1, NULL, 'commons.menu.meter.build.root', 5, NULL, 'build', NULL, 'C', 'compile', 'meter/build/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 测试管理
(300, 124, 1, NULL, 'commons.menu.meter.test.root', 6, NULL, 'test', NULL, 'M', 'test', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(301, 300, 1, NULL, 'commons.menu.meter.test.ui', 8, NULL, 'ui', NULL, 'C', 'meter_ui', 'meter/test/ui/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 版本管理
(350, 124, 1, NULL, 'commons.menu.meter.archive.root', 7, NULL, 'archive', NULL, 'M', 'archive', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 环境资源
(210, 124, 1, NULL, 'commons.menu.meter.env.root', 8, NULL, 'env', NULL, 'M', 'env', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(211, 210, 1, NULL, 'commons.menu.meter.env.credential', 1, NULL, 'credential', NULL, 'C', 'credential', 'meter/env/credential/index', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 开发设计
(132, 124, 1, NULL, 'commons.menu.meter.develop.root', 9, NULL, 'template', NULL, 'M', 'code', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(19, 132, 1, NULL, 'commons.menu.meter.develop.form', 1, NULL, 'form', NULL, 'C', 'form', 'meter/develop/form/index', 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(134, 132, 1, NULL, 'commons.menu.meter.develop.application', 2, NULL, 'application', NULL, 'C', 'app', 'meter/develop/application', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(135, 132, 1, NULL, 'commons.menu.meter.develop.model', 3, NULL, 'model', NULL, 'C', 'model', 'meter/develop/model', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(136, 132, 1, NULL, 'commons.menu.meter.develop.database', 4, NULL, 'db', NULL, 'C', 'db', 'meter/develop/db', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(137, 132, 1, NULL, 'commons.menu.meter.develop.table', 5, NULL, 'table', NULL, 'C', 'table', 'meter/develop/table', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- Home门户
(400, 1, 1, NULL, 'commons.menu.home.root', 6, NULL, 'home', NULL, 'M', 'home', NULL, 1, 1, 1, 1, 1, '', NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 授权服务
(401, 400, 1, NULL, 'commons.menu.home.service', 1, NULL, 'client', NULL, 'C', 'oauth', 'system/oauth/client', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 授权用户
(411, 400, 1, NULL, 'commons.menu.home.user', 2, NULL, 'client', NULL, 'C', 'peoples', 'system/oauth/client', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),

-- 应用导航
(421, 400, 1, NULL, 'commons.menu.home.app', 3, NULL, 'client', NULL, 'C', 'app', 'system/oauth/client', 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(422, 421, 1, 'module_oauth', 'commons.button.query', 1, 'oauth:app:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(423, 421, 1, 'module_oauth', 'commons.button.create', 2, 'oauth:app:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08'),
(424, 421, 1, 'module_oauth', 'commons.button.delete', 3, 'oauth:app:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, NULL, '2022-04-25 09:00:00+08', NULL, '2022-04-25 09:00:00+08');

update sys_menu set is_template = 1 where tenant_id = 1;

-- 复制system租户模板菜单到cowave和open租户
insert into sys_menu (menu_id, parent_id, tenant_id, menu_module, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, is_template, remark, create_by, create_time, update_by, update_time)
select menu_id + 1000, case when parent_id = 0 then 0 else parent_id + 1000 end, 2, menu_module, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, 0, remark, create_by, create_time, update_by, update_time from sys_menu where tenant_id = 1;

insert into sys_menu (menu_id, parent_id, tenant_id, menu_module, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, is_template, remark, create_by, create_time, update_by, update_time)
select menu_id + 2000, case when parent_id = 0 then 0 else parent_id + 2000 end, 3, menu_module, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, 0, remark, create_by, create_time, update_by, update_time from sys_menu where tenant_id = 1;

select setval(pg_get_serial_sequence('sys_menu', 'menu_id'), (select max(menu_id) from sys_menu));

-- 用户部门岗位
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 2, 1, 1, 1, 1);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 3, 1, 2, 1, 0);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 4, 1, 3, 1, 0);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 5, 1, 6, 1, 0);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 7, 1, 4, 1, 0);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 6, 1, 7, 1, 0);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 8, 1, 5, 1, 0);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 3, 3, 19, 0, 1);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 4, 7, 10, 0, 1);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 5, 8, 12, 0, 1);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 7, 5, 18, 0, 1);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 6, 6, 16, 0, 1);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 8, 4, 14, 0, 1);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 9, 5, 17, 1, 0);
insert into sys_user_dept (tenant_id, user_id, dept_id, post_id, is_primary, is_leader) values (2, 10, 5, 18, 1, 0);

-- 用户角色
insert into sys_user_role (tenant_id, user_id, role_id, grant_type, granted_by, granted_time) values (1, 1, 1, 'direct', 'system', '2022-04-25 09:00:00+08');
insert into sys_user_role (tenant_id, user_id, role_id, grant_type, granted_by, granted_time) values (2, 2, 4, 'direct', 'system', '2022-04-25 09:00:00+08');
insert into sys_user_role (tenant_id, user_id, role_id, grant_type, granted_by, granted_time) values (2, 3, 6, 'direct', 'system', '2022-04-25 09:00:00+08');
insert into sys_user_role (tenant_id, user_id, role_id, grant_type, granted_by, granted_time) values (2, 4, 6, 'direct', 'system', '2022-04-25 09:00:00+08');
insert into sys_user_role (tenant_id, user_id, role_id, grant_type, granted_by, granted_time) values (2, 5, 6, 'direct', 'system', '2022-04-25 09:00:00+08');
insert into sys_user_role (tenant_id, user_id, role_id, grant_type, granted_by, granted_time) values (2, 6, 6, 'direct', 'system', '2022-04-25 09:00:00+08');
insert into sys_user_role (tenant_id, user_id, role_id, grant_type, granted_by, granted_time) values (2, 7, 6, 'direct', 'system', '2022-04-25 09:00:00+08');
insert into sys_user_role (tenant_id, user_id, role_id, grant_type, granted_by, granted_time) values (2, 8, 6, 'direct', 'system', '2022-04-25 09:00:00+08');
insert into sys_user_role (tenant_id, user_id, role_id, grant_type, granted_by, granted_time) values (3, 11, 7, 'direct', 'system', '2022-04-25 09:00:00+08');

-- Open Hub 所有访客统一授予访客角色，保留其永久访问能力。
insert into sys_user_role (tenant_id, user_id, role_id, grant_type, granted_by, granted_time)
select 3,
       tu.user_id,
       9,
       'direct',
       'system',
       '2022-04-25 09:00:00+08'
from sys_tenant_user tu
where tu.tenant_id = 3
  and not exists (
      select 1
      from sys_user_role ur
      where ur.tenant_id = 3
        and ur.user_id = tu.user_id
        and ur.role_id = 9
  );

-- 用户关系
insert into sys_user_diagram (parent_id, user_id, tenant_id, relation_type) values (0, 1, 1, 'direct');
insert into sys_user_diagram (parent_id, user_id, tenant_id, relation_type) values (0, 2, 2, 'direct');
insert into sys_user_diagram (parent_id, user_id, tenant_id, relation_type) values (2, 3, 2, 'direct');
insert into sys_user_diagram (parent_id, user_id, tenant_id, relation_type) values (2, 4, 2, 'direct');
insert into sys_user_diagram (parent_id, user_id, tenant_id, relation_type) values (2, 5, 2, 'direct');
insert into sys_user_diagram (parent_id, user_id, tenant_id, relation_type) values (2, 6, 2, 'direct');
insert into sys_user_diagram (parent_id, user_id, tenant_id, relation_type) values (2, 7, 2, 'direct');
insert into sys_user_diagram (parent_id, user_id, tenant_id, relation_type) values (2, 8, 2, 'direct');
insert into sys_user_diagram (parent_id, user_id, tenant_id, relation_type) values (0, 11, 3, 'direct');

-- 访客菜单
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 1, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 5, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 8, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 9, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 6, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 11, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 141, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 142, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 148, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 169, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 170, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 195, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 196, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 2, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 14, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 13, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 15, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 22, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 88, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 39, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 91, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 180, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 183, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 99, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 43, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 95, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 29, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 53, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 129, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 143, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 145, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 149, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 18, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 64, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 79, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 3, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 21, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 115, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 116, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 126, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 19, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 132, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 133, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 134, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 135, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 136, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 137, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 67, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 117, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 71, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 138, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 124, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1001, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1005, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1008, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1009, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1006, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1011, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1141, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1142, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1148, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1169, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1170, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1195, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1196, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1002, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1014, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1013, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1015, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1022, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1088, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1039, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1091, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1180, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1183, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1099, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1043, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1095, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1029, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1053, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1129, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1143, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1145, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1149, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1018, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1064, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1079, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1003, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1021, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1115, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1116, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1126, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1019, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1132, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1133, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1134, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1135, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1136, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1137, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1067, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1117, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1071, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1138, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1124, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2001, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2005, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2008, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2009, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2006, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2011, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2141, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2142, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2148, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2169, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2170, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2195, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2196, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2002, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2014, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2013, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2015, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2022, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2088, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2039, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2091, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2180, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2183, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2099, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2043, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2095, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2029, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2053, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2129, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2143, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2145, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2149, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2018, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2064, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2079, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2003, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2021, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2115, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2116, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2126, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2019, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2132, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2133, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2134, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2135, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2136, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2137, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2067, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2117, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2071, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2138, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2124, null);

-- 流程菜单权限
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 155, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 157, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 158, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (1, 3, 156, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1155, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1157, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1158, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (2, 6, 1156, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2155, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2157, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2158, null);
insert into sys_role_menu (tenant_id, role_id, menu_id, scope_id) values (3, 9, 2156, null);

-- 数据权限，仅初始化cowave租户数据
insert into sys_scope (scope_id, tenant_id, scope_name, scope_module, scope_status, scope_content, remark, create_by, create_time, update_by, update_time) values
(1, 2, '仅本人数据', 'module_oplog', 1, '{"scope":"personal"}', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 2, '本部门数据', 'module_oplog', 1, '{"scope":"dept"}', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(3, 2, '全部数据', 'module_oplog', 1, '{"scope":"all"}', null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
select setval(pg_get_serial_sequence('sys_scope', 'scope_id'), (select max(scope_id) from sys_scope));

-- 认证提供方，迁移旧sys_oauth及hub_oauth中的OAuth和普通链接入口
insert into sys_auth_provider (provider_id, provider_code, provider_type, provider_name, provider_icon, provider_tip, provider_sort, link_url, client_id, client_secret, auth_url, redirect_url, grant_type, response_type, auth_scope, status, create_by, create_time, update_by, update_time)
values (1, 'gitlab', 'oauth', 'Gitlab', '/images/icon/gitlab.png', 'Gitlab用户', 3, null, 'a66456e64cb955b7da257deabe7f088c38327ec63fe3c125e02c5db0407ada23', 'gloas-0300ae665a2f5fc00d4214490c5c174c099c6f023079f8105fd52ac6d7cd0c1b', 'http://localhost:8929/oauth/authorize', 'http://192.168.0.135:8081/oauth/gitlab', 'authorization_code', 'code', 'read_user', 1, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
insert into sys_auth_provider (provider_id, provider_code, provider_type, provider_name, provider_icon, provider_tip, provider_sort, link_url, client_id, client_secret, auth_url, redirect_url, grant_type, response_type, auth_scope, status, create_by, create_time, update_by, update_time)
values (2, 'cowave', 'oauth', 'Hub Admin', '/images/icon/cowave.png', '控维系统用户', 5, null, '6ac6519451ed4ef09431aacccbcb1f5f', null, null, null, null, null, null, 1, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
insert into sys_auth_provider (provider_id, provider_code, provider_type, provider_name, provider_icon, provider_tip, provider_sort, link_url, client_id, client_secret, auth_url, redirect_url, grant_type, response_type, auth_scope, status, create_by, create_time, update_by, update_time)
values (3, 'github', 'oauth', 'GitHub', '/images/icon/github.png', 'GitHub用户', 2, null, null, null, 'https://github.com/login/oauth/authorize', 'http://localhost:8081/oauth/github', 'authorization_code', 'code', 'read:user user:email', 0, null, '2022-04-25 09:00:00+08', null, null);
insert into sys_auth_provider (provider_id, provider_code, provider_type, provider_name, provider_icon, provider_tip, provider_sort, link_url, client_id, client_secret, auth_url, redirect_url, grant_type, response_type, auth_scope, status, create_by, create_time, update_by, update_time)
values (5, 'wechat', 'oauth', '微信', '/images/icon/wechat.png', '微信用户', 4, null, null, null, 'https://open.weixin.qq.com/connect/qrconnect', 'http://localhost:8081/oauth/wechat', 'authorization_code', 'code', 'snsapi_login', 0, null, '2022-04-25 09:00:00+08', null, null);
insert into sys_auth_provider (provider_id, provider_code, provider_type, provider_name, provider_icon, provider_tip, provider_sort, link_url, client_id, client_secret, auth_url, redirect_url, grant_type, response_type, auth_scope, status, create_by, create_time, update_by, update_time)
values (4, 'bilibili', 'oauth', 'Bilibili', '/images/icon/bilibili.png', 'Bilibili用户', 1, null, null, null, 'https://account.bilibili.com/pc/account-pc/auth/oauth', 'http://localhost:8081/oauth/bilibili', 'authorization_code', 'code', 'USER_INFO', 0, null, '2022-04-25 09:00:00+08', null, null);
insert into sys_auth_provider (provider_id, provider_code, provider_type, provider_name, provider_icon, provider_tip, provider_sort, link_url, client_id, client_secret, auth_url, redirect_url, grant_type, response_type, auth_scope, status, create_by, create_time, update_by, update_time)
values (7, 'qq', 'oauth', 'QQ', '/images/icon/qq.png', 'QQ用户', 4, null, null, null, 'https://graph.qq.com/oauth2.0/authorize', 'http://localhost:8081/oauth/qq', 'authorization_code', 'code', 'get_user_info', 0, null, '2022-04-25 09:00:00+08', null, null);
insert into sys_auth_provider (provider_id, provider_code, provider_type, provider_name, provider_icon, provider_tip, provider_sort, link_url, status, create_by, create_time, update_by, update_time)
values (6, 'email', 'link', 'Email', '/images/icon/email.png', '去留言 ~', 0, '/blog/comments', 1, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
select setval(pg_get_serial_sequence('sys_auth_provider', 'provider_id'), (select max(provider_id) from sys_auth_provider));

-- LDAP配置
insert into sys_auth_ldap (ldap_id, ldap_status, ldap_url, ldap_user, ldap_passwd, base_dn, readonly, user_dn, user_class, account_property, subject_property, name_property, email_property, phone_property, post_property, dept_property, leader_property, info_property, create_by, create_time, update_by, update_time)
values (1, 1, 'ldap://10.64.3.1:389', 'zhangyuliang@cowave.com', 'Cowave@123', 'OU=Cowavers,DC=cowave,DC=com', 0, null, 'person', 'sAMAccountName', 'objectGUID', 'displayName', 'userPrincipalName', 'telephoneNumber', 'title', 'department', 'manager', 'distinguishedName', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
select setval(pg_get_serial_sequence('sys_auth_ldap', 'ldap_id'), (select max(ldap_id) from sys_auth_ldap));

-- 门户授权应用
insert into sys_auth_app (app_id, tenant_id, app_name, app_type, app_visible, app_status, app_sort, card_name, card_icon, link_url, client_id, client_secret, grant_type, auth_scope, redirect_url, create_by, create_time, update_by, update_time)
values (1, 2, 'hub-home', 'link', 'public', 1, 0, 'Hub论坛', 'CompactDisc', '/blog', '6ac6519451ed4ef09431aacccbcb1f5f', '4a2e671fbd074f238e80c7f5566f8f7a', '{authorization_code}', '{read_user}', 'http://localhost:3000/oauth/callback?provider=cowave', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
insert into sys_auth_app (app_id, tenant_id, app_name, app_type, app_visible, app_status, app_sort, card_name, card_icon, link_url, create_by, create_time, update_by, update_time)
values (2, 2, 'hub-admin', 'link', 'sys', 1, 1, 'Hub系统管理', 'Cog', 'http://localhost:1024', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
insert into sys_auth_app (app_id, tenant_id, app_name, app_type, app_visible, app_status, app_sort, card_name, card_icon, link_url, create_by, create_time, update_by, update_time)
values (3, 2, 'online-tools', 'link', 'public', 1, 2, '在线工具', 'Fire', 'https://www.jyshare.com', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
insert into sys_auth_app (app_id, tenant_id, app_name, app_type, app_visible, app_status, app_sort, card_name, card_icon, link_url, create_by, create_time, update_by, update_time)
values (4, 2, 'today-news', 'link', 'public', 1, 3, '今日资讯', 'LaptopCode', 'https://hot.imsyy.top', null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
select setval(pg_get_serial_sequence('sys_auth_app', 'app_id'), (select max(app_id) from sys_auth_app));

-- 角色授权门户应用
insert into sys_role_app (tenant_id, role_id, app_id) values (2, 6, 1);

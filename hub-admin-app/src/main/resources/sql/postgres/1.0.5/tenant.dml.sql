-- 租户信息
insert into sys_tenant (tenant_id, tenant_code, tenant_name, tenant_type, title, view_index, tenant_user, tenant_addr, tenant_phone, tenant_email, user_count, status, expire_time, remark, create_by, create_time, update_by, update_time) values
(1, 'system', 'system', 'system', 'tenant.title.system', 'index_system', null, '华清园6栋', null, null, 1, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(2, 'cowave', '控维通信', 'normal', 'tenant.title.cowave', 'index_cowave', null, '华清园6栋', null, null, 9, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08'),
(3, 'open', 'Open Hub', 'normal', 'tenant.title.open', 'index_open', null, '华清园6栋', null, null, 1, 1, null, null, null, '2022-04-25 09:00:00+08', null, '2022-04-25 09:00:00+08');
select setval(pg_get_serial_sequence('sys_tenant', 'tenant_id'), (select max(tenant_id) from sys_tenant));

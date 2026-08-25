# CLAUDE.md

本文件为 Claude Code 在 zoo 框架工程中工作时提供指导。

## 项目概述

`hub-admin` 是基于 **zoo 框架**的系统管理后端服务，采用 **DDD（领域驱动设计）** 多模块工程结构，支持多租户 RBAC 权限管理、Flowable 工作流、系统配置管理等功能。

## 构建与运行

```bash
# 构建全部模块（跳过测试）
mvn clean package -DskipTests

# 运行全部测试
mvn test -pl hub-admin-app

# 运行单个测试类
mvn test -pl hub-admin-app -Dtest=HubConfigControllerTest

# 带覆盖率报告
mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent package
# 聚合报告: hub-test-report/target/site/jacoco-aggregate/jacoco.xml

# 本地启动
cd hub-admin-app && mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 生产部署
cd hub-admin-app/target && ./bin/run.sh start|stop|restart|up
```

## 模块架构

| 模块 | 层级 | 职责 |
|--------|------|------|
| `hub-admin-app` | 应用层 | 启动入口、REST 控制器、Kafka 消费者、Socket.IO 事件、集成测试 |
| `hub-admin-service` | 服务层 | 业务编排，依赖 domain 层的 Biz 接口（写）和 RepositoryFacade 接口（读），与 infra 完全解耦 |
| `hub-admin-domain` | 领域层 | Biz 接口与实现、实体对象、Repository 接口与门面、枚举、国际化 |
| `hub-admin-infra` | 基础设施层 | DAO（MyBatis-Plus `ServiceImpl`）、Mapper XML、仓储实现 |
| `hub-admin-client` | 客户端 | 对外暴露的 DTO 和 Request，完全独立 |
| `hub-test-report` | 报告 | JaCoCo 覆盖率聚合 |

### 调用链路

```
Controller (app) → Service (service) → Biz (domain) + RepositoryFacade (domain)
                                              ↓
                                       DAO (infra) → Mapper XML (infra)
```

### 依赖约定

- **app 模块**：Controller 只依赖 Service 组件；Kafka、SocketIO 等外部接入方式，接口定义在 domain，实现在 infra
- **service 模块**：只依赖 domain 中的 Biz 接口（Command 操作）和 RepositoryFacade 接口（Query 操作）；Service 之间不互相引用
- **domain 模块**：Biz 之间不互相引用；Biz 依赖 Repository 接口；子实体（聚合根下）不定义独立的 Biz/Repository
- **infra 模块**：DAO 之间不互相引用；DAO 依赖 Mapper；子实体的持久化通过聚合根 DAO 统一操作
- **client 模块**：与其它模块无任何引用，完全独立

### 领域子域

| 子域 | 功能 |
|------|------|
| `auth` | 鉴权（登录、MFA 多因素认证、LDAP、系统用户三方登录）、验证码、令牌管理 |
| `rbac` | 租户、用户、角色、菜单、部门、岗位、数据权限范围 |
| `sys` | 系统配置、字典、附件、告警、通知、反馈 |
| `flow` | Flowable 工作流（模型设计、部署、实例运行） |
| `home` | 会员（hub_member/hub_oauth）+ 应用（hub_app）+ 应用 OAuth 授权码流程 |

### 实体命名约定

| 后缀 | 说明 | 定义位置 |
|------|------|------|
| 无后缀 | 领域对象，与持久层库表一致（PO/DO 后缀可省略） | entity 根目录 |
| Command | 写操作入参 | entity/command/ |
| Query | 查询入参 | entity/query/ |
| VO | 客户端视图对象 | entity/vo/ |
| PTO | 持久层传输对象（多表联合查询结果） | entity/pto/ |
| BO | 业务处理过程对象 | entity/bo/ |

> Command/Query/VO/PTO 允许继承 PO/DO（字段差别不大时），减少重复定义。

### 关键注入模式

```
domain/*/biz/xxxBiz.java              # 接口，定义写操作
domain/*/biz/impl/xxxBizImpl.java     # @Component 实现，调用 Repository
domain/*/repository/xxxRepository.java           # 继承 IService<Entity> + RepositoryFacade
domain/*/repository/facade/xxxRepositoryFacade.java  # 只读查询接口
infra/*/dao/xxxDao.java               # @Repository，继承 ServiceImpl<Mapper, Entity>，实现 Repository
```

## 技术栈

- **框架**：Spring Boot 2.7、Spring Security + JWT、Spring Data LDAP
- **ORM**：MyBatis-Plus 3.5，`lambdaQuery()` / `lambdaUpdate()` 链式调用
- **数据库**：PostgreSQL（主）、MySQL（Liquibase 支持）
- **迁移**：Liquibase，SQL 文件在 `hub-admin-app/src/main/resources/sql/`
- **缓存**：Redis (Lettuce)，`@Cacheable` / `@CacheEvict` + `RedisHelper`
- **搜索**：Elasticsearch 7.x
- **消息**：Kafka
- **存储**：MinIO
- **实时通信**：Netty Socket.IO
- **工作流**：Flowable 6.8（内嵌引擎）
- **工具**：Lombok、EasyExcel、Kaptcha

## 配置说明

- **主配置**：`src/main/resources/config/application.yml` 激活 `dev`/`prod` profile
- **框架配置**：`META-INF/zoo.yml`（i18n、MyBatis-Plus、Actuator）
- **日志**：`logback-spring.xml`
- **部署配置**：`bin/env.properties` 覆盖 JVM 参数、端口、数据源等
- **运行参数**：端口 19010，context-path `/admin`
- **认证**：`access-refresh` 模式，access token 60 分钟；`@AnonymousGetMapping`/`@AnonymousPostMapping` 声明匿名接口；`@PreAuthorize("@permits.hasPermit('...')")` 权限控制

## 测试

- 测试基类 `SpringTest` 在静态块中启动 6 个 Testcontainers（PostgreSQL 13、Redis 7、ZK、Kafka、MinIO、ES 7）
- `SpringTestConfiguration` 提供连接到容器的测试 Bean
- MockMvc 辅助方法：`mockPost`、`mockGet`、`mockPatch`、`mockDelete`、`mockExport`、`mockImport`
- 测试风格：端到端场景（登录 → 操作 → 验证 → 退出）

## 包结构

```
com.cowave.hub.admin
├── controller.{auth|rbac|sys|flow|home}    # app 模块
├── kafka.consumer                      # Kafka 消费者
├── socketio.event                      # Socket.IO 事件
├── service.{auth|rbac|sys|flow|home}       # service 模块（接口 + impl）
├── domain.{auth|rbac|sys|flow|home}        # domain 模块
│   ├── biz / impl                      # 业务逻辑
│   ├── entity / {command|query|vo|pto|bo}  # 领域对象
│   ├── enums                           # 枚举
│   └── repository / facade             # 仓储接口与门面
└── infra.{auth|rbac|sys|flow|home}         # infra 模块
    ├── dao / mapper                    # 仓储实现
    └── sender / store                  # 外部组件适配（sys）
```

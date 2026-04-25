# 🎓 EduFlow Platform - 智慧教育管理平台

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Docker](https://img.shields.io/badge/Docker-Support-blue.svg)](https://www.docker.com/)

**EduFlow Platform** 是一个基于 Spring Boot 3 的现代化、企业级智慧教育管理平台，集成了学生管理、成绩分析、教师考核、工作流引擎、内容管理和 AI 智能生成等核心功能。

---

## ✨ 核心特性

### 🏫 教育教学管理
- **学生信息管理**：完整的学生档案系统，支持批量导入导出
- **班级与学科管理**：灵活的班级组织架构和学科配置
- **考试成绩系统**：多维度成绩录入、查询和分析
- **教师考核评估**：自动化的教师绩效考核体系
- **数据对比分析**：历年成绩对比、同期成绩分析、进步趋势追踪

### 📊 智能数据分析
- **班级成绩分析**：按班级维度的综合成绩统计
- **学科成绩分析**：各学科成绩分布和趋势分析
- **个体成绩追踪**：学生个人成绩成长轨迹
- **可视化报告**：自动生成学生、教师、班级多维报告

### 🔐 完善的权限系统
- **RBAC 权限模型**：基于角色的访问控制
- **多级组织架构**：支持部门、角色、用户三级管理
- **细粒度权限控制**：菜单、按钮级别的权限管理
- **登录安全机制**：验证码、Token 认证、会话管理

### 🔄 可视化工作流引擎
- **流程设计器**：拖拽式可视化流程设计
- **流程实例管理**：完整的流程执行跟踪
- **节点配置**：灵活的任务节点和审批规则
- **流程事件记录**：全流程操作日志追溯

### 📝 内容管理系统
- **文章发布管理**：富文本编辑器，支持图文混排
- **分类管理**：多级文章分类体系
- **首页组件配置**：轮播图、产品展示动态配置
- **SEO 优化**：友好的 URL 和元数据管理

### 🤖 AI 智能集成
- **多模型支持**：DeepSeek、豆包、GLM、OpenAI 等多 AI 提供商
- **智能内容生成**：AI 辅助文章创作、营销文案生成
- **自动化任务**：定时任务调度，自动化内容生产
- **灵活配置**：可切换的 AI 服务提供商

### 📁 文件管理系统
- **阿里云 OSS 集成**：云端文件存储和管理
- **多格式支持**：图片、文档、视频等多种文件类型
- **文件分组管理**：按业务场景组织文件资源
- **临时文件清理**：自动化的临时文件管理机制

### 🌐 OpenAPI 接口
- **RESTful API**：标准化的 API 设计规范
- **第三方数据集成**：股票数据等外部 API 接入
- **应用密钥管理**：AppKey/AppSecret 安全认证
- **接口限流保护**：Token 机制防止滥用

---

## 🛠️ 技术栈

### 后端技术
- **核心框架**：Spring Boot 3.2.5
- **编程语言**：Java 17
- **ORM 框架**：MyBatis-Plus 3.5.5
- **数据库**：MySQL 8.0
- **连接池**：Druid 1.2.20
- **HTTP 客户端**：OkHttp 4.12.0
- **模板引擎**：Thymeleaf

### 前端技术
- **UI 框架**：Bootstrap 5
- **JavaScript**：jQuery 3.x
- **图表库**：ECharts
- **富文本编辑器**：UEditor
- **弹窗组件**：Layer

### 部署与运维
- **容器化**：Docker & Docker Compose
- **Web 服务器**：Nginx（反向代理）
- **构建工具**：Maven 3.6+

---

## 📦 快速开始

### 前置要求

- JDK 17 或更高版本
- Maven 3.6+
- MySQL 8.0+
- Docker & Docker Compose（可选，推荐用于生产环境）

### 方式一：本地开发运行

#### 1. 克隆项目

```bash
git clone https://github.com/cruise991/AbsAdmin.git
cd AbsAdmin
```

#### 2. 初始化数据库

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE absframe CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"

# 导入数据表结构
mysql -u root -p absframe < src/main/webapp/sql/absframe.sql
```

#### 3. 配置数据库连接

编辑 `src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/absframe?serverTimezone=Asia/Shanghai
spring.datasource.username=your_username
spring.datasource.password=your_password
```

#### 4. 配置 AI 服务（可选）

如需使用 AI 功能，在 `application.properties` 中配置相应的 API Key：

```properties
deepseek.api-key=your_deepseek_api_key
doubao.api-key=your_doubao_api_key
glm.api-key=your_glm_api_key
openai.api-key=your_openai_api_key
```

#### 5. 编译并运行

```bash
# 编译项目
mvn clean package -DskipTests

# 运行应用
java -jar target/AbsAdmin.jar
```

应用将在 `http://localhost:8020` 启动

### 方式二：Docker 部署（推荐）

#### 1. 构建镜像

```bash
docker build -t eduflow-platform:latest .
```

#### 2. 使用 Docker Compose 启动

```bash
docker-compose up -d
```

#### 3. 访问应用

打开浏览器访问：`http://localhost:8020`

默认管理员账号：
- 用户名：`admin`
- 密码：`123456`

---

## 📂 项目结构

```
AbsAdmin/
├── src/main/java/com/
│   ├── AbsAdminMain.java              # 应用主入口
│   └── abs/
│       ├── system/                    # 系统管理模块
│       │   ├── controller/            # 控制器层（23个）
│       │   ├── service/               # 服务层
│       │   ├── impl/                  # 服务实现
│       │   ├── domain/                # 实体类
│       │   ├── mapper/                # 数据访问层
│       │   ├── filter/                # 过滤器
│       │   ├── util/                  # 工具类
│       │   └── config/                # 配置类
│       ├── article/                   # 文章管理模块
│       │   ├── controller/
│       │   ├── service/
│       │   ├── domain/
│       │   └── impl/
│       └── openapi/                   # OpenAPI 模块
│           ├── controller/
│           ├── service/
│           └── impl/
├── src/main/resources/
│   ├── application.properties         # 应用配置
│   ├── db/                            # MyBatis XML 映射文件
│   ├── mapper/                        # 自定义 Mapper
│   ├── templates/                     # Thymeleaf 模板
│   └── www/                           # 静态资源（CSS/JS/Images）
├── src/main/webapp/
│   ├── sql/                           # 数据库脚本
│   └── driver/                        # ChromeDriver（爬虫用）
├── docker/                            # Docker 配置文件
├── docs/                              # 本地文档（不上传 Git）
├── pom.xml                            # Maven 配置
└── README.md                          # 项目说明
```

---

## 🔑 核心功能模块

### 1. 系统管理
- 用户管理 (`AbsUserController`)
- 角色管理 (`AbsRoleController`)
- 组织管理 (`AbsOuInfoController`)
- 菜单权限 (`AbsViewInfoController`)
- 系统参数 (`AbsSysConfigController`)
- 代码字典 (`AbsCodeItemController`)

### 2. 教育管理
- 学生信息 (`StudentInfoController`)
- 班级管理 (`BaseClassController`)
- 考试管理 (`TsInfoController`)
- 成绩分析
- 教师考核

### 3. 工作流引擎
- 流程定义 (`AbsFlowInfoController`)
- 流程实例
- 节点配置
- 流程事件

### 4. 内容管理
- 文章发布 (`ArticleController`)
- 首页配置 (`AbsHomeZjController`)
- Banner 管理
- 产品展示

### 5. AI 智能
- AI 内容生成 (`AiCreateController`)
- 多模型切换
- 定时任务

### 6. 文件管理
- 文件上传下载 (`AbsFileInfoController`)
- 阿里云 OSS 集成
- 素材管理 (`AbsMaterialController`)

### 7. OpenAPI
- 股票数据接口 (`StockOpenApiController`)
- 应用管理 (`AbsAppInfoController`)

---

## 🗄️ 数据库设计

主要数据表包括：

| 表名 | 说明 |
|------|------|
| `abs_userinfo` | 用户信息表 |
| `abs_roleinfo` | 角色信息表 |
| `abs_ouinfo` | 组织/部门信息表 |
| `abs_viewinfo` | 菜单/视图权限表 |
| `student_info` | 学生基本信息表 |
| `base_class` | 班级基础信息表 |
| `base_subject` | 学科基础信息表 |
| `exam_info` | 考试基础信息表 |
| `student_score` | 学生考试成绩表 |
| `teacher_info` | 教师基本信息表 |
| `abs_flowinfo` | 工作流定义表 |
| `abs_article` | 文章内容表 |
| `abs_fileinfo` | 文件信息表 |
| `abs_appinfo` | 应用密钥信息表 |

详细的数据库设计请参考 `src/main/webapp/sql/absframe.sql`

---

## 🔧 配置说明

### 数据库配置

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/absframe?serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=your_password
```

### MyBatis-Plus 配置

```properties
mybatis-plus.mapper-locations=classpath:mapper/*.xml
mybatis-plus.type-aliases-package=com.abs.system.domain
mybatis-plus.configuration.map-underscore-to-camel-case=true
```

### AI 服务配置

```properties
# AI 提供商选择: deepseek, doubao, glm, openai
ai.provider=glm

# GLM 配置
glm.api-key=your_api_key
glm.base-url=https://open.bigmodel.cn/api/paas/v4/
glm.model=glm-3-turbo

# DeepSeek 配置
deepseek.api-key=your_api_key
deepseek.base-url=https://api.deepseek.com

# 豆包配置
doubao.api-key=your_api_key
doubao.api-secret=your_secret
doubao.api-url=https://ark.cn-beijing.volces.com
```

### 文件上传配置

```properties
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
```

---

## 🚀 部署指南

### 生产环境部署

#### 1. 使用 Docker Compose

创建 `docker-compose.yml`：

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: your_password
      MYSQL_DATABASE: absframe
    volumes:
      - mysql_data:/var/lib/mysql
      - ./src/main/webapp/sql/absframe.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "3306:3306"

  app:
    build: .
    ports:
      - "8020:8020"
    depends_on:
      - mysql
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/absframe?serverTimezone=Asia/Shanghai
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: your_password

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
    depends_on:
      - app

volumes:
  mysql_data:
```

#### 2. 启动服务

```bash
docker-compose up -d
```

#### 3. 查看日志

```bash
docker-compose logs -f app
```

---

## 📖 API 文档

### 主要 API 端点

#### 用户管理
- `POST /api/user/login` - 用户登录
- `GET /api/user/list` - 获取用户列表
- `POST /api/user/add` - 新增用户
- `PUT /api/user/update` - 更新用户
- `DELETE /api/user/delete/{id}` - 删除用户

#### 学生管理
- `GET /api/student/list` - 获取学生列表
- `POST /api/student/add` - 新增学生
- `PUT /api/student/update` - 更新学生
- `GET /api/student/score/{studentId}` - 获取学生成绩

#### 文章管理
- `GET /api/article/list` - 获取文章列表
- `POST /api/article/add` - 发布文章
- `GET /api/article/detail/{id}` - 获取文章详情

#### OpenAPI
- `GET /api/openapi/stock/{code}` - 获取股票数据

完整的 API 文档请参考项目 Wiki 或使用 Swagger UI（待集成）

---

## 🤝 贡献指南

我们欢迎任何形式的贡献！

### 如何贡献

1. Fork 本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启一个 Pull Request

### 开发规范

- 遵循 [阿里巴巴 Java 开发手册](https://github.com/alibaba/p3c)
- 代码格式化：使用 IntelliJ IDEA 默认格式
- 提交信息：使用语义化提交消息
- 测试：新功能必须包含单元测试

---

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

---

## 👥 作者

- **cruise991** - [GitHub Profile](https://github.com/cruise991)

---

## 🙏 致谢

感谢以下开源项目：

- [Spring Boot](https://spring.io/projects/spring-boot)
- [MyBatis-Plus](https://baomidou.com/)
- [Bootstrap](https://getbootstrap.com/)
- [jQuery](https://jquery.com/)
- [Docker](https://www.docker.com/)

---

## 📞 联系方式

- 项目主页：https://github.com/cruise991/AbsAdmin
- 问题反馈：[Issues](https://github.com/cruise991/AbsAdmin/issues)
- Email：2501571227@qq.com

---

## ⭐ Star History

如果这个项目对您有帮助，请给我们一个 Star ⭐

[![Star History Chart](https://api.star-history.com/svg?repos=cruise991/AbsAdmin&type=Date)](https://star-history.com/#cruise991/AbsAdmin&Date)

---

**Made with ❤️ by EduFlow Team**

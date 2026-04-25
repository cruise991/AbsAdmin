# AbsAdmin Docker Compose 本地部署指南

## 📋 目录

- [概述](#概述)
- [环境要求](#环境要求)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
- [详细配置说明](#详细配置说明)
- [日常运维](#日常运维)
- [故障排查](#故障排查)
- [常见问题](#常见问题)

---

## 概述

本文档介绍如何使用 Docker Compose 在本地环境部署 AbsAdmin 应用。该方案包含 MySQL 数据库、Spring Boot 应用和 Nginx 反向代理三个核心组件。

### 架构说明

```
┌─────────────┐
│   Browser   │
└──────┬──────┘
       │
       ▼
┌─────────────┐      ┌──────────────┐
│   Nginx     │──────│  AbsAdmin    │
│  (Port 80)  │      │  (Port 8020) │
└─────────────┘      └──────┬───────┘
                            │
                            ▼
                     ┌──────────────┐
                     │   MySQL 8.0  │
                     │  (Port 3306) │
                     └──────────────┘
```

### 技术栈

- **应用框架**: Spring Boot 3.2.5
- **Java 版本**: JDK 17
- **数据库**: MySQL 8.0
- **Web 服务器**: Nginx (Alpine)
- **容器编排**: Docker Compose
- **构建工具**: Maven

---

## 环境要求

### 硬件要求

- **CPU**: 2+ 核心（推荐 4+ 核心）
- **内存**: 4GB+ 可用内存（推荐 8GB+）
- **磁盘**: 10GB+ 可用空间

### 软件要求

- **Docker**: 20.10+ 
- **Docker Compose**: 2.0+
- **Maven**: 3.6+（用于构建 JAR 包）
- **Git**: 2.0+（可选，用于版本控制）

### 验证环境

```bash
# 检查 Docker 版本
docker --version
docker-compose --version

# 检查 Maven 版本
mvn --version

# 确保 Docker 正在运行
docker info
```

---

## 项目结构

```
AbsAdmin/
├── docker-compose.yml          # Docker Compose 配置文件
├── Dockerfile                  # 应用镜像构建文件
├── .env                        # 环境变量配置（不提交到 Git）
├── .env.example                # 环境变量模板
├── .dockerignore               # Docker 构建忽略文件
├── init-db/                    # 数据库初始化脚本
│   ├── init.sql               # 数据库创建脚本
│   └── schema.sql             # 表结构脚本（需从现有数据库导出）
├── nginx/                      # Nginx 配置
│   ├── nginx.conf             # Nginx 主配置
│   └── conf.d/
│       └── absadmin.conf      # 应用反向代理配置
├── scripts/                    # 辅助脚本
│   ├── start.sh               # 快速启动脚本
│   ├── stop.sh                # 快速停止脚本
│   └── backup.sh              # 数据库备份脚本
├── data/                       # 数据持久化目录（自动创建）
│   ├── mysql/                 # MySQL 数据
│   ├── uploads/               # 应用上传文件
│   └── logs/                  # 应用日志
├── docs/                       # 文档目录
│   └── DOCKER-DEPLOYMENT.md   # 本文档
├── src/                        # 源代码
├── pom.xml                     # Maven 配置
└── README-Docker.md           # 快速参考卡片
```

---

## 快速开始

### 1. 克隆项目（如果还没有）

```bash
cd /Users/workspace/order/AbsAdmin
```

### 2. 修正 pom.xml 主类配置

**重要：** 在构建前需要修正 `pom.xml` 中的主类名称错误。

编辑 `pom.xml`，找到第 264 行：

```xml
<!-- 修改前 -->
<mainClass>com.AbsFrameMain</mainClass>

<!-- 修改后 -->
<mainClass>com.AbsAdminMain</mainClass>
```

### 3. 准备环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑 .env 文件，设置必要的配置
vim .env
```

最少需要配置：
```bash
MYSQL_ROOT_PASSWORD=your_secure_password
SPRING_DATASOURCE_PASSWORD=your_secure_password
```

### 4. 导出数据库结构（首次部署）

从现有阿里云 RDS 导出表结构：

```bash
mysqldump -h rm-bp1q6ok5t71ci853jqo.mysql.rds.aliyuncs.com \
  -u root -p \
  --no-data \
  --single-transaction \
  absframe > init-db/schema.sql
```

或者使用提供的空数据库结构（如果需要从头开始）。

### 5. 构建应用

```bash
# 清理并打包（跳过测试加快速度）
mvn clean package -DskipTests

# 验证 JAR 包生成
ls -lh target/AbsAdmin.jar
```

### 6. 启动服务

```bash
# 一键启动所有服务
docker-compose up -d

# 查看启动状态
docker-compose ps

# 查看实时日志
docker-compose logs -f
```

### 7. 验证部署

```bash
# 等待 1-2 分钟后，检查服务状态
docker-compose ps

# 测试应用访问
curl http://localhost:8020/login/istokenvalid

# 浏览器访问
open http://localhost:8020
```

**预期结果：** 看到登录页面或 API 响应

---

## 详细配置说明

### Docker Compose 配置

#### 服务说明

**1. MySQL 服务**
- 镜像：`mysql:8.0`
- 端口：3306（映射到宿主机）
- 字符集：utf8mb4
- 时区：Asia/Shanghai
- 数据持久化：`data/mysql` 目录

**2. AbsAdmin 服务**
- 构建：使用 Dockerfile
- 端口：8020
- 依赖：等待 MySQL 健康检查通过
- 环境变量：通过 `.env` 文件注入
- 卷挂载：
  - `data/uploads` - 上传文件
  - `data/logs` - 应用日志

**3. Nginx 服务（可选）**
- 镜像：`nginx:alpine`
- 端口：80, 443
- 功能：反向代理、静态资源缓存、WebSocket 支持

### 环境变量说明

#### 必需配置

```bash
# 数据库密码
MYSQL_ROOT_PASSWORD=your_password
SPRING_DATASOURCE_PASSWORD=your_password

# 数据库连接
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/absframe?serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=root
```

#### AI API 配置（可选）

```bash
DEEPSEEK_API_KEY=your_deepseek_key
DOUBAO_API_KEY=your_doubao_key
DOUBAO_API_SECRET=your_doubao_secret
GLM_API_KEY=your_glm_key
OPENAI_API_KEY=your_openai_key
```

#### JVM 参数（可选）

```bash
JAVA_OPTS=-Xms512m -Xmx1024m -XX:+UseG1GC
```

### Nginx 配置要点

**反向代理配置：**
- 代理到 `absadmin:8020`
- 支持 WebSocket 升级
- 设置合理的超时时间
- 启用 Gzip 压缩

**静态资源：**
- 上传文件通过 Nginx 直接提供
- 设置长期缓存头

---

## 日常运维

### 常用命令

```bash
# 启动服务
docker-compose up -d

# 停止服务
docker-compose down

# 重启服务
docker-compose restart

# 重启单个服务
docker-compose restart absadmin

# 查看日志
docker-compose logs -f absadmin
docker-compose logs -f mysql
docker-compose logs -f nginx

# 进入容器
docker-compose exec absadmin bash
docker-compose exec mysql bash
docker-compose exec nginx sh

# 查看资源使用
docker stats

# 清理无用资源
docker system prune -a
```

### 更新应用

```bash
# 1. 重新构建 JAR 包
mvn clean package -DskipTests

# 2. 重建并重启应用容器
docker-compose up -d --build absadmin

# 3. 验证新版本
docker-compose logs -f absadmin
```

### 数据库管理

#### 备份数据库

```bash
# 使用脚本备份
./scripts/backup.sh

# 或手动备份
docker-compose exec mysql mysqldump \
  -uroot -p$MYSQL_ROOT_PASSWORD \
  --single-transaction \
  absframe > backup_$(date +%Y%m%d_%H%M%S).sql
```

#### 恢复数据库

```bash
# 恢复指定备份文件
docker-compose exec -T mysql \
  mysql -uroot -p$MYSQL_ROOT_PASSWORD absframe < backup_20250424.sql
```

#### 连接数据库

```bash
# 命令行连接
docker-compose exec mysql mysql -uroot -p$MYSQL_ROOT_PASSWORD absframe

# 或使用 GUI 工具连接 localhost:3306
```

### 日志管理

```bash
# 查看最近 100 行日志
docker-compose logs --tail=100 absadmin

# 查看特定时间范围的日志
docker-compose logs --since="2025-04-24T10:00:00" absadmin

# 导出日志到文件
docker-compose logs absadmin > logs/app_$(date +%Y%m%d).log
```

---

## 故障排查

### 容器无法启动

```bash
# 1. 查看详细日志
docker-compose logs

# 2. 检查容器状态
docker-compose ps

# 3. 检查资源使用
docker stats

# 4. 查看 Docker 系统信息
docker info
```

### 数据库连接失败

**症状：** 应用日志中出现 "Cannot connect to database"

**解决步骤：**

```bash
# 1. 检查 MySQL 容器状态
docker-compose ps mysql

# 2. 检查 MySQL 日志
docker-compose logs mysql

# 3. 验证数据库是否就绪
docker-compose exec mysql mysqladmin ping -uroot -p$MYSQL_ROOT_PASSWORD

# 4. 检查网络连接
docker-compose exec absadmin ping mysql

# 5. 验证环境变量
docker-compose exec absadmin env | grep SPRING_DATASOURCE
```

### 应用启动失败

**常见原因：**

1. **JAR 包构建失败**
   ```bash
   mvn clean package -DskipTests
   ls -lh target/AbsAdmin.jar
   ```

2. **端口被占用**
   ```bash
   lsof -i :8020
   # 或修改 docker-compose.yml 中的端口映射
   ```

3. **内存不足**
   ```bash
   # 调整 JVM 参数
   JAVA_OPTS=-Xms256m -Xmx512m
   ```

4. **配置文件错误**
   ```bash
   # 检查 .env 文件格式
   cat .env
   
   # 验证环境变量是否正确加载
   docker-compose exec absadmin env
   ```

### 文件上传失败

**检查清单：**

```bash
# 1. 确认上传目录存在且有权限
docker-compose exec absadmin ls -la /app/uploads

# 2. 检查磁盘空间
docker-compose exec absadmin df -h

# 3. 查看应用日志中的错误信息
docker-compose logs absadmin | grep -i "upload\|file"
```

### Nginx 反向代理问题

```bash
# 1. 测试 Nginx 配置
docker-compose exec nginx nginx -t

# 2. 重载 Nginx 配置
docker-compose exec nginx nginx -s reload

# 3. 检查 Nginx 日志
docker-compose logs nginx
```

### 性能问题

```bash
# 监控容器资源使用
docker stats

# 查看 Java 进程信息
docker-compose exec absadmin jps -l

# 检查数据库慢查询
docker-compose exec mysql mysql -uroot -p$MYSQL_ROOT_PASSWORD \
  -e "SHOW PROCESSLIST;"
```

---

## 常见问题

### Q1: 首次启动很慢怎么办？

**A:** 首次启动需要：
- 下载 Docker 镜像（~500MB）
- 初始化 MySQL 数据库
- Spring Boot 应用冷启动

**优化建议：**
- 使用国内 Docker 镜像加速器
- 预先下载所需镜像
- 增加 JVM 堆内存加快启动

### Q2: 如何修改应用端口？

**A:** 编辑 `docker-compose.yml`：

```yaml
services:
  absadmin:
    ports:
      - "9090:8020"  # 将宿主机的 9090 映射到容器的 8020
```

同时修改 Nginx 配置中的 upstream 地址。

### Q3: 数据会丢失吗？

**A:** 不会。数据通过 Docker Volume 持久化：
- MySQL 数据：`data/mysql`
- 上传文件：`data/uploads`

即使删除容器，数据仍然保留。

### Q4: 如何在生产环境部署？

**A:** 生产环境建议：
- 使用外部 MySQL 服务（如阿里云 RDS）
- 配置 SSL 证书
- 设置合理的资源限制
- 启用日志轮转
- 配置监控告警
- 定期备份数据库

### Q5: 支持 ARM64 架构吗？

**A:** 完全支持。本方案已在 Apple Silicon (M1/M2/M3/M4) Mac 上测试通过。

### Q6: 如何禁用 Nginx？

**A:** 直接访问应用端口：

```bash
# 不使用 Nginx，直接访问
http://localhost:8020

# 或者注释掉 docker-compose.yml 中的 nginx 服务
# 然后重启
docker-compose up -d
```

### Q7: ChromeDriver 在 Mac M4 上能用吗？

**A:** 项目中现有的 ChromeDriver 是 Windows 版本。如需使用 Selenium 功能：
- 在 Dockerfile 中下载 Linux ARM64 版本的 ChromeDriver
- 或暂时禁用相关功能

### Q8: 如何查看应用的实时日志？

**A:** 
```bash
# 持续跟踪日志
docker-compose logs -f absadmin

# 只看错误日志
docker-compose logs -f absadmin | grep ERROR

# 只看最近 50 行
docker-compose logs --tail=50 absadmin
```

### Q9: 数据库密码忘记了怎么办？

**A:** 
```bash
# 重置 MySQL root 密码
docker-compose exec mysql mysql -uroot -p$MYSQL_ROOT_PASSWORD \
  -e "ALTER USER 'root'@'%' IDENTIFIED BY 'new_password';"

# 同时更新 .env 文件中的密码
```

### Q10: 如何彻底清理环境？

**A:** 
```bash
# 停止并删除所有容器、网络
docker-compose down

# 删除数据卷（⚠️ 警告：会删除所有数据）
docker-compose down -v

# 删除镜像
docker rmi absadmin-absadmin

# 清理所有未使用的 Docker 资源
docker system prune -a --volumes
```

---

## 安全建议

### 1. 保护敏感信息

- ✅ 不要将 `.env` 文件提交到 Git
- ✅ 使用强密码
- ✅ 定期更换密码
- ✅ 限制数据库远程访问

### 2. 网络安全

- ✅ 仅暴露必要的端口
- ✅ 使用自定义网络隔离服务
- ✅ 生产环境启用 HTTPS
- ✅ 配置防火墙规则

### 3. 容器安全

- ✅ 使用非 root 用户运行应用
- ✅ 定期更新基础镜像
- ✅ 扫描镜像漏洞
- ✅ 限制容器资源

### 4. 数据安全

- ✅ 定期备份数据库
- ✅ 加密敏感数据
- ✅ 审计数据库访问
- ✅ 测试恢复流程

---

## 性能优化

### JVM 调优

根据机器配置调整 `.env` 中的 `JAVA_OPTS`：

```bash
# Mac Studio M4 Max (64GB RAM) - 推荐配置
JAVA_OPTS=-Xms2g -Xmx4g -XX:+UseG1GC -XX:MaxGCPauseMillis=200

# 中等配置 (8GB RAM)
JAVA_OPTS=-Xms512m -Xmx1g -XX:+UseG1GC

# 低配环境 (4GB RAM)
JAVA_OPTS=-Xms256m -Xmx512m -XX:+UseSerialGC
```

### MySQL 优化

在 `docker-compose.yml` 中添加：

```yaml
mysql:
  command:
    - --innodb-buffer-pool-size=1G
    - --max-connections=200
    - --query-cache-size=64M
```

### Nginx 优化

启用 Gzip 压缩、调整 worker 进程数、配置缓存策略。

---

## 附录

### A. Docker Compose 版本兼容性

本方案使用 Docker Compose v2 语法，兼容：
- Docker Compose 2.0+
- Docker Desktop 4.0+

### B. 支持的操作系统

- ✅ macOS (Intel & Apple Silicon)
- ✅ Linux (Ubuntu, CentOS, Debian 等)
- ✅ Windows 10/11 (WSL2)

### C. 相关资源

- [Docker 官方文档](https://docs.docker.com/)
- [Docker Compose 文档](https://docs.docker.com/compose/)
- [Spring Boot Docker 指南](https://spring.io/guides/topicals/spring-boot-docker/)
- [MySQL Docker 官方镜像](https://hub.docker.com/_/mysql)

### D. 联系与支持

如有问题，请：
1. 查看本文档的"故障排查"章节
2. 检查应用和容器日志
3. 搜索类似问题的解决方案
4. 联系项目维护者

---

**最后更新：** 2025-04-24  
**文档版本：** 1.0.0  
**适用项目版本：** AbsAdmin 0.0.1-SNAPSHOT

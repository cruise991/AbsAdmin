# 登录问题修复指南

## 问题描述
登录失败，后端返回 500 错误：`不存在的sqlid:userinfo.getUserMapByUsernameAndPassowrd`

## 根本原因
`SqlApplicationRunnerInit.java` 使用 `File` API 读取 JAR 包内的 SQL 配置文件，在 JAR 包环境下无法正确读取资源文件。

## 已完成的修复

### 1. 修改 SqlApplicationRunnerInit.java
- 使用 Spring 的 `PathMatchingResourcePatternResolver` 替代 `File` API
- 支持 JAR 包内资源加载
- 添加详细日志和错误处理

### 2. 修改 pom.xml
- 添加 `<resources>` 配置
- 确保 `src/main/resources/db/` 下的 XML 文件被打包

## 构建步骤

### 方案 1：使用现有 Docker 构建（推荐）

后台构建进程已在运行（PID 28354），等待网络恢复即可。

```bash
# 1. 等待构建完成（当前正在运行）
# 监控进度：
watch -n 5 'docker images | grep eduflow'

# 2. 构建完成后启动服务
cd /Users/workspace/order/AbsAdmin
docker-compose up -d

# 3. 查看启动日志，确认 SQL 加载成功
docker logs -f absadmin-app | grep "加载 SQL"

# 预期看到类似输出：
# 找到 20 个 SQL 配置文件
# 加载 SQL 文件: abs_userinfo_sql.xml，包含 15 条 SQL 语句
# *****************sql语句加载结束*****************

# 4. 测试登录
curl -X POST http://localhost:8020/login/userlogin \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

### 方案 2：手动构建（如果网络持续有问题）

```bash
cd /Users/workspace/order/AbsAdmin

# 停止现有容器
docker-compose down

# 清理旧镜像
docker rmi absadmin-absadmin 2>/dev/null || true

# 重新构建（使用本地缓存）
DOCKER_BUILDKIT=0 docker-compose build --no-cache absadmin

# 启动服务
docker-compose up -d

# 监控日志
docker logs -f absadmin-app
```

### 方案 3：本地开发模式（最快）

如果您有 Maven 环境，可以在本地运行：

```bash
cd /Users/workspace/order/AbsAdmin

# 本地构建和运行
mvn clean package -DskipTests
java -jar target/EduFlow-Platform.jar

# 或直接使用 Spring Boot Maven 插件
mvn spring-boot:run
```

## 验证修复

### 1. 检查 SQL 加载日志
```bash
docker logs absadmin-app 2>&1 | grep "加载 SQL"
```

应该看到类似输出：
```
找到 20 个 SQL 配置文件
加载 SQL 文件: abs_userinfo_sql.xml，包含 15 条 SQL 语句
加载 SQL 文件: abs_roleinfo_sql.xml，包含 8 条 SQL 语句
...
```

### 2. 测试登录接口
```bash
curl -X POST http://localhost:8020/login/userlogin \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

预期返回：
```json
{
  "data": {
    "token": "xxxxxxxx",
    "username": "admin",
    "ouname": "..."
  },
  "success": true,
  "code": "0000",
  "msg": "success"
}
```

### 3. 前端测试
```bash
# 启动前端
cd frontend
npm run dev

# 浏览器访问 http://localhost:3000
# 使用 admin/123456 登录
```

## 如果仍然失败

### 检查数据库
```bash
docker exec absadmin-mysql mysql -uroot -p'19950123qwe!@#' absframe -e "
SELECT loginname, password FROM abs_userinfo WHERE loginname='admin';
"
```

### 检查容器内 SQL 文件
```bash
docker exec absadmin-app sh -c "
  jar tf /app/app.jar | grep 'BOOT-INF/classes/db/.*sql.xml' | head -10
"
```

应该看到：
```
BOOT-INF/classes/db/abs_userinfo_sql.xml
BOOT-INF/classes/db/abs_roleinfo_sql.xml
...
```

## 修改的文件清单

1. `src/main/java/com/abs/system/SqlApplicationRunnerInit.java` - 修复资源加载
2. `pom.xml` - 添加资源配置
3. `frontend/vite.config.js` - 已添加代理配置
4. `frontend/src/views/Login.vue` - 登录页面
5. `frontend/src/views/Chat.vue` - AI 助手接入真实 API

## 联系支持

如果问题仍未解决，请提供以下信息：
1. Docker 构建日志
2. 应用启动日志（`docker logs absadmin-app`）
3. 数据库连接状态

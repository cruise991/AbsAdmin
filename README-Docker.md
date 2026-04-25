# AbsAdmin Docker 快速参考

## 🚀 快速开始

```bash
# 1. 构建 JAR 包
mvn clean package -DskipTests

# 2. 启动服务
docker-compose up -d

# 3. 查看状态
docker-compose ps

# 4. 访问应用
open http://localhost:8020
```

## 📋 常用命令

### 服务管理

```bash
# 启动
docker-compose up -d

# 停止
docker-compose down

# 重启
docker-compose restart

# 重建并启动
docker-compose up -d --build
```

### 日志查看

```bash
# 查看所有日志
docker-compose logs -f

# 查看应用日志
docker-compose logs -f absadmin

# 查看数据库日志
docker-compose logs -f mysql
```

### 数据库操作

```bash
# 进入 MySQL
docker-compose exec mysql mysql -uroot -p$MYSQL_ROOT_PASSWORD absframe

# 备份数据库
./scripts/backup.sh

# 恢复数据库
docker-compose exec -T mysql mysql -uroot -p$MYSQL_ROOT_PASSWORD absframe < backup.sql
```

### 容器操作

```bash
# 进入应用容器
docker-compose exec absadmin bash

# 进入数据库容器
docker-compose exec mysql bash

# 查看资源使用
docker stats
```

## 🔧 配置文件

- `.env` - 环境变量配置
- `docker-compose.yml` - 服务编排
- `Dockerfile` - 应用镜像
- `nginx/conf.d/absadmin.conf` - Nginx 配置

## 🌐 访问地址

- **应用**: http://localhost:8020
- **Nginx**: http://localhost
- **MySQL**: localhost:3306

## ⚠️ 注意事项

1. 首次启动需要 1-2 分钟
2. 确保 `.env` 文件存在
3. 确保 `target/AbsAdmin.jar` 已构建
4. 数据持久化在 Docker Volume 中

## 🆘 故障排查

```bash
# 检查容器状态
docker-compose ps

# 查看详细日志
docker-compose logs

# 检查资源使用
docker stats

# 重启单个服务
docker-compose restart absadmin
```

## 📚 详细文档

查看完整文档：[docs/DOCKER-DEPLOYMENT.md](docs/DOCKER-DEPLOYMENT.md)

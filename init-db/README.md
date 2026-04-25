# 数据库初始化说明

## 首次部署

### 方式一：从现有数据库导入（推荐）

如果您已有运行的 EduFlow Platform 数据库（如阿里云 RDS），可以导出表结构：

```bash
# 导出表结构（不含数据）
mysqldump -h rm-bp1q6ok5t71ci853jqo.mysql.rds.aliyuncs.com \
  -u root -p \
  --no-data \
  --single-transaction \
  absframe > init-db/schema.sql

# 或者导出完整数据（包含数据）
mysqldump -h rm-bp1q6ok5t71ci853jqo.mysql.rds.aliyuncs.com \
  -u root -p \
  --single-transaction \
  absframe > init-db/full_backup.sql
```

### 方式二：使用空数据库

如果从头开始，可以只使用 `init.sql` 创建空数据库，然后通过应用界面逐步添加数据。

## 后续更新

如果需要更新数据库结构：

```bash
# 进入 MySQL 容器
docker-compose exec mysql bash

# 在容器内执行 SQL 文件
mysql -uroot -p$MYSQL_ROOT_PASSWORD absframe < /docker-entrypoint-initdb.d/schema.sql
```

## 注意事项

1. `init-db/` 目录下的 `.sql` 文件会在 MySQL 容器首次启动时自动执行
2. 已存在的数据库不会重复执行初始化脚本
3. 如需重新初始化，需要删除数据卷：`docker-compose down -v`

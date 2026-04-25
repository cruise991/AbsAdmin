-- ============================================================
-- EduFlow Platform 数据库初始化脚本
-- ============================================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS absframe 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE absframe;

-- 设置时区
SET time_zone = '+08:00';

-- 注意：实际的表结构需要从现有数据库导出
-- 使用以下命令从阿里云 RDS 导出：
-- mysqldump -h rm-bp1q6ok5t71ci853jqo.mysql.rds.aliyuncs.com -u root -p --no-data absframe > schema.sql

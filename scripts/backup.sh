#!/bin/bash

# ============================================================
# EduFlow Platform 数据库备份脚本
# ============================================================

set -e

# 配置
BACKUP_DIR="./backups"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/absframe_backup_$TIMESTAMP.sql"

# 创建备份目录
mkdir -p "$BACKUP_DIR"

echo "=========================================="
echo "  EduFlow Platform 数据库备份脚本"
echo "=========================================="
echo ""
echo "📅 备份时间: $(date)"
echo "📁 备份文件: $BACKUP_FILE"
echo ""

# 检查 MySQL 容器是否运行
if ! docker-compose ps mysql | grep -q "Up"; then
    echo "❌ 错误: MySQL 容器未运行"
    exit 1
fi

# 执行备份
echo "🔄 开始备份..."
docker-compose exec -T mysql mysqldump \
    -uroot -p"${MYSQL_ROOT_PASSWORD:-19950123qwe!@#}" \
    --single-transaction \
    --routines \
    --triggers \
    --events \
    absframe > "$BACKUP_FILE"

# 检查备份是否成功
if [ $? -eq 0 ] && [ -f "$BACKUP_FILE" ]; then
    FILE_SIZE=$(du -h "$BACKUP_FILE" | cut -f1)
    echo "✅ 备份成功！"
    echo "📊 文件大小: $FILE_SIZE"
    echo "📁 文件位置: $BACKUP_FILE"
    
    # 保留最近 10 个备份
    echo ""
    echo "🧹 清理旧备份（保留最近 10 个）..."
    cd "$BACKUP_DIR"
    ls -t absframe_backup_*.sql | tail -n +11 | xargs -r rm
    echo "✅ 清理完成"
else
    echo "❌ 备份失败！"
    exit 1
fi

echo ""
echo "=========================================="
echo "  备份完成"
echo "=========================================="

#!/bin/bash

# ============================================================
# EduFlow Platform 快速停止脚本
# ============================================================

set -e

echo "=========================================="
echo "  EduFlow Platform Docker Compose 停止脚本"
echo "=========================================="
echo ""

# 确认操作
read -p "⚠️  确定要停止所有服务吗？(y/n): " confirm
if [ "$confirm" != "y" ] && [ "$confirm" != "Y" ]; then
    echo "❌ 操作已取消"
    exit 0
fi

echo ""
echo "🛑 停止服务..."
docker-compose down

echo ""
echo "✅ 服务已停止"
echo ""
echo "💡 提示:"
echo "  - 数据已持久化，下次启动不会丢失"
echo "  - 如需完全清理（包括数据），使用: docker-compose down -v"
echo ""

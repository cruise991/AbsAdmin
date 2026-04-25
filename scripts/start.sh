#!/bin/bash

# ============================================================
# EduFlow Platform 快速启动脚本
# ============================================================

set -e

echo "=========================================="
echo "  EduFlow Platform Docker Compose 启动脚本"
echo "=========================================="
echo ""

# 检查 Docker 是否运行
if ! docker info > /dev/null 2>&1; then
    echo "❌ 错误: Docker 未运行，请先启动 Docker Desktop"
    exit 1
fi

# 检查 .env 文件是否存在
if [ ! -f .env ]; then
    echo "⚠️  警告: .env 文件不存在，从 .env.example 复制..."
    cp .env.example .env
    echo "✅ 已创建 .env 文件，请根据需要编辑配置"
    echo ""
fi

# 检查 JAR 包是否存在
if [ ! -f target/EduFlow-Platform.jar ]; then
    echo "📦 JAR 包不存在，开始构建..."
    mvn clean package -DskipTests
    echo "✅ 构建完成"
    echo ""
fi

# 启动服务
echo "🚀 启动 Docker Compose 服务..."
docker-compose up -d

echo ""
echo "⏳ 等待服务启动..."
sleep 5

# 检查服务状态
echo ""
echo "📊 服务状态:"
docker-compose ps

echo ""
echo "=========================================="
echo "  ✅ 启动完成！"
echo "=========================================="
echo ""
echo "📝 常用命令:"
echo "  查看日志:     docker-compose logs -f"
echo "  停止服务:     docker-compose down"
echo "  重启服务:     docker-compose restart"
echo ""
echo "🌐 访问地址:"
echo "  应用:         http://localhost:8020"
echo "  Nginx:        http://localhost"
echo "  MySQL:        localhost:3306"
echo ""
echo "🔍 健康检查:"
echo "  等待 1-2 分钟后访问 http://localhost:8020"
echo ""

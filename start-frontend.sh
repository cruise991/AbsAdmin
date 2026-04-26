#!/bin/bash

# EduFlow 前端快速启动脚本

echo "=========================================="
echo "  EduFlow 前端项目启动"
echo "=========================================="
echo ""

# 检查 Node.js 是否安装
if ! command -v node &> /dev/null; then
    echo "❌ 错误: 未检测到 Node.js，请先安装 Node.js"
    echo "   下载地址: https://nodejs.org/"
    exit 1
fi

echo "✅ Node.js 版本: $(node -v)"
echo "✅ npm 版本: $(npm -v)"
echo ""

# 进入前端目录
cd "$(dirname "$0")/frontend" || exit 1

# 检查 node_modules 是否存在
if [ ! -d "node_modules" ]; then
    echo "📦 首次运行，正在安装依赖..."
    npm install
    if [ $? -ne 0 ]; then
        echo "❌ 依赖安装失败"
        exit 1
    fi
    echo "✅ 依赖安装完成"
    echo ""
fi

# 检查后端服务是否运行
echo "🔍 检查后端服务状态..."
if curl -s http://localhost:8020 > /dev/null 2>&1; then
    echo "✅ 后端服务运行正常 (http://localhost:8020)"
else
    echo "⚠️  警告: 后端服务未运行，请确保 Docker 容器已启动"
    echo "   运行命令: docker-compose up -d"
fi
echo ""

# 启动开发服务器
echo "🚀 启动前端开发服务器..."
echo "   访问地址: http://localhost:3000"
echo "   按 Ctrl+C 停止服务器"
echo ""
echo "=========================================="
echo ""

npm run dev

#!/bin/bash

# ============================================================
# 内容管理模块修复和部署脚本
# 日期: 2026-04-26
# 说明: 修复文章管理和文件管理的字段名问题，配置上传路径
# ============================================================

set -e

echo "=========================================="
echo "  EduFlow Platform 修复和部署脚本"
echo "=========================================="
echo ""

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 步骤 1: 配置数据库上传路径
echo -e "${YELLOW}[步骤 1/5] 配置数据库上传路径...${NC}"
docker exec absadmin-mysql mysql -uroot -p'19950123qwe!@#' -e "
USE absframe;

-- 检查是否已存在配置
SELECT configname, configvalue FROM abs_sysconfig WHERE configname = 'upload_path';

-- 如果不存在则插入
INSERT IGNORE INTO abs_sysconfig (rowguid, userguid, configname, configvalue, addtime, remark) 
VALUES (UUID(), 'system', 'upload_path', '/app/uploads', NOW(), '文件上传保存路径');

-- 如果已存在则更新
UPDATE abs_sysconfig 
SET configvalue = '/app/uploads', remark = '文件上传保存路径'
WHERE configname = 'upload_path';
" 2>/dev/null

echo -e "${GREEN}✓ 数据库配置完成${NC}"
echo ""

# 步骤 2: 确保上传目录存在
echo -e "${YELLOW}[步骤 2/5] 检查并创建上传目录...${NC}"
docker exec absadmin-app sh -c "
mkdir -p /app/uploads
chmod 755 /app/uploads
ls -la /app/ | grep uploads
" 2>/dev/null

echo -e "${GREEN}✓ 上传目录检查完成${NC}"
echo ""

# 步骤 3: 重启后端服务
echo -e "${YELLOW}[步骤 3/5] 重启后端服务...${NC}"
docker restart absadmin-app

echo "等待后端服务启动（30秒）..."
sleep 30

# 检查后端服务状态
if docker ps | grep -q "absadmin-app.*healthy\|absadmin-app.*Up"; then
    echo -e "${GREEN}✓ 后端服务启动成功${NC}"
else
    echo -e "${RED}✗ 后端服务启动失败，请检查日志${NC}"
    docker logs absadmin-app --tail 20
    exit 1
fi
echo ""

# 步骤 4: 构建前端
echo -e "${YELLOW}[步骤 4/5] 构建前端应用...${NC}"
cd /Users/workspace/order/AbsAdmin/frontend
npm run build

echo -e "${GREEN}✓ 前端构建完成${NC}"
echo ""

# 步骤 5: 部署到 Docker
echo -e "${YELLOW}[步骤 5/5] 部署到 Docker 容器...${NC}"

# 清理旧资源
docker exec eduflow-frontend sh -c "rm -rf /usr/share/nginx/html/assets/* /usr/share/nginx/html/*.js /usr/share/nginx/html/*.css"

# 复制新资源
docker cp /Users/workspace/order/AbsAdmin/frontend/dist/. eduflow-frontend:/usr/share/nginx/html/

# 重载 Nginx
docker exec eduflow-frontend nginx -s reload

echo -e "${GREEN}✓ 前端部署完成${NC}"
echo ""

# 验证部署
echo "=========================================="
echo "  验证部署"
echo "=========================================="
echo ""

echo -e "${YELLOW}检查服务状态...${NC}"
docker ps --filter "name=absadmin" --filter "name=eduflow" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo ""
echo -e "${YELLOW}测试后端API...${NC}"
curl -s http://localhost:8020/login/istokenvalid | python3 -m json.tool 2>/dev/null || curl -s http://localhost:8020/login/istokenvalid

echo ""
echo -e "${YELLOW}测试上传配置...${NC}"
curl -s -X POST http://localhost:3006/api/absfile/getpicfilelist \
  -H "Content-Type: application/json" \
  -d '{"filename":"","filetype":"","page":1,"limit":10}' | python3 -m json.tool 2>/dev/null | head -5

echo ""
echo "=========================================="
echo -e "${GREEN}✓ 所有修复和部署完成！${NC}"
echo "=========================================="
echo ""
echo "后续操作："
echo "1. 清除浏览器缓存（Cmd + Shift + R 或 Ctrl + F5）"
echo "2. 访问 http://localhost:3006 测试文章管理"
echo "3. 访问 http://localhost:3006 测试文件管理和上传"
echo ""
echo "详细修复报告："
echo "- /Users/workspace/order/AbsAdmin/docs/CONTENT-MODULE-FIX-REPORT.md"
echo ""
